"use client";

import { useAuth } from "@clerk/nextjs";
import { memo, useState, useEffect } from "react";
import { BaseHandle } from "@/components/base-handle";
import { BaseNode } from "@/components/base-node";
import {
  NodeHeader,
  NodeHeaderTitle,
  NodeHeaderActions,
  NodeHeaderMenuAction,
} from "@/components/node-header";
import {
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
} from "@/components/ui/dropdown-menu";
import { ArrowUp, ArrowDown, Plus } from "lucide-react";
import { Position } from "@xyflow/react";
import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogHeader,
  DialogTrigger,
  DialogContent,
  DialogFooter,
  DialogTitle,
  DialogDescription,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { TextArea } from "@radix-ui/themes";
import { ComboBox } from "@/components/ui/combobox";
import axios from "axios";

const StoryNode = memo(
  ({
    selected,
    data,
    addNewNode,
    updateVotes,
    onReport,
    userID,
    onSelectForAuthor,
  }) => {
    const [votes, setVotes] = useState(data.votes);
    const [isReported, setIsReported] = useState(data.reported);
    const [isAddDialogOpen, setIsAddDialogOpen] = useState(false);
    const [isReportDialogOpen, setIsReportDialogOpen] = useState(false);
    const [isEditDialogOpen, setIsEditDialogOpen] = useState(false);
    const [newNodeTitle, setNewNodeTitle] = useState("Untitled Node");
    const [newNodeBody, setNewNodeBody] = useState("Start writing here...");
    const [editNodeTitle, setEditNodeTitle] = useState(data.title);
    const [editNodeBody, setEditNodeBody] = useState(data.body);
    const [charCount, setCharCount] = useState(0);
    const { userId } = useAuth(); // Fetch the current user's ID

    const themes = [
      { label: "Horror", value: "horror" },
      { label: "Comedy", value: "comedy" },
      { label: "Action", value: "action" },
      { label: "Mystery", value: "mystery" },
      { label: "Drama", value: "drama" },
      { label: "Romance", value: "romance" },
      { label: "Fantasy", value: "fantasy" },
      { label: "Sci-Fi", value: "sci-fi" },
      { label: "Adventure", value: "adventure" },
    ];

    useEffect(() => {
      setVotes(data.votes);
      setIsReported(data.reported);
    }, [data.votes, data.reported]);

    useEffect(() => {
      setCharCount(editNodeBody.length);
    }, [editNodeBody]);

    const handleUpvote = async () => {
      const newVotes = votes + 1;
      setVotes(newVotes); // Optimistically update the UI
      try {
        const response = await axios.patch(`/api/storynode/${data.id}`, {
          upvotes: newVotes,
        });
        const updatedVotes = response.data.upvotes;
        setVotes(updatedVotes); // Update with the response from the server
        updateVotes(data.id.toString(), updatedVotes); // Update the parent component's state
      } catch (error) {
        console.error("Error upvoting:", error);
        setVotes(votes); // Revert to the previous state if the API call fails
      }
    };

    const handleDownvote = async () => {
      const newVotes = votes - 1;
      setVotes(newVotes); // Optimistically update the UI
      try {
        const response = await axios.patch(`/api/storynode/${data.id}`, {
          upvotes: newVotes,
        });
        const updatedVotes = response.data.upvotes;
        setVotes(updatedVotes); // Update with the response from the server
        updateVotes(data.id.toString(), updatedVotes); // Update the parent component's state
      } catch (error) {
        console.error("Error downvoting:", error);
        setVotes(votes); // Revert to the previous state if the API call fails
      }
    };

    const handleFavorite = async () => {
      if (!userId) {
        alert("You must be logged in to favorite a story.");
        return;
      }

      try {
        console.log("Story ID being sent:", data.storyId); // Add this debug line
        const response = await axios.post("/api/favorites", {
          userId,
          storyId: data.storyId,
        });

        alert(response.data.message || "Story favorited!");
      } catch (error) {
        console.error("Error favoriting story:", error.response?.data || error);
        alert("Error favoriting story.");
      }
    };

    const handleAddNode = () => {
      if (typeof addNewNode === "function") {
        addNewNode(data.id, newNodeTitle, newNodeBody);
      } else {
        console.error("addNewNode is not a function!", addNewNode);
      }
    };

    const handleReport = async () => {
      const response = await axios.get(`/api/storynode/${data.id}`);
      const gotReported = response.data.storynode.reported;

      if (gotReported) {
        window.alert("This node has already been reported.");
        return;
      }

      try {
        await axios.patch(`/api/storynode/${data.id}`, {
          reported: true,
        });
        setIsReported(true);
        onReport(data.id, true);
      } catch (error) {
        console.error("Error reporting node:", error);
      }
    };

    const handleSaveChanges = async () => {
      try {
        await axios.patch(`/api/storynode/${data.id}`, {
          title: editNodeTitle,
          body: editNodeBody,
        });
        data.title = editNodeTitle;
        data.body = editNodeBody;
        setIsEditDialogOpen(false);
      } catch (error) {
        console.error("Error saving changes:", error);
      }
    };

    return (
      <BaseNode
        selected={selected}
        className="px-3 py-2 flex"
        style={{ backgroundColor: data.color }}
      >
        <BaseHandle
          id="target-storynode"
          type="target"
          position={Position.Top}
        />
        <div className="flex flex-col items-center mr-3">
          <Button
            onClick={handleUpvote}
            size="sm"
            variant="secondary"
            className="rounded-full mb-1"
          >
            <ArrowUp size={16} />
          </Button>
          <span>{votes}</span>
          <Button
            onClick={handleDownvote}
            size="sm"
            variant="secondary"
            className="rounded-full mt-1"
          >
            <ArrowDown size={16} />
          </Button>
        </div>
        <div className="flex-grow">
          <NodeHeader className="-mx-3 -mt-2 border-b">
            <NodeHeaderTitle>{data.title}</NodeHeaderTitle>
            <NodeHeaderActions>
              <NodeHeaderMenuAction label="Node options">
                <DropdownMenuLabel>Menu</DropdownMenuLabel>
                <DropdownMenuSeparator />
                {data.userID === userID && (
                  <DropdownMenuItem onSelect={() => setIsEditDialogOpen(true)}>
                    Edit
                  </DropdownMenuItem>
                )}
                {data.authorID === userID && (
                  <DropdownMenuItem onSelect={() => onSelectForAuthor(data.id)}>
                    Select for Author Branch
                  </DropdownMenuItem>
                )}
                <DropdownMenuItem onSelect={handleFavorite}>
                  Favorite
                </DropdownMenuItem>
                <DropdownMenuItem onSelect={() => setIsReportDialogOpen(true)}>
                  Report
                </DropdownMenuItem>
              </NodeHeaderMenuAction>
            </NodeHeaderActions>
          </NodeHeader>
          <div
            className="mt-2"
            style={{
              width: "200px",
              whiteSpace: "normal",
              wordWrap: "break-word",
            }}
          >
            <p>{data.body}</p>
          </div>
          <Dialog open={isAddDialogOpen} onOpenChange={setIsAddDialogOpen}>
            <DialogTrigger asChild>
              <Button
                onClick={() => setIsAddDialogOpen(true)}
                size="sm"
                variant="secondary"
                className="absolute bottom-1 right-1 rounded-full bg-blue-500 text-white shadow-md hover:bg-blue-600 transition"
              >
                <Plus size={12} />
              </Button>
            </DialogTrigger>
            <DialogContent className="sm:max-w-[425px]">
              <DialogHeader>
                <DialogTitle>Add New Node</DialogTitle>
                <DialogDescription>
                  Time to get writing, let's make your new story node!
                </DialogDescription>
              </DialogHeader>
              <div className="grid gap-4 py-4">
                <div className="grid grid-cols-4 items-center gap-4">
                  <Label htmlFor="title" className="text-right">
                    Title
                  </Label>
                  <Input
                    id="title"
                    value={newNodeTitle}
                    onChange={(e) => setNewNodeTitle(e.target.value)}
                    className="col-span-3"
                    maxLength={50}
                  />
                </div>
                <div className="grid grid-cols-4 items-center gap-4">
                  <Label htmlFor="body" className="text-right">
                    Body
                  </Label>
                  <TextArea
                    id="body"
                    value={newNodeBody}
                    onChange={(e) => setNewNodeBody(e.target.value)}
                    className="col-span-3"
                    rows={4}
                    maxLength={280}
                  />
                  <div className="col-span-3 text-right text-sm text-gray-500">
                    {newNodeBody.length} / 280 characters
                  </div>
                </div>
              </div>
              <DialogFooter>
                <Button type="submit" onClick={handleAddNode}>
                  Add node
                </Button>
              </DialogFooter>
            </DialogContent>
          </Dialog>
        </div>
        <BaseHandle
          id="source-storynode"
          type="source"
          position={Position.Bottom}
        />
        <Dialog open={isReportDialogOpen} onOpenChange={setIsReportDialogOpen}>
          <DialogContent>
            <DialogTitle>Confirm Report</DialogTitle>
            <DialogDescription>
              Are you sure you want to report this node?
            </DialogDescription>
            <DialogFooter>
              <Button
                variant="secondary"
                onClick={() => setIsReportDialogOpen(false)}
              >
                Cancel
              </Button>
              <Button
                onClick={() => {
                  handleReport();
                  setIsReportDialogOpen(false);
                }}
              >
                Report
              </Button>
            </DialogFooter>
          </DialogContent>
        </Dialog>
        <Dialog open={isEditDialogOpen} onOpenChange={setIsEditDialogOpen}>
          <DialogContent className="sm:max-w-[425px]">
            <DialogHeader>
              <DialogTitle>Edit Node</DialogTitle>
              <DialogDescription>
                Let's make changes to your node!
              </DialogDescription>
            </DialogHeader>
            <div className="grid gap-4 py-4">
              <div className="grid grid-cols-4 items-center gap-4">
                <Label htmlFor="title" className="text-right">
                  Title
                </Label>
                <Input
                  id="title"
                  value={editNodeTitle}
                  onChange={(e) => setEditNodeTitle(e.target.value)}
                  className="col-span-3"
                  maxLength={50}
                />
              </div>
              <div className="grid grid-cols-4 items-center gap-4">
                <Label htmlFor="body" className="text-right">
                  Body
                </Label>
                <TextArea
                  id="body"
                  value={editNodeBody}
                  onChange={(e) => setEditNodeBody(e.target.value)}
                  className="col-span-3"
                  rows={4}
                  maxLength={280}
                />
                <div className="col-span-3 text-right text-sm text-gray-500">
                  {charCount} / 280 characters
                </div>
              </div>
            </div>
            <DialogFooter>
              <Button type="submit" onClick={handleSaveChanges}>
                Save changes
              </Button>
            </DialogFooter>
          </DialogContent>
        </Dialog>
      </BaseNode>
    );
  },
);

export default StoryNode;
