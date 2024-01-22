/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 */

package me.lagggpixel.core.modules.bazaar.menu;

import me.lagggpixel.core.modules.bazaar.api.BazaarAPI;
import me.lagggpixel.core.modules.bazaar.api.bazaar.Bazaar;
import me.lagggpixel.core.modules.bazaar.api.menu.ConfigurableMenuItem;
import me.lagggpixel.core.modules.bazaar.api.menu.MenuInfo;
import me.lagggpixel.core.modules.bazaar.utils.MenuUtils;
import me.lagggpixel.core.libs.containr.Component;
import me.lagggpixel.core.libs.containr.ContainerComponent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 *  @author    Lagggpixel
 *  </p>
 *  Created on January 22, 2024
 */
public class DefaultConfigurableMenuItem implements ConfigurableMenuItem {
    private int slot;
    private ItemStack item;
    private String action;

    public DefaultConfigurableMenuItem(int slot, ItemStack item, String action) {
        this.slot = slot;
        this.item = item;
        this.action = action;
    }

    public static DefaultConfigurableMenuItem deserialize(Map<String, Object> args) {
        return new DefaultConfigurableMenuItem((Integer) args.get("slot"),
                (ItemStack) args.get("item"),
                (String) args.get("action"));
    }

    @Override
    public int getSlot() {
        return slot;
    }

    @Override
    public void setSlot(Bazaar bazaar, int slot) {
        this.slot = slot;
        bazaar.saveConfig();
    }

    @Override
    public ItemStack getItem() {
        return item;
    }

    @Override
    public void setItem(Bazaar bazaar, ItemStack item) {
        this.item = item.clone();
        bazaar.saveConfig();
    }

    @Override
    public String getAction() {
        return action;
    }

    @Override
    public void setAction(Bazaar bazaar, String action) {
        this.action = action;
        bazaar.saveConfig();
    }

    @Override
    public void putItem(ContainerComponent containerComponent, BazaarAPI bazaarApi, Player player, MenuInfo info, boolean editMenu) {
        containerComponent.setElement(slot, Component.element(bazaarApi.getItemPlaceholders().replaceItemPlaceholders(containerComponent, MenuUtils.appendEditLore(item, editMenu), slot, player, info))
                .click(bazaarApi.getClickActionManager().getClickAction(this, info, editMenu))
                .build());
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> args = new HashMap<>();
        args.put("slot", slot);
        args.put("item", item);
        args.put("action", action);
        return args;
    }
}
