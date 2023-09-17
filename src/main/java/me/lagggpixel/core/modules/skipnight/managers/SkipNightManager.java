package me.lagggpixel.core.modules.skipnight.managers;

import me.lagggpixel.core.modules.skipnight.objects.SkipNightVoteType;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

public class SkipNightManager implements Runnable {

    private final World world;
    private final Plugin plugin;
    private final SkipNightVoteType skipNightVoteType;

    public SkipNightManager(World world, Plugin plugin, SkipNightVoteType skipNightVoteType) {
        this.world = world;
        this.plugin = plugin;
        this.skipNightVoteType = skipNightVoteType;
    }

    @Override
    public void run() {
        world.setTime(world.getTime() + 80);
        if (skipNightVoteType == SkipNightVoteType.NIGHT && world.getTime() > 12516 && world.getTime() < 23900) plugin.getServer().getScheduler().runTaskLater(plugin, this, 1);
    }
}
