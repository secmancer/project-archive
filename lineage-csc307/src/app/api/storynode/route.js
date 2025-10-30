import prisma from "@/lib/prisma";

export async function POST(request) {
  try {
    const storyNode = await request.json();

    // Validate required fields
    if (!storyNode.title || !storyNode.body || !storyNode.writer_id) {
      return new Response(JSON.stringify({ error: "Missing required fields" }), { status: 400 });
    }

    // If there's a parent node, story_id must exist
    if (storyNode.parent_id && !storyNode.story_id) {
      return new Response(JSON.stringify({ error: "Missing story_id for a child node" }), { status: 400 });
    }

    // Create story node
    const result = await prisma.storyNode.create({
      data: {
        story_id: storyNode.story_id || null, // Allow null for root nodes
        parent_id: storyNode.parent_id || null,
        title: storyNode.title,
        body: storyNode.body,
        is_chosen_path: false,
        upvotes: 0,
        writer_id: storyNode.writer_id,
      },
    });

    // If the node has a parent, create an edge
    if (storyNode.parent_id) {
      await prisma.edge.create({
        data: {
          source_id: storyNode.parent_id,
          target_id: result.id,
          story_id: storyNode.story_id,
        },
      });
    }

    return new Response(JSON.stringify(result), { status: 201 });
  } catch (error) {
    console.error("Error creating story node:", error);
    return new Response(JSON.stringify({ error: "Failed to create story node" }), { status: 500 });
  }
}
