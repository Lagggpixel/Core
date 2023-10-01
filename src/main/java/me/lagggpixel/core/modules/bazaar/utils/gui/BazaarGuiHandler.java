package me.lagggpixel.core.modules.bazaar.utils.gui;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class BazaarGuiHandler {

    private final HashMap<String, String> guiCommands;
    private final HashMap<String, BazaarGui> guis;

    public BazaarGuiHandler() {
        this.guis = new HashMap<>();
        this.guiCommands = new HashMap<>();
    }

    public void registerGui(String name, BazaarGui bazaarGui) {
        this.guis.put(name, bazaarGui);
    }

    public BazaarGui getGui(String name) {
        return this.guis.get(name);
    }

    public void show(String name, Player player) {
        BazaarGui bazaarGui = this.guis.get(name);
        String command = this.guiCommands.get(name);

        if (bazaarGui == null && command == null) return;

        if (bazaarGui != null) {
            bazaarGui.show(player);
        } else {
            player.performCommand(command);
        }
    }

    public void hide(String name, Player player) {
        BazaarGui bazaarGui = this.guis.get(name);

        if (bazaarGui == null) return;

        bazaarGui.hide(player);
    }

    public void registerGuiCommand(String name, String command) {
        this.guiCommands.put(name, command);
    }

}
