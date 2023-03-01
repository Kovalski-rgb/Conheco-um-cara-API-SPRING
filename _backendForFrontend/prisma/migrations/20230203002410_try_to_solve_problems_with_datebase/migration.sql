/*
  Warnings:

  - You are about to drop the column `active` on the `products` table. All the data in the column will be lost.
  - You are about to drop the column `createdAt` on the `products` table. All the data in the column will be lost.
  - You are about to drop the column `deletedAt` on the `products` table. All the data in the column will be lost.
  - You are about to drop the column `description` on the `products` table. All the data in the column will be lost.
  - You are about to drop the column `image` on the `products` table. All the data in the column will be lost.
  - You are about to drop the column `price` on the `products` table. All the data in the column will be lost.
  - You are about to drop the column `productType` on the `products` table. All the data in the column will be lost.
  - You are about to drop the column `userId` on the `products` table. All the data in the column will be lost.
  - You are about to drop the `communities` table. If the table is not empty, all the data it contains will be lost.
  - You are about to drop the `messages` table. If the table is not empty, all the data it contains will be lost.
  - You are about to drop the `moderators` table. If the table is not empty, all the data it contains will be lost.
  - You are about to drop the `posts` table. If the table is not empty, all the data it contains will be lost.
  - You are about to drop the `services` table. If the table is not empty, all the data it contains will be lost.
  - You are about to drop the `users` table. If the table is not empty, all the data it contains will be lost.

*/
-- DropForeignKey
ALTER TABLE `communities` DROP FOREIGN KEY `Communities_moderatorsId_fkey`;

-- DropForeignKey
ALTER TABLE `messages` DROP FOREIGN KEY `Messages_communitiesId_fkey`;

-- DropForeignKey
ALTER TABLE `messages` DROP FOREIGN KEY `Messages_fromUserId_fkey`;

-- DropForeignKey
ALTER TABLE `messages` DROP FOREIGN KEY `Messages_toUserId_fkey`;

-- DropForeignKey
ALTER TABLE `posts` DROP FOREIGN KEY `Posts_communitiesId_fkey`;

-- DropForeignKey
ALTER TABLE `posts` DROP FOREIGN KEY `Posts_productsId_fkey`;

-- DropForeignKey
ALTER TABLE `posts` DROP FOREIGN KEY `Posts_servicesId_fkey`;

-- DropForeignKey
ALTER TABLE `posts` DROP FOREIGN KEY `Posts_userId_fkey`;

-- DropForeignKey
ALTER TABLE `products` DROP FOREIGN KEY `Products_userId_fkey`;

-- DropForeignKey
ALTER TABLE `services` DROP FOREIGN KEY `Services_userId_fkey`;

-- DropForeignKey
ALTER TABLE `users` DROP FOREIGN KEY `Users_communitiesId_fkey`;

-- DropForeignKey
ALTER TABLE `users` DROP FOREIGN KEY `Users_moderatorsId_fkey`;

-- AlterTable
ALTER TABLE `products` DROP COLUMN `active`,
    DROP COLUMN `createdAt`,
    DROP COLUMN `deletedAt`,
    DROP COLUMN `description`,
    DROP COLUMN `image`,
    DROP COLUMN `price`,
    DROP COLUMN `productType`,
    DROP COLUMN `userId`;

-- DropTable
DROP TABLE `communities`;

-- DropTable
DROP TABLE `messages`;

-- DropTable
DROP TABLE `moderators`;

-- DropTable
DROP TABLE `posts`;

-- DropTable
DROP TABLE `services`;

-- DropTable
DROP TABLE `users`;
