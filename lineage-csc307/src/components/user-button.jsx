import Link from "next/link";
import { Button } from "@/components/ui/button";
import { clerkClient } from "@clerk/nextjs/server";

export default async function UserButton({ userId }) {
  // Get user information from Clerk
  const client = await clerkClient();
  const user = await client.users.getUser(userId);
  
  let displayName;
  if (user.firstName && user.lastName) {
    displayName = `${user.firstName} ${user.lastName}`;
  } else if (user.firstName) {
    displayName = user.firstName;
  } else if (user.emailAddresses && user.emailAddresses.length > 0) {
    displayName = user.emailAddresses[0].emailAddress;
  } else {
    displayName = "Unknown User";
  }

  return (
    <Button 
      variant="ghost" 
      className="hover:bg-zinc-100 transition-colors duration-200"
      asChild
    >
      <Link href={`/user/${userId}`}>
        {displayName}
      </Link>
    </Button>
  );
}
