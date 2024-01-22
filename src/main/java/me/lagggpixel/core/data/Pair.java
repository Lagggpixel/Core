/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.data;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
/**
 *  @author    Lagggpixel
 *  </p>
 *  Created on January 22, 2024
 */
public class Pair<F, S> {
  
  private final F first;
  private final S second;
  
  @Contract("_, _ -> new")
  public static <F, S> @NotNull Pair<F, S> of(F first, S second) {
    return new Pair<>(first, second);
  }
  
  @Contract(" -> new")
  public static <F, S> @NotNull Pair<F, S> empty() {
    return new Pair<>(null, null);
  }
  
  public boolean bothPresent() {
    return this.first != null && this.second != null;
  }
  
  public boolean isPresent() {
    return this.first != null || this.second != null;
  }
  
  public boolean isEmpty() {
    return this.first == null && this.second == null;
  }
  
  public F first() {
    return this.first;
  }
  
  public S second() {
    return this.second;
  }
  
}