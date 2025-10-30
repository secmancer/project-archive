package io.github.secmancer.scuffedclient.module.render;

import com.lukflug.panelstudio.base.IToggleable;
import io.github.secmancer.scuffedclient.module.util.Module;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;

public class Xray extends Module {
    public static ArrayList<Block> blocks = new ArrayList<Block>();

    public Xray() {
        super("Xray", "Allows the player to see through the world to find ore.", () -> true, true);
        initXRay();
    }

    public IToggleable getToggle() { return this.isEnabled(); }

    public void initXRay() {
        blocks.add(Blocks.EMERALD_ORE);
        blocks.add(Blocks.EMERALD_BLOCK);
        blocks.add(Blocks.DIAMOND_ORE);
        blocks.add(Blocks.DIAMOND_BLOCK);
        blocks.add(Blocks.GOLD_ORE);
        blocks.add(Blocks.GOLD_BLOCK);
        blocks.add(Blocks.IRON_ORE);
        blocks.add(Blocks.IRON_BLOCK);
        blocks.add(Blocks.COAL_ORE);
        blocks.add(Blocks.COAL_BLOCK);
        blocks.add(Blocks.REDSTONE_BLOCK);
        blocks.add(Blocks.REDSTONE_ORE);
        blocks.add(Blocks.LAPIS_ORE);
        blocks.add(Blocks.LAPIS_BLOCK);
        blocks.add(Blocks.NETHER_GOLD_ORE);
        blocks.add(Blocks.ANCIENT_DEBRIS);
        blocks.add(Blocks.NETHER_QUARTZ_ORE);
        blocks.add(Blocks.MOSSY_COBBLESTONE);
        blocks.add(Blocks.COBBLESTONE);
        blocks.add(Blocks.STONE_BRICKS);
        blocks.add(Blocks.OAK_PLANKS);
        blocks.add(Blocks.DEEPSLATE_EMERALD_ORE);
        blocks.add(Blocks.DEEPSLATE_DIAMOND_ORE);
        blocks.add(Blocks.DEEPSLATE_GOLD_ORE);
        blocks.add(Blocks.DEEPSLATE_IRON_ORE);
        blocks.add(Blocks.DEEPSLATE_COAL_ORE);
    }

    public static boolean isXRayBlock(Block b) {
        return blocks.contains(b);
    }

    @Override
    public void onEnable(MinecraftClient client) {
        client.worldRenderer.reload();
    }

    @Override
    public void onDisable(MinecraftClient client) {
        client.worldRenderer.reload();
    }
}
