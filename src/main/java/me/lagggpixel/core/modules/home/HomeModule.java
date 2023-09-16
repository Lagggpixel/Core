package me.lagggpixel.core.modules.home;

import me.lagggpixel.core.modules.Module;
import me.lagggpixel.core.modules.home.commands.HomeCommands;
import me.lagggpixel.core.modules.home.listeners.HomeGuiListeners;
import me.lagggpixel.core.modules.home.managers.HomeManager;
import me.lagggpixel.core.utils.CommandUtils;
import org.jetbrains.annotations.NotNull;

public class HomeModule extends Module {

    private HomeManager homeManager;

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
        CommandUtils.registerCommand(new HomeCommands(this, homeManager));
    }

    @Override
    public void registerListeners() {
        new HomeGuiListeners(homeManager);
    }
}
