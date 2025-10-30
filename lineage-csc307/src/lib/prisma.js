import { PrismaClient } from '@prisma/client'

const prisma = new PrismaClient()
const globalForPrisma = global;
if (process.env.NODE_ENV !== 'production'){
    globalForPrisma.prisma = prisma
}
export default prisma
