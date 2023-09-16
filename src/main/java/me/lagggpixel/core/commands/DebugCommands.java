package me.lagggpixel.core.commands;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.CommandClass;
import me.lagggpixel.core.data.UserUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DebugCommands extends CommandClass {
    @Override
    public String getCommandName() {
        return "debug";
    }

    @Override
    public String getCommandDescription() {
        return null;
    }

    @Override
    public List<String> getCommandAliases() {
        return List.of("savedata");
    }

    @Override
    public String getCommandPermission() {
        return "core.debug";
    }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (s.equals("savedata")) {
            UserUtils.saveData(Main.getUserData());
            return true;
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}
