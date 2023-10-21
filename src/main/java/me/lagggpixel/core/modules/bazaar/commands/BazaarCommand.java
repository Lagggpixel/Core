package me.lagggpixel.core.modules.bazaar.commands;

import me.lagggpixel.core.data.CommandClass;
import me.lagggpixel.core.data.Lang;
import me.lagggpixel.core.modules.bazaar.BazaarModule;
import me.lagggpixel.core.modules.bazaar.gui.BazaarCategoryBazaarGui;
import me.lagggpixel.core.utils.ChatUtils;
import me.lagggpixel.core.utils.CommandUtils;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BazaarCommand extends CommandClass {
  
  BazaarModule bazaarModule;
  ;
  
  public BazaarCommand(BazaarModule bazaarModule) {
    this.bazaarModule = bazaarModule;
  }
  
  @Override
  public String getCommandName() {
    return "bazaar";
  }
  
  @Override
  public String getCommandDescription() {
    return null;
  }
  
  @Override
  public List<String> getCommandAliases() {
    return List.of("bazaar", "bz");
  }
  
  @Override
  public String getCommandPermission() {
    return CommandUtils.generateCommandBasePermission(bazaarModule, this);
  }
  
  @Override
  public String getUsage() {
    return null;
  }
  
  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    
    if (!(commandSender instanceof Player sender)) {
      commandSender.sendMessage(Lang.PLAYER_ONLY.toComponentWithPrefix(null));
      return true;
    }
    
    new BazaarCategoryBazaarGui(sender, BazaarModule.getBazaar().getCategories().stream().filter(category -> category.getName().equals(ChatUtils.stringToComponent("Farming").color(TextColor.fromHexString("#FFFF00")))).findFirst().get(), false).show(sender);
    return true;
  }
  
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    return null;
  }
}
