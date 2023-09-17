package me.lagggpixel.core.modules.skipnight.commands;

import me.lagggpixel.core.modules.skipnight.managers.SkipNightVoteManager;
import me.lagggpixel.core.modules.skipnight.objects.SkipNightVoteType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static org.bukkit.Bukkit.getLogger;

public class SkipNightCommand implements CommandExecutor {

    SkipNightVoteManager skipNightVoteManager;

    public SkipNightCommand(SkipNightVoteManager skipNightVoteManager) {
        this.skipNightVoteManager = skipNightVoteManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("yes")) {
                if (!(sender instanceof Player player)) {
                    getLogger().info("Vote not allowed from console.");
                }
                else {
                    this.skipNightVoteManager.addYes(player.getUniqueId(), SkipNightVoteType.NIGHT);
                }
            }
            else if (args[0].equalsIgnoreCase("no")) {
                if (!(sender instanceof Player player)) {
                    getLogger().info("Vote not allowed from console.");
                }
                else {
                    this.skipNightVoteManager.addNo(player.getUniqueId(), SkipNightVoteType.NIGHT);
                }
            }
        }
        else {
            if (!(sender instanceof Player player)) {
                getLogger().info("Vote can't be started from console.");
            }
            else {
                this.skipNightVoteManager.start(player, SkipNightVoteType.NIGHT);
            }
        }

        return true;
    }
}
