package net.apple70cents.assistant70cents.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;

public class ConnectUtils {
    public static void connect(String ip) {
        MinecraftClient mc = MinecraftClient.getInstance();
        //#if MC>=12000
        ServerInfo entry = new ServerInfo(ip, ip, ServerInfo.ServerType.OTHER);
        //#else
        //$$ ServerInfo entry = new ServerInfo(ip, ip, false);
        //#endif

        //#if MC>12004
        ConnectScreen.connect(mc.currentScreen, mc, ServerAddress.parse(entry.address), entry, false, null);
        //#elseif MC>=12000
        //$$ ConnectScreen.connect(mc.currentScreen, mc, ServerAddress.parse(entry.address), entry, false);
        //#elseif MC>=11700
        //$$ ConnectScreen.connect(mc.currentScreen, mc, ServerAddress.parse(entry.address), entry);
        //#else
        //$$ mc.openScreen(new ConnectScreen(mc.currentScreen, mc, entry));
        //#endif
    }
}
