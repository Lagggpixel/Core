package me.lagggpixel.core.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ChatUtils {
  
  public static @NotNull Component stringToComponentCC(String stringWithColorCodes) {
    return LegacyComponentSerializer.legacy('&').deserialize(stringWithColorCodes).asComponent();
  }
  
  @Contract("_ -> new")
  public static @NotNull TextComponent stringToComponent(String string) {
    return Component.text(string).toBuilder().build();
  }
  
  public static @NotNull String componentToString(Component component) {
    if (component == null) {
      return "";
    }
    return PlainTextComponentSerializer.plainText().serialize(component);
  }
}
