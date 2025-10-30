import prisma from "@/lib/prisma";
import Link from "next/link";
import UserButton from "@/components/user-button";
import FavoriteButton from "@/components/FavoriteButton";

async function getStories() {
  try {
    const storiesByTheme = await prisma.story.findMany({
      select: {
        id: true,
        title: true,
        theme: true,
        most_recent: true,
        root_node: {
          select: {
            body: true,
          },
        },
        author: {
          select: {
            id: true,
          },
        },
      },
      orderBy: [
        { theme: "asc" }, // Group stories by theme
        { most_recent: "desc" }, // Sort by most recent within each theme
      ],
    });

    // Group stories by theme
    const groupedStories = storiesByTheme.reduce((acc, story) => {
      const previewText = story.root_node?.body?.substring(0, 150) + "...";
      const storyData = {
        id: story.id,
        title: story.title,
        preview: previewText,
        author: story.author?.id || "Anonymous",
        date: story.most_recent.toLocaleDateString(),
      };

      if (!acc[story.theme]) {
        acc[story.theme] = [];
      }
      acc[story.theme].push(storyData);

      return acc;
    }, {});

    return groupedStories;
  } catch (error) {
    console.error("Failed to fetch stories:", error);
    return {};
  }
}

export default async function ExplorePage() {
  const storiesByTheme = await getStories();

  return (
    <div className="container mx-auto p-4">
      {Object.entries(storiesByTheme).map(([theme, stories]) => (
        <div key={theme} className="mb-8">
          <h2 className="text-2xl font-bold text-gray-800 mb-4 capitalize">
            {theme.toLowerCase()}
          </h2>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {stories.map((story) => (
              <div key={story.id} className="relative">
                <Link
                  href={`/story/${story.id}`}
                  className="transition-transform hover:scale-105"
                >
                  <div className="bg-white rounded-lg shadow p-4 h-full">
                    <h3 className="text-xl font-bold">{story.title}</h3>
                    <p className="text-gray-600">{story.preview}</p>
                    <div className="mt-2 flex items-center">
                      <UserButton userId={story.author} />
                      <span className="text-sm text-gray-500 ml-2">
                        {story.date}
                      </span>
                    </div>
                  </div>
                </Link>
                <FavoriteButton storyId={story.id} />
              </div>
            ))}
          </div>
        </div>
      ))}
    </div>
  );
}
