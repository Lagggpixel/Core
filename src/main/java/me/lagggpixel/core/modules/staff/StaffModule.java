package me.lagggpixel.core.modules.staff;

import me.lagggpixel.core.modules.Module;
import org.jetbrains.annotations.NotNull;

public class StaffModule extends Module {
    @NotNull
    @Override
    public String getId() {
        return "staff";
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
