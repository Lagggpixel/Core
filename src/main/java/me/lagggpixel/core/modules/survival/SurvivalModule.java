package me.lagggpixel.core.modules.survival;

import me.lagggpixel.core.interfaces.IModule;
import me.lagggpixel.core.modules.survival.commands.AnvilCommand;
import me.lagggpixel.core.modules.survival.commands.CraftCommand;
import me.lagggpixel.core.modules.survival.commands.EnchantingTableCommand;
import me.lagggpixel.core.modules.survival.commands.tpaCommands.TpaAcceptCommand;
import me.lagggpixel.core.modules.survival.commands.tpaCommands.TpaCancelCommand;
import me.lagggpixel.core.modules.survival.commands.tpaCommands.TpaCommand;
import me.lagggpixel.core.modules.survival.commands.tpaCommands.TpaDenyCommand;
import me.lagggpixel.core.modules.survival.handlers.SurvivalItemHandler;
import me.lagggpixel.core.modules.survival.handlers.TpaHandler;
import me.lagggpixel.core.modules.survival.listeners.InventoryClickListener;
import me.lagggpixel.core.modules.survival.listeners.SurvivalItemListeners;
import me.lagggpixel.core.modules.survival.listeners.WhitelistedListener;
import me.lagggpixel.core.utils.CommandUtils;
import org.jetbrains.annotations.NotNull;

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
  }

  private void registerInventoryCommands() {
    CommandUtils.registerCommand(new AnvilCommand(this));
    CommandUtils.registerCommand(new EnchantingTableCommand(this));
    CommandUtils.registerCommand(new CraftCommand(this));
  }

  private void registerTpaCommands() {
    CommandUtils.registerCommand(new TpaCommand(this, tpaHandler));
    CommandUtils.registerCommand(new TpaAcceptCommand(this, tpaHandler));
    CommandUtils.registerCommand(new TpaDenyCommand(this, tpaHandler));
    CommandUtils.registerCommand(new TpaCancelCommand(this, tpaHandler));
  }
}
