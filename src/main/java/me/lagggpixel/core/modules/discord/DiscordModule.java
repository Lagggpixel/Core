package me.lagggpixel.core.modules.discord;

import me.lagggpixel.core.modules.Module;
import me.lagggpixel.core.modules.discord.listener.Listeners;
import me.lagggpixel.core.modules.discord.managers.DiscordManager;
import me.lagggpixel.core.modules.discord.managers.NMSManager;
import org.jetbrains.annotations.NotNull;

public class DiscordModule extends Module {

    NMSManager nmsManager;
    public static DiscordManager discordManager;

    @NotNull
    @Override
    public String getId() {
        return "discord";
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void initialize() {
        nmsManager = new NMSManager();
        discordManager = new DiscordManager(nmsManager);
    }

    @Override
    public void registerCommands() {

    }

    @Override
    public void registerListeners() {
        new Listeners(discordManager);
    }
}
