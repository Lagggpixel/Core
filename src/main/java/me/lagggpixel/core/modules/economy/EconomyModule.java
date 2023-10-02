package me.lagggpixel.core.modules.economy;

import me.lagggpixel.core.modules.Module;
import org.jetbrains.annotations.NotNull;

public class EconomyModule extends Module {

    @NotNull
    @Override
    public String getId() {
        return "economy";
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void registerCommands() {

    }

    @Override
    public void registerListeners() {

    }
}
