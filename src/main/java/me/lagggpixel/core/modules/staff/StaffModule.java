package me.lagggpixel.core.modules.staff;

import me.lagggpixel.core.modules.Module;
import me.lagggpixel.core.modules.staff.commands.GamemodeCommands;
import me.lagggpixel.core.modules.staff.commands.StaffCommand;
import me.lagggpixel.core.modules.staff.commands.VanishCommand;
import me.lagggpixel.core.utils.CommandUtils;
import org.jetbrains.annotations.NotNull;

public class StaffModule extends Module {
    @NotNull
    @Override
    public String getId() {
        return "staff";
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void onEnable() {
        CommandUtils.registerCommand(new GamemodeCommands(this));
        CommandUtils.registerCommand(new StaffCommand(this));
        CommandUtils.registerCommand(new VanishCommand(this));
    }
    
    @Override
    public void onDisable() {
    
    }
    
    @Override
    public void registerCommands() {

    }

    @Override
    public void registerListeners() {

    }
}
