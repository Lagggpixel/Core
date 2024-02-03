/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.data.user;

import com.google.gson.annotations.Expose;
import lombok.*;
import org.mineskin.com.google.gson.annotations.SerializedName;

/**
 * @author Lagggpixel
 * @since February 03, 2024
 */
@Data
@Getter
@Setter
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class UserPreference {

  // Whether if the user has acknowledged the piston rules for claims
  @SerializedName("AcknowledgedPistonRules")
  @Expose
  private boolean acknowledgedPistonRules = false;

}
