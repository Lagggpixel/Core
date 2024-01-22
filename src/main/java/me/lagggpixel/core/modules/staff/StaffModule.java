/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.staff;

import me.lagggpixel.core.interfaces.IModule;
import me.lagggpixel.core.modules.staff.commands.GamemodeCommands;
import me.lagggpixel.core.modules.staff.commands.StaffCommand;
import me.lagggpixel.core.modules.staff.commands.SudoCommand;
import me.lagggpixel.core.modules.staff.commands.VanishCommand;
import me.lagggpixel.core.utils.CommandUtils;
import org.jetbrains.annotations.NotNull;

/**
 *  @author    Lagggpixel
 *  </p>
 *  Created on January 22, 2024
 */
public class StaffModule implements IModule {
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
        CommandUtils.registerCommand(new SudoCommand(this));
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
