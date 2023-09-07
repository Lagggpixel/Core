package me.lagggpixel.core.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class ChatUtil {

    public static Component convertStringWithColorCodesToComponent(String stringWithColorCodes) {

        return LegacyComponentSerializer.legacy('&').deserialize(stringWithColorCodes).asComponent();

    }

    public static TextComponent convertStringToComponent(String string) {
        return Component.text(string).toBuilder().build();
    }
}
