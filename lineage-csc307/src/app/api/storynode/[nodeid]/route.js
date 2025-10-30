import prisma from "@/lib/prisma";

export async function DELETE(request) {
  const { nodeid } = request.query;
  try {
    const nodeToAnonymize = await prisma.storyNode.findUnique({
      where: { id: parseInt(nodeid) },
    });

    if (!nodeToAnonymize) {
      return new Response(JSON.stringify({ error: "Node not found" }), {
        status: 404,
      });
    }

    const anonymizedNode = await prisma.storyNode.update({
      where: { id: parseInt(nodeid) },
      data: {
        writer: null,
        writer_id: null,
        title: "Deleted",
        body: "This contribution has been deleted.",
        // Do we need to keep the upvotes to allow proper community path?
        upvotes: 0,
      },
    });

    return new Response(JSON.stringify(anonymizedNode), { status: 200 });
  } catch (error) {
    console.error(error);
    return new Response(JSON.stringify({ error: "Failed to anonymize node" }), {
      status: 500,
    });
  }
}

export async function PATCH(req, { params }) {
  try {
    const { nodeid } = await params;
    const updateData = await req.json();

    const result = await prisma.storyNode.update({
      where: { id: parseInt(nodeid) },
      data: updateData,
    });

    return new Response(JSON.stringify(result), {
      status: 200,
      headers: {
        "Content-Type": "application/json",
      },
    });
  } catch (error) {
    console.log(error);
  }
}

export async function GET(request, { params }) {
  const { nodeid } = params;

  try {
    const storynode = await prisma.storyNode.findUnique({
      where: { id: Number(nodeid) },
    });

    if (!storynode) {
      return new Response(JSON.stringify({ error: "Story node not found" }), {
        status: 404,
      });
    }

    return new Response(JSON.stringify({ storynode }), { status: 200 });
  } catch (error) {
    console.error(error);
    return new Response(
      JSON.stringify({ error: "Failed to fetch story node" }),
      {
        status: 500,
      },
    );
  }
}
