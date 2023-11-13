package me.lagggpixel.core.modules.staff.commands;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.CommandClass;
import me.lagggpixel.core.data.Lang;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.modules.staff.StaffModule;
import me.lagggpixel.core.modules.staff.handlers.StaffModeHandler;
import me.lagggpixel.core.utils.CommandUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StaffCommand extends CommandClass {
  private final StaffModule staffModule;
  private final StaffModeHandler staffModeHandler = new StaffModeHandler();
  
  public StaffCommand(StaffModule staffModule) {
    this.staffModule = staffModule;
  }
  
  @Override
  public String getCommandName() {
    return "staff";
  }
  
  @Override
  public String getCommandDescription() {
    return null;
  }
  
  @Override
  public List<String> getCommandAliases() {
    return List.of("staff", "staffmode", "admin");
  }
  
  @Override
  public String getCommandPermission() {
    return CommandUtils.generateCommandBasePermission(staffModule, this);
  }
  
  @Override
  public String getUsage() {
    return null;
  }
  
  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command cmd, @NotNull String label, String[] args) {
    if (!(commandSender instanceof Player sender)) {
      // TODO - add console handling
      commandSender.sendMessage(Lang.PLAYER_ONLY.toComponentWithPrefix());
      return true;
    }
    if (args.length == 0) {
      User user = Main.getUser(sender.getUniqueId());
      boolean newStaffModeState = !user.isStaffMode();
      user.setStaffMode(newStaffModeState);
      
      if (newStaffModeState) {
        // Player entered staff mode
        sender.sendMessage(Lang.STAFF_MODE_ENABLED.toComponentWithPrefix());
        staffModeHandler.enterStaffMode(sender);
      } else {
        // Player exited staff mode
        sender.sendMessage(Lang.STAFF_MODE_DISABLED.toComponentWithPrefix());
        staffModeHandler.exitStaffMode(sender);
      }
      return true;
    }
    
    return true;
  }
  
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    // Tab completion logic (if needed)
    return null;
  }
}
