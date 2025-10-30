"use client";

import { useAuth } from "@clerk/nextjs";
import { useEffect, useState } from "react";
import axios from "axios";
import Link from "next/link";

export default function FavoritesPage() {
  const { userId, isSignedIn, isLoaded } = useAuth();
  const [favorites, setFavorites] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!isLoaded) return;

    if (!isSignedIn) {
      alert("You need to be signed in to view your favorites.");
      return;
    }

    const fetchFavorites = async () => {
      try {
        const response = await axios.get(`/api/favorites?userId=${userId}`);
        setFavorites(response.data.favorites);
      } catch (error) {
        console.error("Error fetching favorites:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchFavorites();
  }, [isLoaded, isSignedIn, userId]);

  if (loading) {
    return (
      <div className="w-full h-full flex flex-col items-center justify-center">
        <p>Loading...</p>
      </div>
    );
  }

  if (favorites.length === 0) {
    return (
      <div className="w-full h-full flex flex-col items-center justify-center">
        <h1>No Favorites Found!</h1>
        <p>
          Try adding some favorites from Explore or Search to start building
          your list!
        </p>
      </div>
    );
  }

  return (
    <div className="w-full h-full flex flex-col items-center">
      <h1 className="text-2xl font-bold mb-4">Your Favorite Stories</h1>
      <ul className="w-full max-w-md">
        {favorites.map((favorite) => (
          <li key={favorite.story.id} className="border-b py-2">
            <Link
              href={`/story/${favorite.story.id}`}
              className="text-blue-500 hover:underline"
            >
              {favorite.story.title}
            </Link>
          </li>
        ))}
      </ul>
    </div>
  );
}
