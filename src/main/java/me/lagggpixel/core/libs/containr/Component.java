/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 */

package me.lagggpixel.core.libs.containr;

import me.lagggpixel.core.libs.containr.builder.*;
import me.lagggpixel.core.libs.containr.internal.util.ItemBuilder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;

/**
 * Represents any component usable in the Containr library.
 * Also, this is the place where component api methods are present.
 *
 * @author ZorTik
 */
/**
 *  @author    Lagggpixel
 *  </p>
 *  Created on January 22, 2024
 */
public interface Component {

    /**
     * ElementDeserializer could help with centralizing
     * deserialization which allows us to use shared postprocessors
     * for deserialization.
     * <p></p>
     * This class can be used same way as the #element methods in Component
     * class, with the difference that it's centralized.
     *
     * <p></p>
     * Usage:
     * <pre>
     *     ElementDeserializer deserializer = Component.elementDeserializer();
     *     deserializer.namePostprocessor((name, item) -> {
     *          // Process name and set it to the final item.
     *          // This will be called after the item is built.
     *     });
     *     ConfigurationSection itemSection = ...;
     *     Element element = deserializer.element(itemSection);
     * </pre>
     */
    class ElementDeserializer {

        private final List<BiConsumer<ItemBuilder, ConfigurationSection>> after;

        private ElementDeserializer() {
            after = new CopyOnWriteArrayList<>();
        }

        public @NotNull ElementDeserializer namePostprocessor(BiConsumer<String, ItemStack> mod) {
            return addPostprocessor((b, s) -> b.withBuildModifier(item -> mod.accept(b.getName(), item)));
        }

        public @NotNull ElementDeserializer lorePostprocessor(BiConsumer<List<String>, ItemStack> mod) {
            return addPostprocessor((b, s) -> b.withBuildModifier(item -> mod.accept(b.getLore(), item)));
        }

        public @NotNull ElementDeserializer addPostprocessor(BiConsumer<ItemBuilder, ConfigurationSection> mod) {
            after.add(mod);
            return this;
        }

        // *** These methods directly call the Component.element methods ***

        public @NotNull SimpleElementBuilder element() {
            return Component.element();
        }

        public @NotNull SimpleElementBuilder element(ConfigurationSection section) {
            return Component.element(section);
        }

        public @NotNull SimpleElementBuilder element(
                ConfigurationSection section,
                BiConsumer<ItemBuilder, ConfigurationSection> modifier
        ) {
            return Component.element(section, (b, s) -> {
                modifier.accept(b, s);
                after.forEach(consumer -> consumer.accept(b, s));
            });
        }

        public ItemBuilder apply(ItemStack item, @Nullable ConfigurationSection section) {
            ItemBuilder builder = ItemBuilder.newBuilder(item);
            after.forEach(consumer -> consumer.accept(builder, section));
            return builder;
        }

    }

    /**
     * Creates a new GUI builder.
     *
     * @return The GUI builder
     */
    static @NotNull SimpleGUIBuilder gui() {
        return new SimpleGUIBuilder();
    }

    /**
     * Creates a new pattern GUI builder with provided title and pattern.
     *
     * @return The GUI builder
     */
    static @NotNull PatternGUIBuilder gui(@NotNull String title, @NotNull String[] pattern) {
        return new PatternGUIBuilder(title, pattern);
    }

    /**
     * Creates a new container builder for static container.
     *
     * @return The container builder
     */
    static @NotNull ContainerBuilder<StaticContainer> staticContainer() {
        return ContainerBuilder.newBuilder(StaticContainer.class);
    }

    /**
     * Creates a new container builder for paged container.
     *
     * @return The container builder
     */
    static @NotNull ContainerBuilder<PagedContainer> pagedContainer() {
        return ContainerBuilder.newBuilder(PagedContainer.class);
    }

    /**
     * Creates a new simple element builder.
     * This is a simple way to create an element without inheriting
     * the element class.
     *
     * @return The element builder
     */
    static @NotNull SimpleElementBuilder element() {
        return new SimpleElementBuilder();
    }

    /**
     * Creates a new simple element builder with provided static item.
     *
     * @param item The item to set
     * @return The element builder
     */
    static @NotNull SimpleElementBuilder element(ItemStack item) {
        return new SimpleElementBuilder().item(item);
    }

    static @NotNull SimpleElementBuilder element(ConfigurationSection section) {
        return SimpleElementBuilder.fromConfig(section);
    }

    static @NotNull SimpleElementBuilder element(
            ConfigurationSection section,
            BiConsumer<ItemBuilder, ConfigurationSection> modifier
    ) {
        return SimpleElementBuilder.fromConfig(section, modifier);
    }

    static @NotNull ElementDeserializer elementDeserializer() {
        return new ElementDeserializer();
    }

    /**
     * Creates a new switchable element builder.
     *
     * @param optionsType The type class of the options
     * @return The switchable element builder
     * @param <T> The type of the options
     */
    static @NotNull <T> SwitchableElementBuilder<T> switchableElement(Class<T> optionsType) {
        return new SwitchableElementBuilder<>();
    }

    /**
     * Creates a builder to create a new animated element.
     *
     * @return The animated element builder
     */
    static @NotNull AnimatedElementBuilder animatedElement() {
        return new AnimatedElementBuilder();
    }

}
