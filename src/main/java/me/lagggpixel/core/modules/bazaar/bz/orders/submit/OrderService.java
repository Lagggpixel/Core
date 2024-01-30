/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.bazaar.bz.orders.submit;

import me.lagggpixel.core.modules.bazaar.api.bazaar.orders.BazaarOrder;
import me.lagggpixel.core.modules.bazaar.api.bazaar.orders.InstantBazaarOrder;
import me.lagggpixel.core.modules.bazaar.api.bazaar.orders.InstantSubmitResult;
import me.lagggpixel.core.modules.bazaar.api.bazaar.orders.SubmitResult;

/**
 * @since January 22, 2024
 */
public interface OrderService {
  InstantSubmitResult submit(InstantBazaarOrder order);

  SubmitResult submit(BazaarOrder order);

  int claim(BazaarOrder order);

  int claim(InstantBazaarOrder order);
}
