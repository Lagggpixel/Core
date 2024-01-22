/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 */

package me.lagggpixel.core.libs.containr.component.gui;

import lombok.Getter;
import lombok.Setter;
import me.lagggpixel.core.libs.containr.GUIRepository;
import me.lagggpixel.core.libs.containr.GUI;
import me.lagggpixel.core.libs.containr.component.element.AnimatedElement;
import me.lagggpixel.core.libs.containr.component.element.AnimatedSuppliedElement;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;

/**
 *  @author    Lagggpixel
 *  </p>
 *  Created on January 22, 2024
 */
public abstract class AnimatedGUI extends GUI {

    @Nullable
    private Player currentPlayer;
    private boolean stopped;
    @Setter
    @Getter
    private long period;

    public AnimatedGUI(String title, int rows, int period, TimeUnit unit) {
        super(title, rows);
        this.period = unit.toMillis(period);
        this.currentPlayer = null;
        this.stopped = false;
    }

    @Override
    public void open(@NotNull Player p, boolean update) {
        super.open(p, update);
        this.currentPlayer = p;
        new BukkitRunnable() {
            @Override
            public void run() {
                GUI gui = GUIRepository.OPENED_GUIS.getOrDefault(currentPlayer.getName(), null);
                if(!currentPlayer.isOnline() || gui != AnimatedGUI.this || stopped) {
                    cancel();
                    return;
                }
                tickAnimatedElements(currentPlayer);
            }
        }.runTaskTimer(Bukkit.getPluginManager().getPlugins()[0], 0L, period / (1000 / 20));
    }

    public void kill() {
        this.stopped = true;
    }

    @Deprecated
    public void onPreTick(Player player) {}

    public void tickAnimatedElements(Player player) {
        onPreTick(player);
        update(player, AnimatedSuppliedElement.class, AnimatedElement.class);
    }

}
