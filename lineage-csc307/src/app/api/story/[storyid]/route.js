import prisma from "@/lib/prisma";

export async function DELETE(request) {
  const { storyid } = request.query;
  try {
    const storyToAnonymize = await prisma.story.findUnique({
      where: { id: parseInt(storyid) },
    });

    if (!storyToAnonymize) {
      return new Response(JSON.stringify({ error: "Story not found" }), {
        status: 404,
      });
    }

    const anonymizedStory = await prisma.story.update({
      where: { id: parseInt(storyid) },
      data: {
        title: "Deleted",
        author_id: null,
        author: null,
        most_recent: new Date(),
      },
    });

    return new Response(JSON.stringify(anonymizedStory), { status: 200 });
  } catch (error) {
    console.error(error);
    return new Response(
      JSON.stringify({ error: "Failed to anonymize story" }),
      { status: 500 },
    );
  }
}

// GET request
export async function GET(request, { params }) {
  const { storyid } = params;

  try {
    const story = await prisma.story.findUnique({
      where: { id: Number(storyid) },
      include: {
        nodes: {
          include: {
            children: true,
            outgoing_edges: true,
            incoming_edges: true,
          },
        },
        edges: true,
      },
    });

    if (!story) {
      return new Response(JSON.stringify({ error: "Story not found" }), {
        status: 404,
      });
    }

    return new Response(JSON.stringify({ story }), { status: 200 });
  } catch (error) {
    console.error(error);
    return new Response(JSON.stringify({ error: "Failed to fetch story" }), {
      status: 500,
    });
  }
}
