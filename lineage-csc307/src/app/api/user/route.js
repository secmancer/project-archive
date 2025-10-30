import prisma from "@/lib/prisma";

export async function POST(request) {
  const { id } = await request.json();
  const newUser = await prisma.user.create({
    data: {
      id: id,
    },
  });
  return new Response(JSON.stringify(newUser));
}

// This is a GET request for the user database
export async function GET(req) {
  const result = await prisma.user.findMany();
  return new Response(JSON.stringify(result), {
    status: 200,
    headers: {
      "Content-Type": "application/json",
    },
  });
}

// This is a PATCH request for the user database
export async function PATCH(req) {
  try {
    const { id, ...updateData } = await req.json();
    const result = await prisma.user.update({
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
    return new Response(JSON.stringify({ error: "Failed to update user" }), {
      status: 500,
      headers: {
        "Content-Type": "application/json",
      },
    });
  }
}
