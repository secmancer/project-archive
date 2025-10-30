"use client";

import { useForm } from "react-hook-form";
import { Button } from "@/components/ui/button";
import React, { useState, useEffect } from "react";
import axios from "axios";
import { useAuth } from "@clerk/nextjs";
import { useRouter } from "next/navigation";

export default function Page() {
  const { userId, isSignedIn, isLoaded } = useAuth();
  const router = useRouter();
  const [bodyCharCount, setBodyCharCount] = useState(0);
  const [titleCharCount, setTitleCharCount] = useState(0);

  useEffect(() => {
    if (!isLoaded) return;
    if (!isSignedIn) {
      router.push("/sign-in");
      return;
    }
  }, [isLoaded, isSignedIn]);

  const {
    register,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm();

  const bodyValue = watch("body", "");
  useEffect(() => {
    setBodyCharCount(bodyValue.length);
  }, [bodyValue]);

  const titleValue = watch("title", "");
  useEffect(() => {
    setTitleCharCount(titleValue.length);
  }, [titleValue]);

  const updateDb = async function (data) {
    try {
      // Step 1: Create the story first
      const response1 = await axios.post("/api/story", {
        author_id: userId,
        title: data.title,
        theme: data.theme,
      });
  
      const storyId = response1.data.id;
  
      // Step 2: Create the root story node, linking it to the story
      const response2 = await axios.post("/api/storynode", {
        story_id: storyId,
        parent_id: null, // Root node has no parent
        title: data.title,
        body: data.body,
        writer_id: userId,
      });
  
      // Step 3: Update the story to set its `root_node_id`
      await axios.patch(`/api/story`, { id: storyId, root_node_id: response2.data.id });
   
      // Step 4: Redirect to the new story page
      router.push(`/story/${storyId}`);
    } catch (error) {
      console.error("Error redirecting to new story:", error);
    }
  };
  
  return (
    <div className="w-full h-full flex items-center justify-center">
      <form onSubmit={handleSubmit(updateDb)} className="flex flex-col gap-4">
        <input
          id="title"
          type="text"
          placeholder="Title"
          maxLength={50}
          {...register("title", { required: true })}
        />
        <p>{titleCharCount}/50 characters</p>
        {errors.title && <p className="text-red-500">Title is required</p>}

        <textarea
          id="body"
          type="text"
          rows="7"
          cols="50"
          placeholder="Body"
          maxLength={280}
          {...register("body", { required: true })}
        />
        <p>{bodyCharCount}/280 characters</p>
        {errors.body && <p className="text-red-500">Body is required</p>}

        <select {...register("theme", { required: true })}>
          <option value="">Select a theme</option>
          <option value="HORROR">Horror</option>
          <option value="COMEDY">Comedy</option>
          <option value="ACTION">Action</option>
          <option value="MYSTERY">Mystery</option>
          <option value="DRAMA">Drama</option>
          <option value="ROMANCE">Romance</option>
          <option value="FANTASY">Fantasy</option>
          <option value="SCIFI">Sci-Fi</option>
          <option value="ADVENTURE">Adventure</option>
        </select>
        {errors.theme && <p className="text-red-500">Theme is required</p>}

        <Button>Submit</Button>
      </form>
    </div>
  );
}
