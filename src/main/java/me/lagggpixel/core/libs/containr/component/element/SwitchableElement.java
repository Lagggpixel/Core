/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 *
 */

package me.lagggpixel.core.libs.containr.component.element;

import com.google.common.collect.Lists;
import lombok.Getter;
import me.lagggpixel.core.libs.containr.Container;
import me.lagggpixel.core.libs.containr.ContextClickInfo;
import me.lagggpixel.core.libs.containr.Element;
import me.lagggpixel.core.libs.containr.internal.util.CyclicArrayList;
import me.lagggpixel.core.libs.containr.internal.util.QuadConsumer;
import me.lagggpixel.core.libs.containr.GUI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author ZorTik
 * @since January 22, 2024
 */
@Getter
public abstract class SwitchableElement<T> extends Element {

  private final CyclicArrayList<T> options;

  public SwitchableElement() {
    this(Lists.newArrayList());
  }

  public SwitchableElement(List<T> options) {
    this.options = new CyclicArrayList<>(options);
  }

  public abstract ItemStack option(@Nullable T option);

  @Deprecated
  public QuadConsumer<GUI, Container, Player, ClickType> action(@Nullable T newOption) {
    return (o1, o2, o3, o4) -> {
    };
  }

  @ApiStatus.OverrideOnly
  public void action(ContextClickInfo info, T newOption) {
  }

  @Override
  public void click(ContextClickInfo info) {
    next();
    action(info, options.getCurrent().orElse(null));
    action(options.getCurrent().orElse(null)).accept(
        info.getGui(),
        info.getContainer(),
        info.getPlayer(),
        info.getClickType());
  }

  public void next() {
    options.getNext();
  }

  public boolean setCurrent(T obj) {
    for (int i = 0; i < options.size(); i++) {
      T objAtIIndex = options.get(i);
      if (obj.equals(objAtIIndex)) {
        options.setCurrentPos(i);
        return true;
      }
    }
    return false;
  }

  @Nullable
  @Override
  public ItemStack item(Player player) {
    return option(options.getCurrent().orElse(null));
  }
}
