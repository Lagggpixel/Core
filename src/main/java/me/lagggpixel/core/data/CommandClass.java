package me.lagggpixel.core.data;

import org.bukkit.command.TabExecutor;

import java.util.List;

public abstract class CommandClass implements TabExecutor {

    public abstract String getCommandName();
    public abstract String getCommandDescription();
    public abstract List<String> getCommandAliases();
    public abstract String getCommandPermission();
    public abstract String getUsage();

}
