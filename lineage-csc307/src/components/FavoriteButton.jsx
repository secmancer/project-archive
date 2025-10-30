"use client";

import { useState, useEffect } from "react";
import { useAuth } from "@clerk/nextjs";

export default function FavoriteButton({ storyId }) {
  const { userId, isSignedIn } = useAuth();
  const [favorite, setFavorite] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const checkFavoriteStatus = async () => {
      if (!isSignedIn) return;

      try {
        const response = await fetch(`/api/favorites?userId=${userId}`);
        const data = await response.json();
        const isFavorited = data.favorites.some(
          (fav) => fav.story.id === storyId,
        );
        setFavorite(isFavorited);
      } catch (error) {
        console.error("Error checking favorite status:", error);
      } finally {
        setLoading(false);
      }
    };

    checkFavoriteStatus();
  }, [userId, isSignedIn, storyId]);

  const handleFavorite = async () => {
    if (!isSignedIn) {
      alert("You need to be signed in to favorite a story.");
      return;
    }

    try {
      const response = await fetch("/api/favorites", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ userId, storyId }),
      });

      if (response.ok) {
        const result = await response.json();
        setFavorite(!favorite);
        alert(result.message);
      } else {
        console.error("Error toggling favorite status:", await response.json());
      }
    } catch (error) {
      console.error("Error toggling favorite status:", error);
    }
  };

  return (
    <button
      onClick={handleFavorite}
      className={`absolute top-2 right-2 p-2 rounded-full ${favorite ? "bg-yellow-400" : "bg-yellow-300 hover:bg-yellow-400"}`}
      title={favorite ? "Remove from Favorites" : "Add to Favorites"}
      disabled={loading}
    >
      ⭐
    </button>
  );
}
