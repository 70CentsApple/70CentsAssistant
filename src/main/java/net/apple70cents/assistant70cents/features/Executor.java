package net.apple70cents.assistant70cents.features;

import net.apple70cents.assistant70cents.utils.ConnectUtils;
import net.apple70cents.assistant70cents.utils.LoggerUtils;
import net.apple70cents.assistant70cents.utils.MessageUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.MessageScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.realms.gui.screen.RealmsMainScreen;

public class Executor {
    public void execute(ASTNode node) {
        if (node == null) {
            return;
        }
        for (ASTNode child : node.children) {
            executeCommand(child);
        }
        LoggerUtils.info("Executor is over!");
    }

    private void executeCommand(ASTNode node) {
        LoggerUtils.info("Executing: `" + node.command + "` with args: " + node.arguments);
        MinecraftClient mc = MinecraftClient.getInstance();
        switch (node.command) {
            case "enter_server":
                // to call this on the render thread
                mc.execute(() -> {
                    ConnectUtils.connect(node.arguments.get(0));
                });
                break;
            case "send_message":
                MessageUtils.sendToPublicChat(node.arguments.get(0));
                break;
            case "exit_server":
                // to call this on the render thread
                mc.execute(() -> {
                    if (mc.world != null) {
                        mc.world.disconnect();
                    }
                    mc.disconnect();
                    mc.setScreen(new TitleScreen());
                });
                break;
            case "exit_game":
                // to call this on the render thread
                mc.execute(mc::stop);
                break;
            case "await":
                await(Integer.parseInt(node.arguments.get(0)));
                break;
            default:
                throw new IllegalArgumentException("Unknown command: " + node.command);
        }
    }

    public void await(long millis) {
        try {
            LoggerUtils.info("Awaiting for " + millis + "ms");
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Await interrupted");
        }
    }
}
