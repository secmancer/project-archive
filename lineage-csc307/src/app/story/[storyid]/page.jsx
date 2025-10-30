import StoryFlow from "@/components/storyflow";
import { auth } from "@clerk/nextjs/server";

export default async function StoryPage() {
  const { userId, redirectToSignIn } = await auth();
  if (!userId) return redirectToSignIn();

  return <StoryFlow userId={userId} />;
}
