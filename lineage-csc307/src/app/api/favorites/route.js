import prisma from "@/lib/prisma";
import { NextResponse } from "next/server";

export async function POST(req) {
  try {
    const { userId, storyId } = await req.json();

    // Check if already favorited
    const existingFavorite = await prisma.userStoryMapping.findFirst({
      where: { user_id: userId, story_id: storyId },
    });

    if (existingFavorite) {
      await prisma.userStoryMapping.delete({
        where: { id: existingFavorite.id },
      });
      return NextResponse.json(
        { message: "Story unfavorited successfully" },
        { status: 200 },
      );
    }

    // Create favorite entry
    await prisma.userStoryMapping.create({
      data: { user_id: userId, story_id: storyId, favorite: true },
    });

    return NextResponse.json(
      { message: "Story favorited successfully" },
      { status: 201 },
    );
  } catch (error) {
    console.error("Error toggling favorite status:", error);
    return NextResponse.json(
      { error: "Internal server error" },
      { status: 500 },
    );
  }
}

export async function GET(req) {
  try {
    const { searchParams } = new URL(req.url);
    const userId = searchParams.get("userId");

    if (!userId) {
      return NextResponse.json(
        { error: "User ID is required" },
        { status: 400 },
      );
    }

    const favorites = await prisma.userStoryMapping.findMany({
      where: { user_id: userId, favorite: true },
      include: { story: true }, // Include story details
    });

    return NextResponse.json({ favorites }, { status: 200 });
  } catch (error) {
    console.error("Error fetching favorites:", error);
    return NextResponse.json(
      { error: "Internal server error" },
      { status: 500 },
    );
  }
}
