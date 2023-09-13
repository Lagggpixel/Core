package me.lagggpixel.core.modules.rtp.commands;

import me.lagggpixel.core.data.CommandClass;
import me.lagggpixel.core.data.Lang;
import me.lagggpixel.core.modules.rtp.managers.RtpManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RtpCommand extends CommandClass {
    @Override
    public String getCommandName() {
        return "rtp";
    }

    @Override
    public String getCommandDescription() {
        return null;
    }

    @Override
    public List<String> getCommandAliases() {
        return List.of("randomteleport");
    }

    @Override
    public String getCommandPermission() {
        return null;
    }

    @Override
    public String getUsage() {
        return null;
    }

    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(commandSender instanceof Player sender)) {
            commandSender.sendMessage(Lang.PLAYER_ONLY.toComponentWithPrefix(null));
            return true;
        }
        if (args.length != 0) {
            sender.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix(null));
            return true;
        }
        RtpManager.teleportRandomly(sender);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}
