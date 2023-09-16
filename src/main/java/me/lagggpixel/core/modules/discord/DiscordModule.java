package me.lagggpixel.core.modules.discord;

import me.lagggpixel.core.modules.Module;
import me.lagggpixel.core.modules.discord.listener.Listeners;
import me.lagggpixel.core.modules.discord.managers.DiscordManager;
import org.jetbrains.annotations.NotNull;

public class DiscordModule extends Module {

    DiscordManager discordManager;

    @NotNull
    @Override
    public String getId() {
        return "discord";
    }

    @Override
    public void initialize() {
discordManager = new DiscordManager();
    }

    @Override
    public void registerCommands() {

    }

    @Override
    public void registerListeners() {
        new Listeners(discordManager);
    }
}
