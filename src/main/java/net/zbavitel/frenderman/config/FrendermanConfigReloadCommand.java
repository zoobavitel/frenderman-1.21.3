package net.zbavitel.frenderman.config;

import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;

public class FrendermanConfigReloadCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(CommandManager.literal("reloadFrendermanConfig")
                    .executes(context -> {
                        FrendermanConfig.load(); // Reload config
                        context.getSource().sendFeedback(() -> Text.of("Frenderman config reloaded!"), true);
                        return 1;
                    }));
        });
    }
}
