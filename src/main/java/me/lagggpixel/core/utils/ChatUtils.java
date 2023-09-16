package me.lagggpixel.core.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ChatUtils {

    public static @NotNull Component convertStringWithColorCodesToComponent(String stringWithColorCodes) {
        return LegacyComponentSerializer.legacy('&').deserialize(stringWithColorCodes).asComponent();
    }

    @Contract("_ -> new")
    public static @NotNull TextComponent convertStringToComponent(String string) {
        return Component.text(string).toBuilder().build();
    }
}
