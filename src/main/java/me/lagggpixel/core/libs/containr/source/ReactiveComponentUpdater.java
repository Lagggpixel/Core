/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 */

package me.lagggpixel.core.libs.containr.source;

import lombok.RequiredArgsConstructor;
import me.lagggpixel.core.libs.containr.*;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@RequiredArgsConstructor
/**
 *  @author    Lagggpixel
 * @since January 27, 2024 January 22, 2024
 */
public class ReactiveComponentUpdater implements ComponentSource {

    private final ComponentSupplier supplier;
    private final List<UUID> registeredEvents = new CopyOnWriteArrayList<>();

    @Override
    public boolean enable(ComponentTunnel tunnel) {
        Container container = obtainContainer(tunnel);
        if (container == null) return false;

        UUID uuid = container.getEventBus().on(Container.Event.UpdateEvent.class, event -> {
            supplier.getComponents().forEach(event::append);
        });
        return registeredEvents.add(uuid);
    }

    @Override
    public void disable(ComponentTunnel tunnel) {
        Container container = obtainContainer(tunnel);
        if (container != null)
            for (UUID uuid : registeredEvents) {
                if(container.getEventBus().off(uuid)) registeredEvents.remove(uuid);
            }
    }

    /**
 *  @author    Lagggpixel
 * @since January 27, 2024 January 22, 2024
 */
public interface ComponentSupplier {
        Collection<Element> getComponents();
    }

    @Nullable
    private static Container obtainContainer(ComponentTunnel tunnel) {
        if(!(tunnel instanceof Container.LocalComponentTunnel))
            return null;

        ContainerComponent component = ((Container.LocalComponentTunnel) tunnel).getComponent();
        return component instanceof Container ? (Container) component : null;
    }

}
