import prisma from "@/lib/prisma";

export async function POST(request) {
  try {
    const { source, target, story_id } = await request.json();
    const result = await prisma.edge.create({
      data: {
        source_id: source,
        target_id: target,
        story_id: story_id,
      },
    });
    return new Response(JSON.stringify(result), {
      status: 200,
      headers: { "Content-Type": "application/json" },
    });
  } catch (error) {
    console.error("Error creating edge:", error);
    return new Response(JSON.stringify({ error: "Failed to create edge" }), {
      status: 500,
      headers: { "Content-Type": "application/json" },
    });
  }
}
