package me.lagggpixel.core.modules.survival.handlers;

import lombok.Getter;
import me.lagggpixel.core.modules.survival.data.TpaRequest;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;


@Getter
public class TpaHandler {
  
  private final Map<Player, TpaRequest> tpaRequestMap = new HashMap<>();
  
}