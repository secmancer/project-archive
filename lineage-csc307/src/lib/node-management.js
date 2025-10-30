// Function to calculate the depth of each node
export const calculateNodeDepths = (nodes, rootNodeId) => {
  const nodeMap = {};
  nodes.forEach((node) => {
    nodeMap[node.id] = node;
  });

  const calculateDepth = (nodeId, currentDepth) => {
    const node = nodeMap[nodeId];
    if (!node) return currentDepth;
    node.data.depth = currentDepth;
    node.children.forEach((childId) =>
      calculateDepth(childId, currentDepth + 1),
    );
  };

  calculateDepth(rootNodeId, 0);
};

// Function to group nodes by their depth
export const groupNodesByDepth = (nodes) => {
  const layers = {};
  nodes.forEach((node) => {
    const depth = node.data.depth;
    if (!layers[depth]) {
      layers[depth] = [];
    }
    layers[depth].push(node);
  });
  return layers;
};

// Function to determine the author and community nodes for each layer
export const determineAuthorAndCommunityNodes = (layers) => {
  const layerInfo = {};
  Object.keys(layers).forEach((layer) => {
    const nodes = layers[layer];
    const authorSelected = nodes.find((node) => node.data.isAuthorSelected);
    const sortedByVotes = [...nodes].sort(
      (a, b) => b.data.votes - a.data.votes,
    );
    const mostPopular = sortedByVotes[0];
    const secondMostPopular = sortedByVotes[1];

    layerInfo[layer] = {
      authorNode: authorSelected,
      communityNode:
        authorSelected === mostPopular ? secondMostPopular : mostPopular,
    };
  });
  return layerInfo;
};

// Function to update the colors of the nodes based on author and community selection
export const updateNodeColors = (nodes, layerInfo) => {
  nodes.forEach((node) => {
    node.data.color = "default"; // Reset all node colors
  });

  Object.keys(layerInfo).forEach((layer) => {
    const { authorNode, communityNode } = layerInfo[layer];
    if (authorNode) {
      authorNode.data.color = "#95B9C7"; // Baby blue color
      authorNode.data.is_chosen_path = true; // Set is_chosen_path to true for author node
    }
    if (communityNode) {
      communityNode.data.color = "#FFD700"; // Gold color
    }
  });
};
