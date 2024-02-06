/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.survival;

import me.lagggpixel.core.interfaces.IModule;
import me.lagggpixel.core.modules.survival.commands.AnvilCommand;
import me.lagggpixel.core.modules.survival.commands.CraftCommand;
import me.lagggpixel.core.modules.survival.commands.EnchantingTableCommand;
import me.lagggpixel.core.modules.survival.commands.WorldTpCommand;
import me.lagggpixel.core.modules.survival.commands.tpaCommands.TpaAcceptCommand;
import me.lagggpixel.core.modules.survival.commands.tpaCommands.TpaCancelCommand;
import me.lagggpixel.core.modules.survival.commands.tpaCommands.TpaCommand;
import me.lagggpixel.core.modules.survival.commands.tpaCommands.TpaDenyCommand;
import me.lagggpixel.core.modules.survival.handlers.SurvivalItemHandler;
import me.lagggpixel.core.modules.survival.handlers.TpaHandler;
import me.lagggpixel.core.modules.survival.listeners.InventoryClickListener;
import me.lagggpixel.core.modules.survival.listeners.SurvivalItemListeners;
import me.lagggpixel.core.modules.survival.listeners.WhitelistedListener;
import me.lagggpixel.core.modules.survival.listeners.WorldDataListeners;
import me.lagggpixel.core.utils.CommandUtils;
import org.jetbrains.annotations.NotNull;

/**
 * @author Lagggpixel
 * @since January 22, 2024
 */
public class SurvivalModule implements IModule {
  private TpaHandler tpaHandler;
  private SurvivalItemHandler survivalItemHandler;

  @NotNull
  @Override
  public String getId() {
    return "survival";
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public void onEnable() {
    tpaHandler = new TpaHandler();
    survivalItemHandler = new SurvivalItemHandler();
  }

  @Override
  public void onDisable() {

  }

  @Override
  public void registerCommands() {
    registerTpaCommands();
    registerInventoryCommands();
  }

  @Override
  public void registerListeners() {
    new SurvivalItemListeners(survivalItemHandler);
    new InventoryClickListener();
    new WhitelistedListener();
    new WorldDataListeners();
  }

  private void registerInventoryCommands() {
    CommandUtils.registerCommand(new AnvilCommand(this));
    CommandUtils.registerCommand(new EnchantingTableCommand(this));
    CommandUtils.registerCommand(new CraftCommand(this));
    CommandUtils.registerCommand(new WorldTpCommand(this));
  }

  private void registerTpaCommands() {
    CommandUtils.registerCommand(new TpaCommand(this, tpaHandler));
    CommandUtils.registerCommand(new TpaAcceptCommand(this, tpaHandler));
    CommandUtils.registerCommand(new TpaDenyCommand(this, tpaHandler));
    CommandUtils.registerCommand(new TpaCancelCommand(this, tpaHandler));
  }
}
