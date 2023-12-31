package me.lagggpixel.core.data;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.commands.Tpa;
import me.lagggpixel.core.enums.Lang;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class TpaRequest {
  
  private final Player requester;
  private final Player target;
  
  private final long timeStarted;
  
  public TpaRequest(Player requester, Player target) {
    this.requester = requester;
    this.target = target;
    
    this.timeStarted = System.currentTimeMillis();
    
    requester.sendMessage(Lang.TPA_REQUEST_SENT.toComponentWithPrefix(Map.of("%player%", target.getName())));
    target.sendMessage(Lang.TPA_REQUEST_RECEIVED.toComponentWithPrefix(Map.of("%player%", requester.getName())));
    startCountDown();
    
    Tpa.getTpaRequestMap().put(requester, this);
  }
  
  private void startCountDown() {
    BukkitRunnable runnable = new BukkitRunnable() {
      @Override
      public void run() {
        long currentTime = System.currentTimeMillis();
        long timeElapsed = currentTime - timeStarted;
        if (!requester.isOnline()) {
          target.sendMessage(Lang.TPA_REQUEST_CANCELLED_RECEIVER.toComponentWithPrefix(Map.of("%player%", requester.getName())));
          this.cancelRequest();
          return;
        }
        
        if (!target.isOnline()) {
          requester.sendMessage(Lang.TPA_REQUEST_TIMEOUT_REQUESTER.toComponentWithPrefix(Map.of("%player%", target.getName())));
          this.cancelRequest();
          return;
        }
        
        if (timeElapsed >= 60 * 1000) {
          requester.sendMessage(Lang.TPA_REQUEST_TIMEOUT_REQUESTER.toComponentWithPrefix(Map.of("%player%", target.getName())));
          target.sendMessage(Lang.TPA_REQUEST_TIMEOUT_RECEIVER.toComponentWithPrefix(Map.of("%player%", requester.getName())));
          this.cancelRequest();
        }
      }
      
      private void cancelRequest() {
        Tpa.getTpaRequestMap().remove(requester);
        cancel();
      }
    };
    runnable.runTaskTimerAsynchronously(Main.getInstance(), 0L, 20L);
  }
  
  
  
  public void cancelTpa() {
    requester.sendMessage(Lang.TPA_REQUEST_CANCELLED_REQUESTER.toComponentWithPrefix(Map.of("%player%", target.getName())));
    target.sendMessage(Lang.TPA_REQUEST_CANCELLED_RECEIVER.toComponentWithPrefix(Map.of("%player%", requester.getName())));
  }
  
}
