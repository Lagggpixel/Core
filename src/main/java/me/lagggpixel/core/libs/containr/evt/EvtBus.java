/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 */

package me.lagggpixel.core.libs.containr.evt;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  @author    Lagggpixel
 *  </p>
 *  Created on January 22, 2024
 */
public class EvtBus<T> {

    private final Map<UUID, ListenerWrapper<T>> entries = new ConcurrentHashMap<>();

    public <E extends T> @Nullable UUID on(Class<E> evt, EvtListener<E> listener) {
        return on(evt, listener, false);
    }

    public <E extends T> @Nullable UUID on(Class<E> evt, EvtListener<E> listener, boolean requireOnly) {
        if (requireOnly && entries.values().stream().anyMatch(w -> w.evtTypeSuper.equals(evt))) {
            return null;
        }
        UUID uuid = UUID.randomUUID();
        entries.put(uuid, new ListenerWrapper<>(evt, listener));
        return uuid;
    }

    public boolean off(UUID uuid) {
        return entries.remove(uuid) != null;
    }

    public void emit(T eventObject) {
        for (ListenerWrapper<T> entry : entries.values()) {
            entry.onEvent(eventObject);
        }
    }

    /**
 *  @author    Lagggpixel
 *  </p>
 *  Created on January 22, 2024
 */
public interface EvtListener<I> {
        void onEvent(I event);
    }

    @SuppressWarnings("unchecked, rawtypes")
    @RequiredArgsConstructor
    private static final class ListenerWrapper<I> implements EvtListener<I> {

        private final Class<? extends I> evtTypeSuper;
        private final EvtListener thatListener;

        @Override
        public void onEvent(I event) {
            if(evtTypeSuper.isAssignableFrom(event.getClass())) {
                thatListener.onEvent(event);
            }
        }
    }

}
