/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class ChatUtils {
  
  public static @NotNull Component stringToComponentCC(String stringWithColorCodes) {
    return LegacyComponentSerializer.legacy('&').deserialize(stringWithColorCodes).asComponent();
  }

  public static @NotNull TextComponent stringToTextComponentCC(String stringWithColorCodes) {
    return LegacyComponentSerializer.legacy('&').deserialize(stringWithColorCodes);
  }
  
  @Contract("_ -> new")
  public static @NotNull Component stringToComponent(String string) {
    return Component.text(string).toBuilder().build();
  }

  @Contract("_ -> new")
  public static @NotNull TextComponent stringToTextComponent(String string) {
    return Component.text(string).toBuilder().build();
  }
  
  public static @NotNull String componentToString(Component component) {
    if (component == null) {
      return "";
    }
    return PlainTextComponentSerializer.plainText().serialize(component);
  }

  public static @NotNull Component stripColor(Component component) {
    return PlainTextComponentSerializer.builder().build().deserialize(PlainTextComponentSerializer.plainText().serialize(component));
  }

}
