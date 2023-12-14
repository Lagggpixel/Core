package me.lagggpixel.core.modules.bazaar;

import lombok.Getter;
import me.lagggpixel.core.Main;
import me.lagggpixel.core.interfaces.IModule;
import me.lagggpixel.core.modules.bazaar.commands.BazaarCommand;
import me.lagggpixel.core.modules.bazaar.impl.CoreBazaar;
import me.lagggpixel.core.modules.bazaar.interfaces.Bazaar;
import me.lagggpixel.core.modules.bazaar.interfaces.BazaarSubItem;
import me.lagggpixel.core.utils.ChatUtils;
import me.lagggpixel.core.utils.CommandUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class BazaarModule implements IModule {
  
  @Getter
  private static Bazaar bazaar;
  private BukkitRunnable autoSaveRunnable;
  
  @NotNull
  @Override
  public String getId() {
    return "bazaar";
  }
  
  @Override
  public boolean isEnabled() {
    return true;
  }
  
  @Override
  public void onEnable() {
    try {
      bazaar = new CoreBazaar();
    } catch (Bazaar.BazaarIOException | Bazaar.BazaarItemNotFoundException ex) {
      Main.log(Level.SEVERE, ("Failed to onEnable bazaar: " + ex.getMessage()));
    }
    autoSaveRunnable = new BukkitRunnable() {
      @Override
      public void run() {
        saveEscrow();
      }
    };
    autoSaveRunnable.runTaskTimerAsynchronously(Main.getInstance(), 6000L, 6000L);
  }
  
  @Override
  public void onDisable() {
  
  }
  
  @Override
  public void registerCommands() {
    CommandUtils.registerCommand(new BazaarCommand(this));
  }
  
  @Override
  public void registerListeners() {
  
  }
  
  private void saveEscrow() {
    // Main.log(Level.INFO, "Bazaar escrow data saved");
  }
  
  public static void checkBuyOrderAvailability(BazaarSubItem item, Player opener) {
    if (item.getLowestSellPrice() < 0.0) {
      opener.sendMessage(ChatUtils.stringToComponentCC("&cNo sell offers!"));
      return;
    }
  }
}
