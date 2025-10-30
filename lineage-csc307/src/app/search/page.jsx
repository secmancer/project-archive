"use client";
import { useState } from "react";
import { useRouter } from "next/navigation";

export default function SearchPage() {
  const [query, setQuery] = useState("");
  const [results, setResults] = useState([]);
  const router = useRouter();

  const handleSearch = async (e) => {
    e.preventDefault();
    const response = await fetch(`/api/story?query=${query}`);
    const data = await response.json();
    setResults(data);
  };

  return (
    <div className="p-4">
      <h1 className="text-2xl font-bold mb-4">Search for a Story</h1>
      <form onSubmit={handleSearch} className="flex gap-2 mb-4">
        <input
          type="text"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          className="border p-2 w-full rounded"
          placeholder="Enter story title..."
        />
        <button
          type="submit"
          className="bg-blue-500 text-white px-4 py-2 rounded"
        >
          Search
        </button>
      </form>
      <ul className="list-none">
        {results.map((story) => (
          <li
            key={story.id}
            className="p-2 border-b flex justify-between items-center hover:bg-gray-100"
          >
            <a
              onClick={() => router.push(`/story/${story.id}`)}
              className="cursor-pointer"
            >
              {story.title}
            </a>
          </li>
        ))}
      </ul>
    </div>
  );
}