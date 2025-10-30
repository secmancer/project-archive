package io.github.secmancer.scuffedclient.module.gui;

import com.lukflug.panelstudio.base.IToggleable;
import com.lukflug.panelstudio.component.IFixedComponent;
import com.lukflug.panelstudio.hud.HUDList;
import com.lukflug.panelstudio.hud.ListComponent;
import com.lukflug.panelstudio.mc20.MinecraftGUI;
import io.github.secmancer.scuffedclient.gui.setting.BooleanSetting;
import io.github.secmancer.scuffedclient.gui.setting.ColorSetting;
import io.github.secmancer.scuffedclient.gui.setting.StringSetting;
import io.github.secmancer.scuffedclient.module.util.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.util.Objects;

public class StatsModule extends Module
{
    private static StatsModule instance;

    private static final ColorSetting color = new ColorSetting("Text Color","color","The color of the displayed text.",()->true,false,true,new Color(255, 255, 255),true);
    private static final BooleanSetting sortUp=new BooleanSetting("Sort Up","sortUp","Whether to align the text from the bottom up.",()->true,false);
    private static final BooleanSetting sortRight=new BooleanSetting("Sort Right","sortRight","Whether to align the text from right to left.",()->true,false);
    private static final StringSetting line1=new StringSetting("First Line","line1","The first line of text.",()->true," ");
    private static final StringSetting line2=new StringSetting("Second Line","line2","The second line of text.",()->true," ");
    private static final StringSetting line3=new StringSetting("Third Line","line3","The third line of text.",()->true," ");

    private static final StringSetting line4=new StringSetting("Fourth Line","line4","The fourth line of text.",()->true," ");

    public StatsModule() {
        super("Stats", "Displays the coordinates, FPS, and the ping to the player", () -> true, true);
        instance = this;
        settings.add(color);
        settings.add(sortUp);
        settings.add(sortRight);
        settings.add(line1);
        settings.add(line2);
        settings.add(line3);
        settings.add(line4);
    }

    public static IFixedComponent getComponent(MinecraftGUI.GUIInterface inter) {
        return new ListComponent(()->"Stats",new Point(90,10),"stats",new HUDList() {
            @Override
            public int getSize() {
                return 4;
            }

            @Override
            public String getItem(int index) {
                if (index==0) return line1.getValue();
                else if (index==1) return line2.getValue();
                else if (index==2) return line3.getValue();
                else return line4.getValue();
            }

            @Override
            public Color getItemColor(int index) {
                return color.getValue();
            }

            @Override
            public boolean sortUp() {
                return sortUp.getValue();
            }

            @Override
            public boolean sortRight() {
                return sortRight.getValue();
            }
        },9,2);
    }

    @Override
    public void tick(MinecraftClient client) {
        if (client.player != null) {
            line1.setValue("FPS: " + client.getCurrentFps());

            assert client.world != null;
            boolean isNether = client.world.getRegistryKey().getValue().getPath().contains("nether");
            BlockPos pos = client.player.getBlockPos();
            BlockPos pos2 = isNether ? BlockPos.ofFloored(client.player.getPos().multiply(8, 1, 8)) : BlockPos.ofFloored(client.player.getPos().multiply(0.125, 1, 0.125));
            line2.setValue("Coords (Raw): " + "[" + pos.getX() + " " + pos.getY() + " " + pos.getZ() + "]");

            if (isNether) {
                line3.setValue("Coords (Nether): " + "[" + pos2.getX() + " " + pos2.getY() + " " + pos2.getZ() + "]");
            } else {
                line3.setValue("Coords (Overworld): " + "[" + pos2.getX() + " " + pos2.getY() + " " + pos2.getZ() + "]");
            }

            line4.setValue("Ping: " + String.valueOf(Objects.requireNonNull(client.player.networkHandler.getPlayerListEntry(client.player.getGameProfile().getId())).getLatency()));
        }
    }

    public static IToggleable getToggle() {
        return instance.isEnabled();
    }
}
