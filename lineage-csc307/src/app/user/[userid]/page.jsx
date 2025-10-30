import moment from "moment";
import { ArrowRight } from "lucide-react";
import prisma from "@/lib/prisma";
import Link from "next/link";
import { auth, clerkClient } from "@clerk/nextjs/server";

export default async function Page({ params }) {
  const { userid } = params;
  const { userId: currentUserId } = auth();
  const client = await clerkClient();

  // Get user information from Clerk
  try {
    const profileUser = await client.users.getUser(userid);
    
    // we can go directly to prisma since this is server side component
    const stories = await prisma.story.findMany({
      where: {
        author_id: userid,
      },
      include: {
        root_node: true,
      },
      orderBy: {
        most_recent: 'desc',
      },
    });

    const contributions = await prisma.storyNode.findMany({
      where: {
        writer_id: userid,
      },
      orderBy: {
        created_date: 'desc',
      },
    });

    // Set titles based on whether user is viewing their own profile
    let displayName;
    if (profileUser.firstName && profileUser.lastName) {
      displayName = `${profileUser.firstName} ${profileUser.lastName}`;
    } else if (profileUser.firstName) {
      displayName = profileUser.firstName;
    } else {
      displayName = profileUser.emailAddresses[0]?.emailAddress || "User";
    }
    const storiesTitle = currentUserId === userid ? "Your Stories" : `${displayName}'s Stories`;
    const contributionsTitle = currentUserId === userid ? "Your Contributions" : `${displayName}'s Contributions`;

    return (
      <div className="w-full h-full">
        <div className="py-4 flex w-full h-full">
          <div className="w-full flex flex-col border-r border-zinc-200">
            <h1 className="w-full text-center font-semibold">
              {storiesTitle}
            </h1>
            <div className="flex flex-col gap-4 p-4">
              {stories.map((story) => (
                <div key={story.id} className="w-full border border-zinc-200 rounded-md p-4 min-h-40 flex flex-col">
                  <div className="flex flex-row w-full justify-between mb-2">
                    <div className="text-lg font-semibold">{story.title}</div>
                    <div className="text-sm text-zinc-500">{moment(story.most_recent).format("MMM D, YYYY")}</div>
                  </div>
                  <div className="text-sm text-zinc-500">
                    {story.root_node?.body || "No content available"}
                  </div>
                  <Link href={`/story/${story.id}`} className="group flex items-center gap-2 w-min whitespace-nowrap mt-auto mt-2 ml-auto cursor-pointer">
                    <p className="text-sm group-hover:underline">
                      View Story
                    </p>
                    <ArrowRight className="w-4 h-4 group-hover:translate-x-1 transition-all duration-300" />
                  </Link>
                </div>
              ))}
            </div>
          </div>
          <div className="w-full flex flex-col border-l border-zinc-200">
            <h1 className="w-full text-center font-semibold">
              {contributionsTitle}
            </h1>
            <div className="flex flex-col gap-4 p-4">
              {contributions.map((contribution) => (
                <div key={contribution.id} className="w-full border border-zinc-200 rounded-md p-4 min-h-40 flex flex-col">
                  <div className="flex flex-row w-full justify-between mb-2">
                    <div className="text-lg font-semibold">{contribution.title}</div>
                    <div className="text-sm text-zinc-500">{moment(contribution.created_date).format("MMM D, YYYY")}</div>
                  </div>
                  <div className="text-sm text-zinc-500">
                    {contribution.body || "No content available"}
                  </div>
                  <Link href={`/story/${contribution.story_id}`} className="group flex items-center gap-2 w-min whitespace-nowrap mt-auto mt-2 ml-auto cursor-pointer">
                    <p className="text-sm group-hover:underline">
                      View Story
                    </p>
                    <ArrowRight className="w-4 h-4 group-hover:translate-x-1 transition-all duration-300" />
                  </Link>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>
    );
  } catch (error) {
    console.error("Error fetching user:", error);
    return (
      <div className="w-full h-full flex items-center justify-center">
        <div className="text-center">
          <h1 className="text-xl font-semibold mb-2">User not found</h1>
          <p className="text-zinc-500">We couldn't find a user with the provided ID: {userid}</p>
          <Link href="/" className="text-blue-500 hover:underline mt-4 inline-block">
            Return to Home
          </Link>
        </div>
      </div>
    );
  }
}
