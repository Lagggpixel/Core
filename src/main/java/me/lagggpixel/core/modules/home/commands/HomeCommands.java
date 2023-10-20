package me.lagggpixel.core.modules.home.commands;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.CommandClass;
import me.lagggpixel.core.data.Lang;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.modules.Module;
import me.lagggpixel.core.modules.home.data.Home;
import me.lagggpixel.core.modules.home.managers.HomeManager;
import me.lagggpixel.core.utils.ChatUtils;
import me.lagggpixel.core.utils.CommandUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HomeCommands extends CommandClass implements Listener {
  
  Module module;
  HomeManager homeManager;
  
  public HomeCommands(Module module, HomeManager homeManager) {
    this.module = module;
    this.homeManager = homeManager;
  }
  
  @Override
  public String getCommandName() {
    return "home";
  }
  
  @Override
  public String getCommandDescription() {
    return null;
  }
  
  @Override
  public List<String> getCommandAliases() {
    return List.of("homes", "sethome", "delhome");
  }
  
  @Override
  public String getCommandPermission() {
    return CommandUtils.generateCommandBasePermission(module, this);
  }
  
  @Override
  public String getUsage() {
    return null;
  }
  
  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command cmd, @NotNull String label, String[] args) {
    if (!(commandSender instanceof Player player)) {
      commandSender.sendMessage(Lang.PLAYER_ONLY.toComponentWithPrefix(null));
      return true;
    }
    
    UUID playerUUID = player.getUniqueId();
    User user = Main.getUser(playerUUID);
    
    if (label.equals("homes") || label.equals("home")) {
      
      if (args.length == 0) {
        openHomesGUI(player, user);
        return true;
      }
      
      if (args[0].equalsIgnoreCase("list")) {
        openHomesGUI(player, user);
        return true;
      }
      
      if (args[0].equalsIgnoreCase("set")) {
        
        if (args.length != 2) {
          player.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix(null));
          return true;
        }
        
        handleCreateHome(player, user, args[1]);
        
        return true;
      }
      
      if (args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("delete")) {
        
        if (args.length != 2) {
          player.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix(null));
          return true;
        }
        
        
        handleDeleteHome(player, user, args[1]);
        return true;
      }
      
      player.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix(null));
      
      return true;
    }
    if (label.equals("sethome")) {
      if (args.length != 1) {
        player.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix(null));
        return true;
      }
      
      handleCreateHome(player, user, args[0]);
      
      return true;
    }
    if (label.equals("delhome")) {
      if (args.length != 1) {
        player.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix(null));
        return true;
      }
      handleDeleteHome(player, user, args[0]);
      return true;
    }
    
    player.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix(null));
    return true;
  }
  
  private void handleCreateHome(Player player, @NotNull User user, String homeName) {
    if (user.getHomes().containsKey(homeName)) {
      player.sendMessage(Lang.HOME_ALREADY_EXIST.toComponentWithPrefix(Map.of(
          "%home%", homeName
      )));
      return;
    }
    
    if (homeNameInvalid(homeName)) {
      player.sendMessage(Lang.HOME_NAME_INVALID.toComponentWithPrefix(null));
      return;
    }
    
    Home home = homeManager.createHomeObject(homeName, player.getLocation());
    homeManager.setHome(user, homeName, home);
    
    player.sendMessage(Lang.HOME_CREATED.toComponentWithPrefix(Map.of(
        "%home%", homeName
    )));
    
  }
  
  private void handleDeleteHome(Player player, @NotNull User user, String homeName) {
    if (!user.getHomes().containsKey(homeName)) {
      player.sendMessage(Lang.HOME_DOES_NOT_EXIST.toComponentWithPrefix(Map.of(
          "%home%", homeName
      )));
      return;
    }
    
    homeManager.deleteHome(user, homeName);
    
    player.sendMessage(Lang.HOME_DELETED.toComponentWithPrefix(Map.of(
        "%home%", homeName
    )));
  }
  
  private void openHomesGUI(@NotNull Player player, @NotNull User user) {
    Inventory gui = player.getServer().createInventory(null, 27, homeManager.HOME_GUI_NAME);
    
    for (Map.Entry<String, Home> homeEntry : user.getHomes().entrySet()) {
      ItemStack homeItem = createHomeItem(homeEntry.getKey());
      gui.addItem(homeItem);
    }
    
    player.openInventory(gui);
  }
  
  private @NotNull ItemStack createHomeItem(String homeName) {
    
    ItemStack item = new ItemStack(Material.PLAYER_HEAD);
    ItemMeta meta = item.getItemMeta();
    
    assert meta != null;
    meta.displayName(ChatUtils.stringToComponent(homeName));
    meta.getPersistentDataContainer().set(homeManager.HOME_ITEM_NAMESPACE_KEY, PersistentDataType.STRING, homeName);
    
    item.setItemMeta(meta);
    
    return item;
  }
  
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    return null;
  }
  
  private boolean homeNameInvalid(@NotNull String homeName) {
    int minLength = 3;
    int maxLength = 20;
    
    if (homeName.length() < minLength || homeName.length() > maxLength) {
      return true;
    }
    
    // Check if the home name contains special characters
    if (!homeName.matches("^[a-zA-Z0-9]+$")) {
      return true;
    }
    
    return false;
  }
  
}
