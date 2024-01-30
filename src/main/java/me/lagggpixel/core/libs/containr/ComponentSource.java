/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 *
 */

package me.lagggpixel.core.libs.containr;

/**
 * A source class that is used for publishing components to
 * a {@link ComponentTunnel}.
 * <p>
 * Also, {@link ComponentSource} is used in {@link Component}
 * for reactively filling the container with elements. So,
 * with this feeature, users are able to fill containers
 * dynamically from various sources.
 *
 * @author ZorTik
 * @since January 22, 2024
 */
public interface ComponentSource {

  boolean enable(ComponentTunnel tunnel);

  void disable(ComponentTunnel tunnel);

}
