import prisma from "@/lib/prisma";

export async function POST(request) {
  const { userStoryMapping } = await request.json();
  const newUserStoryMapping = await prisma.userstorymapping.create({data: {
    ...userStoryMapping
  }});
  return Response.json({userStoryMapping: newUserStoryMapping});
}
// This is a GET request for the user story mapping database
export async function GET(req) {
  const result = await prisma.userStoryMapping.findMany();
  return new Response(JSON.stringify(result), {
    status: 200,
    headers: {
      "Content-Type": "application/json",
    },
  });
}

// This is a PATCH request for the user story mapping database
export async function PATCH(req) {
  try {
    const { id, ...updateData } = await req.json();
    const result = await prisma.userStoryMapping.update({
      where: { id },
      data: updateData,
    });

    return new Response(JSON.stringify(result), {
      status: 200,
      headers: {
        "Content-Type": "application/json",
      },
    });
  } catch (error) {
    return new Response(
      JSON.stringify({ error: "Failed to update user story mapping" }),
      {
        status: 500,
        headers: {
          "Content-Type": "application/json",
        },
      },
    );
  }
}
