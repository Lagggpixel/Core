/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 */

package me.lagggpixel.core.modules.bazaar.api.menu;

import me.lagggpixel.core.libs.containr.ContextClickInfo;

import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public interface ClickActionManager {
  void addClickAction(String name, Consumer<ContextClickInfo> action);

  void addClickAction(String name, Function<MenuInfo, Consumer<ContextClickInfo>> action);

  void addEditClickAction(String name, BiFunction<ConfigurableMenuItem, MenuInfo, Consumer<ContextClickInfo>> action);

  Consumer<ContextClickInfo> getClickAction(ConfigurableMenuItem configurableMenuItem, MenuInfo menuInfo, boolean editing);

  Set<String> getActions();
}
