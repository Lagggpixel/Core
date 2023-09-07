package me.lagggpixel.core.modules.homes;

import me.lagggpixel.core.modules.Module;
import me.lagggpixel.core.modules.homes.commands.HomeCommands;
import me.lagggpixel.core.modules.homes.listeners.HomeGuiListeners;
import me.lagggpixel.core.modules.homes.managers.HomeManager;
import me.lagggpixel.core.utils.CommandUtils;
import org.jetbrains.annotations.NotNull;

public class HomeModule extends Module {

    HomeManager homeManager;

    @Override
    public @NotNull String getId() {
        return "home";
    }

    @Override
    public void initialize() {
        homeManager = new HomeManager();
    }

    @Override
    public void registerCommands() {
        CommandUtils.registerCommand(new HomeCommands(homeManager));
    }

    @Override
    public void registerListeners() {
        new HomeGuiListeners(homeManager);
    }
}
