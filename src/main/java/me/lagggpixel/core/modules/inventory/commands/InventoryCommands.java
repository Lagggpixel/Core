package me.lagggpixel.core.modules.inventory.commands;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.CommandClass;
import me.lagggpixel.core.data.Lang;
import me.lagggpixel.core.data.User;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class InventoryCommands extends CommandClass {

    @Override
    public String getCommandName() {
        return "inventory";
    }

    @Override
    public String getCommandDescription() {
        return null;
    }

    @Override
    public List<String> getCommandAliases() {
        return List.of("inv");
    }

    @Override
    public String getCommandPermission() {
        return "core.inventory.command";
    }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (!(commandSender instanceof Player sender)) {
            commandSender.sendMessage(Lang.PLAYER_ONLY.toComponentWithPrefix(null));
            return true;
        }

        UUID playerUUID = sender.getUniqueId();
        User user = Main.getUser(playerUUID);

        if (args.length == 0) {
            // Invalid arguments exception
            return true;
        }

        if (args[0].equalsIgnoreCase("clear")) {
            if (args.length == 1) {

                sender.getInventory().clear();
                sender.getInventory().setHelmet(null);
                sender.getInventory().setChestplate(null);
                sender.getInventory().setLeggings(null);
                sender.getInventory().setBoots(null);

                // Send player message for inventory cleared

                return true;

            }
            if (args.length == 2) {
                String playerName = args[1];
                Player player = Bukkit.getPlayerExact(playerName);

                // Checks for another player's inventory and clears it

                return true;
            }

            // Invalid argument exception

            return true;
        }



        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}
