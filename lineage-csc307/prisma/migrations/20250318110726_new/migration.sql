/*
  Warnings:

  - Added the required column `theme` to the `Story` table without a default value. This is not possible if the table is not empty.

*/
-- CreateEnum
CREATE TYPE "Theme" AS ENUM ('HORROR', 'COMEDY', 'ACTION', 'MYSTERY', 'DRAMA', 'ROMANCE', 'FANTASY', 'SCIFI', 'ADVENTURE');

-- AlterTable
ALTER TABLE "Story" ADD COLUMN     "theme" "Theme" NOT NULL;
