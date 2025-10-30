import prisma from "@/lib/prisma";

const VALID_THEMES = ["HORROR", "COMEDY", "ACTION", "MYSTERY", "DRAMA", "ROMANCE", "FANTASY", "SCIFI", "ADVENTURE"];

export async function POST(request) {
  try {
    const { author_id, title, theme } = await request.json();

    // Validate required fields
    if (!author_id || !title || !theme) {
      return new Response(JSON.stringify({ error: "Missing required fields" }), { status: 400 });
    }

    if (!VALID_THEMES.includes(theme)) {
      return new Response(JSON.stringify({ error: "Invalid theme" }), { status: 400 });
    }

    const result = await prisma.story.create({
      data: {
        author_id: author_id,
        title: title,
        theme: theme
      },
    });

    return new Response(JSON.stringify(result), { status: 201 });
  } catch (error) {
    console.error("Error creating story:", error);
    return new Response(JSON.stringify({ error: "Failed to create story" }), { status: 500 });
  }
}


export async function GET(req) {
  const { searchParams } = new URL(req.url);
  const query = searchParams.get("query");

  let result;
  if (query) {
    result = await prisma.story.findMany({
      where: {
        title: {
          contains: query,
          mode: "insensitive",
        },
      },
    });
  } else {
    result = await prisma.story.findMany();
  }

  return new Response(JSON.stringify(result), {
    status: 200,
    headers: { "Content-Type": "application/json" },
  });
}

export async function DELETE(request) {
  try {
    const { id } = await request.json();

    if (!id) {
      return new Response(JSON.stringify({ error: "Missing story ID" }), { status: 400 });
    }

    const deletedStory = await prisma.story.delete({
      where: { id: Number(id) },
    });

    return new Response(JSON.stringify(deletedStory), { status: 200 });
  } catch (error) {
    console.error("Error deleting story:", error);
    return new Response(JSON.stringify({ error: "Failed to delete story" }), { status: 500 });
  }
}

export async function PATCH(req) {
  try {
    const { id, theme, ...updateData } = await req.json();

    if (!id) {
      return new Response(JSON.stringify({ error: "Missing story ID" }), { status: 400 });
    }

    if (theme && !VALID_THEMES.includes(theme)) {
      return new Response(JSON.stringify({ error: "Invalid theme" }), { status: 400 });
    }

    const result = await prisma.story.update({
      where: { id: Number(id) },
      data: { ...updateData, ...(theme && { theme }) },
    });

    return new Response(JSON.stringify(result), {
      status: 200,
      headers: { "Content-Type": "application/json" },
    });
  } catch (error) {
    console.error("Error updating story:", error);
    return new Response(JSON.stringify({ error: "Failed to update story" }), {
      status: 500,
      headers: { "Content-Type": "application/json" },
    });
  }
}