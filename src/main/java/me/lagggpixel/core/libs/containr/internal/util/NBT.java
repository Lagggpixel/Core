/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 *
 */

package me.lagggpixel.core.libs.containr.internal.util;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author ZorTik
 * @since January 22, 2024
 */
public final class NBT {

  public static ItemStack putNBT(ItemStack stack, String key, String value) {
    return modifyNBT(stack, item -> item.setString(key, value));
  }

  @Nullable
  public static String getNBTString(ItemStack stack, String key) {
    return getFromNBT(stack, item -> item.hasKey(key)
        ? item.getString(key)
        : null);
  }

  public static ItemStack modifyNBT(ItemStack stack, Consumer<NBTItem> nbtItemConsumer) {
    NBTItem item = new NBTItem(stack);
    nbtItemConsumer.accept(item);
    return item.getItem();
  }

  public static <T> T getFromNBT(ItemStack stack, Function<NBTItem, T> nbtItemFunction) {
    NBTItem item = new NBTItem(stack);
    return nbtItemFunction.apply(item);
  }

}
