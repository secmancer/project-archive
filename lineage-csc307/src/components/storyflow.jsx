"use client";

import { useParams } from "next/navigation";
import React, { useEffect, useState } from "react";
import {
  ReactFlow,
  Controls,
  Background,
  useNodesState,
  useEdgesState,
} from "@xyflow/react";
import "@xyflow/react/dist/style.css";
import { ZoomSlider } from "@/components/zoom-slider/";
import axios from "axios";
import ELK from "elkjs/lib/elk.bundled.js";

import StoryNode from "./nodes/storynode";
import {
  calculateNodeDepths,
  groupNodesByDepth,
  determineAuthorAndCommunityNodes,
  updateNodeColors,
} from "@/lib/node-management";

const elk = new ELK();
const nodeWidth = 300;
const nodeHeight = 200;

const getLayoutedElements = async (nodes, edges) => {
  const graph = {
    id: "root",
    layoutOptions: {
      "elk.direction": "DOWN",
    },
    children: nodes.map((node) => ({
      id: node.id,
      width: nodeWidth,
      height: nodeHeight,
    })),
    edges: edges.map((edge) => ({
      id: edge.id,
      sources: [edge.source],
      targets: [edge.target],
    })),
  };

  const layout = await elk.layout(graph);

  const updatedNodes = nodes.map((node) => {
    const { x, y } = layout.children.find((n) => n.id === node.id);
    return {
      ...node,
      position: { x: x - nodeWidth / 2, y: y - nodeHeight / 2 },
      draggable: false,
      targetPosition: "top",
      sourcePosition: "bottom",
    };
  });

  const updatedEdges = edges.map((edge) => ({
    ...edge,
    animated: true,
    style: { stroke: "#000", strokeWidth: 2 },
  }));

  return { nodes: updatedNodes, edges: updatedEdges };
};

