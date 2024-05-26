package net.apple70cents.assistant70cents;

import net.apple70cents.assistant70cents.config.ConfigStorage;
import net.apple70cents.assistant70cents.features.ASTNode;
import net.apple70cents.assistant70cents.features.Executor;
import net.apple70cents.assistant70cents.features.Parser;
import net.apple70cents.assistant70cents.utils.LoggerUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;

/**
 * @author 70CentsApple
 */
public class Assistant70Cents implements ModInitializer {

    public final static ConfigStorage DEFAULT_CONFIG = new ConfigStorage(true);
    public static ConfigStorage CONFIG;

    private boolean hasExecuted = false;

    @Override
    public void onInitialize() {
        LoggerUtils.init();

        if (!ConfigStorage.configFileExists()) {
            // if the config file doesn't exist, create a new one with the default settings.
            DEFAULT_CONFIG.save();
        }

        CONFIG = new ConfigStorage(false).withDefault(DEFAULT_CONFIG.getHashmap());

        LoggerUtils.info("Successfully started 70CentsAssistant");
        ASTNode ast = Parser.parseScript();
        LoggerUtils.info("Read script ast as follows:");
        System.out.println("ast = " + ast);
        ast.prettyPrint();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.currentScreen instanceof TitleScreen && !hasExecuted) {
                hasExecuted = true;
                Runnable runnable = () -> {
                    Executor executor = new Executor();
                    executor.await(3000);
                    executor.execute(ast);
                };
                Thread thread = new Thread(runnable, "70CentsAssistant Executor");
                thread.start();
            }
        });
    }
}
