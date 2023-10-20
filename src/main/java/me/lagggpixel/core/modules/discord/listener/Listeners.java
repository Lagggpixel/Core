package me.lagggpixel.core.modules.discord.listener;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.modules.discord.managers.DiscordManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class Listeners implements Listener {

    private final DiscordManager discordManager;

    public Listeners(DiscordManager discordManager) {
        Main.getPluginManager().registerEvents(this, Main.getInstance());
        this.discordManager = discordManager;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void PlayerJoinEvent(@NotNull PlayerJoinEvent event) {
        discordManager.sendEmbed(discordManager.MESSAGING_CHANNEL, discordManager.createJoinMessageEmbed(event));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void PlayerQuitEvent(@NotNull PlayerQuitEvent event) {
        discordManager.sendEmbed(discordManager.MESSAGING_CHANNEL, discordManager.createQuitMessageEmbed(event));
    }
}
