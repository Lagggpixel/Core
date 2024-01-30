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
import me.lagggpixel.core.libs.containr.component.element.AnimatedElement;
import me.lagggpixel.core.libs.containr.internal.util.CyclicArrayList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author ZorTik
 * @since January 22, 2024
 */
public class AnimatedElementBuilder implements ElementBuilder<AnimatedElement> {

  private Consumer<ContextClickInfo> action = (i) -> {
  };
  private final CyclicArrayList<Supplier<ItemStack>> frames = new CyclicArrayList<>();

  public final @NotNull AnimatedElementBuilder click(Consumer<ContextClickInfo> info) {
    this.action = this.action.andThen(info);
    return this;
  }

  public final @NotNull AnimatedElementBuilder frames(ItemStack... frames) {
    for (ItemStack frame : frames) {
      this.frames.add(() -> frame);
    }
    return this;
  }

  @SafeVarargs
  public final @NotNull AnimatedElementBuilder frames(Supplier<ItemStack>... frames) {
    this.frames.addAll(Lists.newArrayList(frames));
    return this;
  }

  @Override
  public @NotNull AnimatedElement build() {
    return new AnimatedElement(frames) {
      @Override
      public void click(ContextClickInfo info) {
        action.accept(info);
      }
    };
  }
}
