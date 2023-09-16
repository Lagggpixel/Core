package me.lagggpixel.core.modules.discord;

import me.lagggpixel.core.modules.Module;
import me.lagggpixel.core.modules.discord.listener.Listeners;
import org.jetbrains.annotations.NotNull;

public class DiscordModule extends Module {
    @NotNull
    @Override
    public String getId() {
        return "discord";
    }

    @Override
    public void initialize() {

    }

    @Override
    public void registerCommands() {

    }

    @Override
    public void registerListeners() {
        new Listeners();
    }
}
