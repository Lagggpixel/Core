/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.discord.handlers;

import me.lagggpixel.core.Main;
import org.javacord.api.entity.user.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Lagggpixel
 * @since February 07, 2024
 */
public class LinkHandler {

  private static LinkHandler instance;

  private final Random random;

  public final Map<UUID, Integer> tokens;

  private LinkHandler() {
    tokens = new HashMap<>();

    random = new Random();
  }

  public Integer createNewToken(UUID playerUuid) {
    Integer token = new Random().nextInt(999999);
    while (tokens.containsValue(token)) {
      token = random.nextInt(999999);
    }
    tokens.put(playerUuid, token);
    return token;
  }

  public void removeToken(UUID playerUuid) {
    tokens.remove(playerUuid);
  }

  public UUID getPlayer(Integer token) {
    AtomicReference<UUID> uuid = new AtomicReference<>();
    tokens.forEach((key, value) -> {
      if (Objects.equals(value, token)) {
        uuid.set(key);
      }
    });
    return uuid.get();
  }

  public UUID getLinkedPlayer(User user) {
    long id = user.getId();
    AtomicReference<UUID> uuid = new AtomicReference<>();
    Main.getUserData().forEach((key, userData) -> {
      if (userData.getDiscordId() != null && userData.getDiscordId() == id) {
        uuid.set(key);
      }
    });
    return uuid.get();
  }

  public static LinkHandler getInstance() {
    if (instance == null) {
      instance = new LinkHandler();
    }
    return instance;
  }

}
