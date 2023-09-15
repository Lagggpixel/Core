package me.lagggpixel.core.modules.inventory.commands;

import me.lagggpixel.core.data.CommandClass;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

// enderchest
// enderchest [player]
// enderchest open [player]
// enderchest clear
// enderchest clear [player] (-s)
// enderchest clone [player]
// enderchest clone [player] [target] (-s)
public class EnderchestCommands extends CommandClass {
    @Override
    public String getCommandName() {
        return null;
    }

    @Override
    public String getCommandDescription() {
        return null;
    }

    @Override
    public List<String> getCommandAliases() {
        return null;
    }

    @Override
    public String getCommandPermission() {
        return null;
    }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}
