import Link from "next/link";
import { Signature } from "lucide-react";
import { SignedIn, SignedOut, UserButton } from "@clerk/nextjs";
import { auth } from "@clerk/nextjs/server"
import { Button } from "@/components/ui/button";

const links = [
  {
    name: "Start Story",
    href: "/start-story",
  },
  {
    name: "Explore",
    href: "/explore",
  },
  {
    name: "Favorites",
    href: "/favorites",
  },
  {
    name: "Search",
    href: "/search",
  },
];

export default async function Navbar({ children }) {
  const { userId } = await auth();
  
  return (
    <div className="w-screen h-screen flex flex-col">
      <div className="flex flex-row border-b border-zinc-200">
        <Link
          className="p-2 m-2 h-12 w-12 flex items-center justify-center rounded-full hover:bg-zinc-200 transition-all duration-300"
          href="/"
        >
          <Signature size={36} />
        </Link>
        <div className="w-1/2 ml-auto flex items-center justify-between mr-8">
          {links.map((link, index) => (
            <Link
              href={link.href}
              key={index}
              className="px-4 py-2 text-zinc-500 hover:text-zinc-900 transition-all duration-300"
            >
              {link.name}
            </Link>
          ))}
          
          <SignedIn>
            <Link
              href={`/user/${userId}`}
              className="px-4 py-2 text-zinc-500 hover:text-zinc-900 transition-all duration-300"
            >
              My Stories
            </Link>
          </SignedIn>
          <SignedOut>
            <Link
              href="/sign-in"
              className="px-4 py-2 text-zinc-500 hover:text-zinc-900 transition-all duration-300"
            >
              My Stories
            </Link>
          </SignedOut>
        </div>
        <div className="ml-auto mr-4 flex items-center">
          <SignedIn>
            <UserButton />
          </SignedIn>
          <SignedOut>
            <Button variant="outline" asChild>
              <Link href="/sign-in">Sign In</Link>
            </Button>
          </SignedOut>
        </div>
      </div>
      <div className="w-full h-full">{children}</div>
    </div>
  );
}
