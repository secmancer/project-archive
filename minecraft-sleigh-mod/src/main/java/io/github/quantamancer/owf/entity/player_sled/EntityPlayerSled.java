package io.github.quantamancer.owf.entity.player_sled;

import io.github.quantamancer.owf.entity.ModEntities;
import io.github.quantamancer.owf.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.EatGrassGoal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

public class EntityPlayerSled extends HorseEntity implements Inventory, NamedScreenHandlerFactory {

    private DefaultedList<ItemStack> inventory;
    private static final TrackedData<Integer> SLED_TYPE;

    public EntityPlayerSled(EntityType<? extends HorseEntity> entityType, World world) {
        super(entityType, world);
        this.inventory = DefaultedList.ofSize(size(), ItemStack.EMPTY);
    }

    public static EntityPlayerSled spawn (World world, double x, double y, double z, float yaw) {
        EntityPlayerSled sled = new EntityPlayerSled(ModEntities.SLED, world);
        sled.setPosition(x, y ,z);
        sled.setVelocity(Vec3d.ZERO);
        sled.prevX = x;
        sled.prevY = y;
        sled.prevZ = z;
        sled.setYaw(yaw);
        return sled;
    }

    @Override
    public void openInventory(PlayerEntity player) {
        if (!this.world.isClient && (!this.hasPassengers() || this.hasPassenger(player)) && this.isTame()) {
            player.openHandledScreen(this);
        }
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new EatGrassGoal(this));
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SLED_TYPE, SledType.OAK.ordinal());
    }

    @Override
    public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {
        ItemStack currStack = player.getStackInHand(hand);
        if(!currStack.isEmpty()) {
            ActionResult result = currStack.useOnEntity(player, this, hand);
            if (result.isAccepted())
                return result;
        } else if (currStack.isEmpty() && !player.isSneaking()) { return ActionResult.success(player.startRiding(this)); }
        return super.interact(player, hand);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        return ActionResult.PASS;
    }

    @Override
    protected void onBlockCollision(BlockState state) {
        super.onBlockCollision(state);
        BlockState currBlock = this.getLandingBlockState();
        if (this.getVelocity() != Vec3d.ZERO) {
            if (currBlock.getBlock() == Blocks.SNOW || currBlock.getBlock() == Blocks.SNOW_BLOCK || currBlock.getBlock() == Blocks.POWDER_SNOW) {
                for (int i = 0; i < 2; ++i)
                    this.world.addParticle(ParticleTypes.SNOWFLAKE, this.getParticleX(0.5D), this.getRandomBodyY(), this.getParticleZ(0.5D), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    public Item asItem() {
        switch(this.getSledType()) {
            case OAK:
            default:
                return ModItems.OAK_SLED_SPAWNER;
            case SPRUCE:
                return ModItems.SPRUCE_SLED_SPAWNER;
            case BIRCH:
                return ModItems.BIRCH_SLED_SPAWNER;
            case JUNGLE:
                return ModItems.JUNGLE_SLED_SPAWNER;
            case ACACIA:
                return ModItems.ACACIA_SLED_SPAWNER;
            case DARK_OAK:
                return ModItems.DARK_OAK_SLED_SPAWNER;
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putString("Type", this.getSledType().getName());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.contains("Type", 8)) {
            this.setSledType(EntityPlayerSled.SledType.getType(nbt.getString("Type")));
        }
    }

    @Override
    public boolean hasPlayerRider() {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ITEM_SHIELD_BLOCK;
    }

    @Override
    protected void playJumpSound() {
        this.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.4F, 1.0F);
    }

    @Override
    protected void playWalkSound(BlockSoundGroup group) {
        this.playSound(SoundEvents.ENTITY_WOLF_STEP, 0.4F, 1.0F);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_WOLF_STEP, 0.4F, 1.0F);
    }

    @Override
    protected boolean receiveFood(PlayerEntity player, ItemStack item) {
        return false;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ITEM_BREAK;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canJump() {
        return false;
    }

    @Override
    public double getMountedHeightOffset() {
        return 0.1D;
    }

    @Override
    public boolean canBreedWith(AnimalEntity other) {
        return false;
    }

    @Override
    public boolean isTame() {
        return true;
    }

    @Override
    public boolean isSaddled() {
        return true;
    }

    @Override
    public boolean hasArmorSlot() {
        return false;
    }

    @Override
    public boolean hasArmorInSlot() {
        return false;
    }


    @Override
    public int size() {
        return 9*6;
    }

    @Override
    public boolean isEmpty() {
        Iterator<ItemStack> itr = this.inventory.iterator();
        ItemStack itemStack;
        do {
            if (!itr.hasNext()) {
                return true;
            }
            itemStack = itr.next();
        } while (itemStack.isEmpty());
        return false;
    }

    @Override
    public ItemStack getStack(int slot) {
        return this.inventory.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(this.inventory, slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack itemStack = this.inventory.get(slot);
        if (itemStack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            this.inventory.set(slot, ItemStack.EMPTY);
            return itemStack;
        }
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.inventory.set(slot, stack);
        if (!stack.isEmpty() && stack.getCount() > this.getMaxCountPerStack()) {
            stack.setCount(this.getMaxCountPerStack());
        }
    }

    @Override
    public void markDirty() {}

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return GenericContainerScreenHandler.createGeneric9x6(syncId, inv, this);
    }

    @Override
    public void clear() {}

    @Override
    protected void drop(DamageSource source) {
        super.drop(source);
        if (this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
            for (ItemStack itemStack : this.inventory) {
                dropStack(itemStack);
            }
        }
    }

    public void setSledType(EntityPlayerSled.SledType type) { this.dataTracker.set(SLED_TYPE, type.ordinal()); }

    public EntityPlayerSled.SledType getSledType() { return EntityPlayerSled.SledType.getType((Integer)this.dataTracker.get(SLED_TYPE)); }

    static {
        SLED_TYPE = DataTracker.registerData(EntityPlayerSled.class, TrackedDataHandlerRegistry.INTEGER);
    }

    public enum SledType {
        OAK(Blocks.OAK_PLANKS, "oak"),
        SPRUCE(Blocks.SPRUCE_PLANKS, "spruce"),
        BIRCH(Blocks.BIRCH_PLANKS, "birch"),
        JUNGLE(Blocks.JUNGLE_PLANKS, "jungle"),
        ACACIA(Blocks.ACACIA_PLANKS, "acacia"),
        DARK_OAK(Blocks.DARK_OAK_PLANKS, "dark_oak");

        private final String name;
        private final Block baseBlock;

        private SledType(Block baseBlock, String name) {
            this.name = name;
            this.baseBlock = baseBlock;
        }

        public String getName() {
            return this.name;
        }

        public Block getBaseBlock() {
            return this.baseBlock;
        }

        public String toString() {
            return this.name;
        }

        public static EntityPlayerSled.SledType getType(int type) {
           EntityPlayerSled.SledType[] types = values();
            if (type < 0 || type >= types.length) {
                type = 0;
            }
            return types[type];
        }

        public static EntityPlayerSled.SledType getType(String name) {
            EntityPlayerSled.SledType[] types = values();

            for(int i = 0; i < types.length; ++i) {
                if (types[i].getName().equals(name)) {
                    return types[i];
                }
            }
            return types[0];
        }

    }
}