const StoryFlow = ({ userId }) => {
  const params = useParams();
  const storyID = params.storyid;
  const [nodes, setNodes, onNodesChange] = useNodesState([]);
  const [edges, setEdges, onEdgesChange] = useEdgesState([]);
  const [loading, setLoading] = useState(true);
  const [storyExists, setStoryExists] = useState(true);

  useEffect(() => {
    const fetchNodesAndEdges = async () => {
      setLoading(true);
      setStoryExists(true);
      try {
        const storyResponse = await axios.get(`/api/story/${storyID}`);
        if (storyResponse.status !== 200) {
          setStoryExists(false);
          return;
        }
        const story = storyResponse.data.story;
        const authorId = storyResponse.data.story.author_id;
        const rootNodeId = storyResponse.data.story.root_node_id; // Get the root node ID

        const fetchedNodes = story.nodes.map((node) => ({
          id: node.id.toString(),
          position: { x: 0, y: 0 },
          data: {
            id: node.id,
            title: node.title,
            body: node.body,
            votes: node.upvotes,
            reported: node.reported,
            userID: node.writer_id,
            authorID: authorId,
            storyId: Number(storyID), // Ensure this matches between both locations
            is_chosen_path: node.id === rootNodeId,
            color: node.id === rootNodeId ? "#95B9C7" : "default",
            isAuthorSelected: node.id === rootNodeId,
          },
          parent_id: node.parent_id,
          children: node.children.map((child) => child.id.toString()),
          type: "storyNode",
        }));

        const fetchedEdges = story.edges.map((edge) => ({
          id: edge.id.toString(),
          source: edge.source_id.toString(),
          target: edge.target_id.toString(),
          type: "smoothstep",
          animated: true,
        }));

        const { nodes: layoutedNodes, edges: layoutedEdges } =
          await getLayoutedElements(fetchedNodes, fetchedEdges);

        calculateNodeDepths(layoutedNodes, rootNodeId); // Calculate the depth of each node

        setNodes(layoutedNodes);
        setEdges(layoutedEdges);

        const layers = groupNodesByDepth(layoutedNodes);
        const layerInfo = determineAuthorAndCommunityNodes(layers);

        updateNodeColors(layoutedNodes, layerInfo);
        setNodes(layoutedNodes);
      } catch (error) {
        console.error("Error fetching nodes:", error);
        setStoryExists(false);
      } finally {
        setLoading(false);
      }
    };

    fetchNodesAndEdges();
  }, [storyID]);

  const addNewNode = async (parentId, title, body) => {
    try {
      const storyResponse = await axios.get(`/api/story/${storyID}`);
      const authorId = storyResponse.data.story.author_id;
      const rootNodeId = storyResponse.data.story.root_node_id; // Get the root node ID

      const response = await axios.post(`/api/storynode`, {
        parent_id: Number(parentId),
        title: title,
        body: body,
        upvotes: 0,
        story_id: Number(storyID),
        writer_id: userId,
      });

      console.log("StoryID in addNewNode:", storyID);
      const new_node = response.data;

      const newNode = {
        id: new_node.id.toString(),
        position: {
          x: 0,
          y: 0,
        },
        data: {
          id: new_node.id,
          title: new_node.title,
          body: new_node.body,
          votes: new_node.upvotes,
          reported: new_node.reported,
          userID: new_node.writer_id,
          authorID: authorId,
          storyId: Number(storyID), // Ensure this matches between both locations
          is_chosen_path: false,
          color: "default",
        },
        type: "storyNode",
        parent_id: new_node.parent_id,
        children: [],
      };

      const newEdge = {
        id: `${parentId}-${new_node.id}`,
        source: parentId.toString(),
        target: new_node.id.toString(),
        type: "smoothstep",
        animated: true,
      };

      const updatedNodes = [...nodes, newNode];
      const updatedEdges = [...edges, newEdge];

      await axios.post(`/api/storyedge`, {
        source: parentId,
        target: new_node.id,
        story_id: Number(storyID),
      });

      const { nodes: layoutedNodes, edges: layoutedEdges } =
        await getLayoutedElements(updatedNodes, updatedEdges);

      calculateNodeDepths(layoutedNodes, rootNodeId); // Calculate the depth of each node

      const layers = groupNodesByDepth(layoutedNodes);
      const layerInfo = determineAuthorAndCommunityNodes(layers);

      updateNodeColors(layoutedNodes, layerInfo);

      setNodes(layoutedNodes);
      setEdges(layoutedEdges);
    } catch (error) {
      console.error("Error adding node:", error);
    }
  };

  const updateVotes = (nodeId, newVotes) => {
    setNodes((prevNodes) =>
      prevNodes.map((node) =>
        node.id === nodeId
          ? { ...node, data: { ...node.data, votes: newVotes } }
          : node,
      ),
    );

    const layers = groupNodesByDepth(nodes);
    const layerInfo = determineAuthorAndCommunityNodes(layers);
    updateNodeColors(nodes, layerInfo);
  };

  const updateReported = (nodeId, reported) => {
    setNodes((prevNodes) =>
      prevNodes.map((node) =>
        node.id === nodeId
          ? { ...node, data: { ...node.data, reported: reported } }
          : node,
      ),
    );
  };

  const handleAuthorSelection = async (nodeId) => {
    const updatedNodes = nodes.map((node) => {
      if (node.data.id === nodeId) {
        return {
          ...node,
          data: {
            ...node.data,
            isAuthorSelected: !node.data.isAuthorSelected,
            is_chosen_path: !node.data.isAuthorSelected,
          },
        };
      } else {
        const isSameLayer =
          node.data.depth ===
          nodes.find((n) => n.data.id === nodeId).data.depth;
        return {
          ...node,
          data: {
            ...node.data,
            isAuthorSelected: isSameLayer ? false : node.data.isAuthorSelected,
            is_chosen_path: isSameLayer ? false : node.data.is_chosen_path,
          },
        };
      }
    });

    const layers = groupNodesByDepth(updatedNodes);
    const layerInfo = determineAuthorAndCommunityNodes(layers);
    updateNodeColors(updatedNodes, layerInfo);
    setNodes(updatedNodes);

    try {
      await axios.patch(`/api/storynode/${nodeId}`, {
        is_chosen_path: updatedNodes.find((node) => node.data.id === nodeId)
          .data.is_chosen_path,
      });
    } catch (error) {
      console.error("Error updating node:", error);
    }
  };

  if (loading) {
    return (
      <div className="w-full h-full flex items-center justify-center">
        Loading...
      </div>
    );
  }

  if (!storyExists) {
    return (
      <div className="w-full h-full flex flex-col items-center justify-center">
        <h1>Story Not Found!</h1>
        <p>The story you are trying to find doesn't exist</p>
      </div>
    );
  }

  return (
    <div style={{ height: "100%" }}>
      <ReactFlow
        nodes={nodes}
        edges={edges}
        onNodesChange={onNodesChange}
        onEdgesChange={onEdgesChange}
        nodeTypes={{
          storyNode: (props) => (
            <StoryNode
              {...props}
              addNewNode={addNewNode}
              updateVotes={updateVotes}
              onReport={updateReported}
              userID={userId}
              onSelectForAuthor={handleAuthorSelection}
            />
          ),
        }}
      >
        <Background />
        <ZoomSlider position="top-left" />
        <Controls />
      </ReactFlow>
    </div>
  );
};

export default StoryFlow;
