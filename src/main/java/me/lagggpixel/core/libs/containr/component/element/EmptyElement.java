package me.lagggpixel.core.libs.containr.component.element;

import me.lagggpixel.core.libs.containr.Element;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EmptyElement extends Element {

    @Contract(" -> new")
    @NotNull
    public static EmptyElement create() {
        return new EmptyElement();
    }

    @Nullable
    @Override
    public ItemStack item(Player player) {
        return null;
    }

}
