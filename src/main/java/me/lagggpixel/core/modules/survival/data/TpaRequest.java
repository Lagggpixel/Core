/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.survival.data;

import lombok.Getter;
import me.lagggpixel.core.Main;
import me.lagggpixel.core.modules.survival.handlers.TpaHandler;
import me.lagggpixel.core.enums.Lang;
import me.lagggpixel.core.utils.TeleportUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

@Getter
/**
 *  @author    Lagggpixel
 *  </p>
 *  Created on January 22, 2024
 */
public class TpaRequest {
  
  private final Player requester;
  private final Player target;
  
  private final long timeStarted;
  
  private BukkitRunnable runnable;
  
  private final TpaHandler tpaHandler;
  
  public TpaRequest(Player requester, Player target, TpaHandler tpaHandler) {
    this.requester = requester;
    this.target = target;
    
    this.tpaHandler = tpaHandler;
    
    this.timeStarted = System.currentTimeMillis();
    
    requester.sendMessage(Lang.TPA_REQUEST_SENT.toComponentWithPrefix(Map.of("%player%", target.getName())));
    target.sendMessage(Lang.TPA_REQUEST_RECEIVED.toComponentWithPrefix(Map.of("%player%", requester.getName())));
    startCountDown();
    
    tpaHandler.getTpaRequestMap().put(requester, this);
  }
  
  private void startCountDown() {
    runnable = new BukkitRunnable() {
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
      
      public void cancelRequest() {
        tpaHandler.getTpaRequestMap().remove(requester);
        cancel();
      }
    };
    runnable.runTaskTimerAsynchronously(Main.getInstance(), 0L, 20L);
  }
  
  
  
  public void cancelTpa() {
    requester.sendMessage(Lang.TPA_REQUEST_CANCELLED_REQUESTER.toComponentWithPrefix(Map.of("%player%", target.getName())));
    target.sendMessage(Lang.TPA_REQUEST_CANCELLED_RECEIVER.toComponentWithPrefix(Map.of("%player%", requester.getName())));
    tpaHandler.getTpaRequestMap().remove(requester);
    runnable.cancel();
  }
  
  public void acceptTpa() {
    tpaHandler.getTpaRequestMap().remove(requester);
    runnable.cancel();
    requester.sendMessage(Lang.TPA_REQUEST_ACCEPTED_REQUESTER.toComponentWithPrefix(Map.of("%player%", target.getName())));
    target.sendMessage(Lang.TPA_REQUEST_ACCEPTED_RECEIVER.toComponentWithPrefix(Map.of("%player%", requester.getName())));
    TeleportUtils.teleportWithDelay(requester, target.getLocation(), target.getName());
  }
  
  public void denyTpa() {
    tpaHandler.getTpaRequestMap().remove(requester);
    runnable.cancel();
    requester.sendMessage(Lang.TPA_REQUEST_DENIED_REQUESTER.toComponentWithPrefix(Map.of("%player%", target.getName())));
    target.sendMessage(Lang.TPA_REQUEST_DENIED_RECEIVER.toComponentWithPrefix(Map.of("%player%", requester.getName())));
  }
}
