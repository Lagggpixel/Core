/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 *
 */

package me.lagggpixel.core.libs.containr.builder;

import com.google.common.collect.Lists;
import me.lagggpixel.core.libs.containr.ContextClickInfo;
import me.lagggpixel.core.libs.containr.component.element.SwitchableElement;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author ZorTik
 * @since January 22, 2024
 */
public class SwitchableElementBuilder<T> implements ElementBuilder<SwitchableElement<T>> {

  private BiConsumer<ContextClickInfo, T> action = (i, o) -> {
  };
  private Function<T, ItemStack> itemFunc = (o) -> null;
  private final List<T> options = new ArrayList<>();

  @SafeVarargs
  public final SwitchableElementBuilder<T> options(T... options) {
    this.options.addAll(Lists.newArrayList(options));
    return this;
  }

  public final SwitchableElementBuilder<T> click(@NotNull BiConsumer<ContextClickInfo, T> action) {
    this.action = this.action.andThen(action);
    return this;
  }

  public final SwitchableElementBuilder<T> item(@Nullable ItemStack item) {
    return item((o) -> item);
  }

  public final SwitchableElementBuilder<T> item(@NotNull Function<T, ItemStack> itemFunc) {
    this.itemFunc = itemFunc;
    return this;
  }

  public SwitchableElement<T> build() {
    return new SwitchableElement<T>(options) {
      @Override
      public void action(ContextClickInfo info, T newOption) {
        action.accept(info, newOption);
      }

      @Override
      public ItemStack option(@Nullable T option) {
        return itemFunc.apply(option);
      }
    };
  }
}
