# Lineage
An app for users/authors everywhere to create and collaborate on intricate storylines.

CSC 307  - Introduction to Software Engineering Winter 2025- project - done in collaboration with others

Archive of the project after the completion of the class.

Written by (GitHub Usernames):
- XainLubin
- ahjeung
- Twicee
- NoahMoscovici
- secmancer

Instructions written for the project have been given below:

## Setup
- To setup, you need to have Docker installed on your machine.
- You need at least two environment files:
   - `.env`  for local development (doing database migrations, etc.)
   - `.env.docker` for running all three containers (prisma studio, app, database) on DOCKER
- This app uses Clerk, so you need to create an account, create a test organization, and generate the relevant keys.
- You can grab them [here](https://dashboard.clerk.com/last-active?path=api-keys) and place them into the relevant environment files.
- In the end, your environment files should look like this:

### `.env.docker`:
```env
DATABASE_URL="postgresql://postgres:prisma@postgres_db:5432/postgres?schema=public"
NEXT_PUBLIC_CLERK_PUBLISHABLE_KEY="key"
CLERK_SECRET_KEY="key"
NEXT_PUBLIC_CLERK_SIGN_IN_URL=/sign-in
```

### `.env`:
```env
DATABASE_URL="postgresql://postgres:prisma@localhost:5432/postgres?schema=public"
NEXT_PUBLIC_CLERK_PUBLISHABLE_KEY="key"
CLERK_SECRET_KEY="key"
NEXT_PUBLIC_CLERK_SIGN_IN_URL=/sign-in
```

- Next, install the necessary dependencies:
```sh
npm install
```

- Lastly, you need to generate the necessary Prisma client. You can achieve that by running:
```sh
npx prisma generate
```

- You can now build and run the app.

## Build Instructions
- This app fully runs on 3 Docker containers: one for the app, one for the database, and one for access into Prisma Studio if needed.
- To build, run this command in the root directory of the project:
```sh
docker compose -f docker-compose.yml build
```

## Run Instructions
- To run, use this command:
```sh
docker compose up -d
```
- If this is your first time running the application, then Prisma needs to be initially migrated.
- You can achieve that by running this command:
```sh
npx prisma migrate dev
```

## Usage
- You can view the app at `localhost:3000` in your browser.
- You can view Prisma Studio at `localhost:5555` in your browser.

## Workflow for Different Changes

### For frontend code changes:
```sh
docker compose up --build -d
```

### For database schema changes:
```sh
npx prisma migrate dev
```

### To stop containers:
```sh
docker compose down
```

### To fully reset (remove all data, images, containers):
```sh
docker compose down --rmi all --volumes --remove-orphans
docker compose -f docker-compose.yml build
docker compose up -d
npx prisma migrate dev
```

### To erase database data but keep everything else:
```sh
docker compose down --volumes
docker compose up -d
npx prisma migrate dev
