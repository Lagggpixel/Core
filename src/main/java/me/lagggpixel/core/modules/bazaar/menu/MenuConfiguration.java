/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 */

package me.lagggpixel.core.modules.bazaar.menu;

import com.cryptomorin.xseries.XMaterial;
import me.lagggpixel.core.modules.bazaar.api.BazaarAPI;
import me.lagggpixel.core.modules.bazaar.api.bazaar.Bazaar;
import me.lagggpixel.core.modules.bazaar.api.menu.MenuInfo;
import me.lagggpixel.core.libs.containr.Component;
import me.lagggpixel.core.libs.containr.ContainerComponent;
import me.lagggpixel.core.libs.containr.builder.SimpleGUIBuilder;
import me.lagggpixel.core.libs.containr.internal.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  @author    Lagggpixel
 *  </p>
 *  Created on January 22, 2024
 */
public abstract class MenuConfiguration implements ConfigurationSerializable {
    protected final int rows;
    protected final List<DefaultConfigurableMenuItem> items;
    protected String name;

    public MenuConfiguration(String name, int rows, List<DefaultConfigurableMenuItem> items) {
        this.name = name;
        this.rows = rows;
        this.items = items;
    }

    public static void fillWithGlass(int rows, List<DefaultConfigurableMenuItem> items) {
        ItemStack glass = ItemBuilder.newBuilder(XMaterial.BLACK_STAINED_GLASS_PANE.parseItem()).withName(ChatColor.WHITE.toString()).build();
        for (int i = 0; i < rows * 9; i++) {
            int finalI = i;
            if (items.stream().anyMatch(configurableMenuItem -> configurableMenuItem.getSlot() == finalI)) continue;
            items.add(new DefaultConfigurableMenuItem(i, glass, ""));
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> args = new HashMap<>();
        args.put("name", name);
        args.put("rows", rows);
        args.put("items", items);
        return args;
    }

    protected SimpleGUIBuilder getMenuBuilder() {
        return Component.gui()
                .title(name)
                .rows(rows);
    }

    public String getName() {
        return name;
    }

    public int getRows() {
        return rows;
    }

    public List<DefaultConfigurableMenuItem> getItems() {
        return items;
    }

    public void setName(Bazaar bazaar, String name) {
        this.name = name;
        bazaar.saveConfig();
    }

    protected void loadItems(ContainerComponent containerComponent, BazaarAPI bazaarApi, Player player, MenuInfo info, boolean edit) {
        for (DefaultConfigurableMenuItem item : items) {
            item.putItem(containerComponent, bazaarApi, player, info, edit);
        }
    }
}
