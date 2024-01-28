/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 */

package me.lagggpixel.core.libs.containr;

import me.lagggpixel.core.libs.containr.internal.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * Container interface that represents a container component that does not
 * necessarily have to be a {@link Container} implementation.
 *
 * @author ZorTik
 */
/**
 *  @author    Lagggpixel
 * @since January 27, 2024 January 22, 2024
 */
public interface ContainerComponent extends Component, Iterable<Element> {

    <T extends Element> Map<Integer, T> content(Class<T> clazz);
    void attachSource(ComponentSource source);
    boolean appendContainer(Container container);
    boolean setContainer(int positionRelativeIndex, @NotNull Container container);
    boolean setContainer(@NotNull Container container, int positionRelativeIndex);
    boolean insertContainerInLine(Container container, int startPositionRelativeIndex, int endPositionRelativeIndex);
    void fillElement(Element element);
    void fillElement(Element element, int fromIndexInclusive, int toIndexExclusive);
    boolean appendElements(Collection<Element> elements);
    boolean appendElements(int startIndex, Collection<Element> elements);
    boolean appendElement(Element element);
    boolean appendElement(int startIndex, Element element);
    void setElement(int relativeIndex, Element element);
    @Deprecated
    void setElement(Element element, int relativeIndex);
    void moveAllByY(int yAddon, Class<?> filter);
    void compress();
    Optional<Pair<Container, Element>> findElementById(String id);
    IntStream searchContainers(Class<? extends Container> containerClass);
    int searchContainer(Class<? extends Container> containerClass);
    List<Container> getContainers(boolean deep);
    int[] getEmptyElementSlots();
    boolean isFreeSlot(int slot);
    List<Container> innerContainers();
    List<Element> innerElements();
    void clear();

}
