package me.lagggpixel.core.modules.discord.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.lagggpixel.core.Main;
import me.lagggpixel.core.utils.DiscordUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class Listeners implements Listener {

    public Listeners() {
        Main.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void PlayerJoinEvent(@NotNull PlayerJoinEvent event) {
        DiscordUtils.sendEmbed(DiscordUtils.createJoinMessageEmbed(event));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void PlayerQuitEvent(@NotNull PlayerQuitEvent event) {
        DiscordUtils.sendEmbed(DiscordUtils.createQuitMessageEmbed(event));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void PlayerAsyncChatEvent(AsyncChatEvent event) {
        DiscordUtils.sendEmbed(DiscordUtils.createChatEmbed(event));
    }

}
