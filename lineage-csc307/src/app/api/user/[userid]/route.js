import prisma from "@/lib/prisma";

export async function GET(request, { params }) {
    const { userid } = params;

    try {
        const stories = await prisma.story.findMany({
            where: { author_id: userid },
            orderBy: { most_recent: "desc" },
        });

        return new Response(JSON.stringify(stories), {
            status: 200,
            headers: { "Content-Type": "application/json" },
        });
    } catch (error) {
        console.error("Error fetching user stories:", error);
        return new Response(JSON.stringify({ error: "Failed to fetch stories" }), {
            status: 500,
        });
    }
}

export async function DELETE(request) {
    const { userid } = request.query;
    try {
        await prisma.story.updateMany({
            where: { author_id: parseInt(userid) },
            data: { author_id: null },
        });

        await prisma.storyNode.updateMany({
            where: { writer_id: parseInt(userid) },
            data: { writer_id: null },
        });

        const deletedUser = await prisma.user.delete({
            where: { id: parseInt(userid) },
        });

        return new Response(JSON.stringify(deletedUser), { status: 200 });
    } catch (error) {
        console.error(error);
        return new Response(JSON.stringify({ error: "Failed to delete user" }), { status: 500 });
    }
}