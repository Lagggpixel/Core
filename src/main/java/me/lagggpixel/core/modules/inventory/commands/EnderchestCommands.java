package me.lagggpixel.core.modules.inventory.commands;

import me.lagggpixel.core.data.CommandClass;
import me.lagggpixel.core.data.Lang;
import me.lagggpixel.core.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnderchestCommands extends CommandClass implements Listener {

    private final Map<Player, Inventory> enderChestContents = new HashMap<>();

    @Override
    public String getCommandName() {

        return "enderchest";
    }

    @Override
    public String getCommandDescription() {

        return "Open your or another player's ender chest.";
    }

    @Override
    public List<String> getCommandAliases() {

        return List.of("ec", "enderc", "echest");
    }

    @Override
    public String getCommandPermission() {

        return "core.enderchest";
    }

    @Override
    public String getUsage() {

        return null;
    }

    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(commandSender instanceof Player sender)) {
            commandSender.sendMessage(Lang.PLAYER_ONLY.toComponentWithPrefix(null));
            return true;
        }
        if (args.length == 0) {
            openEnderChest(sender);
            return true;
        } else if (args.length == 1) {
            Player targetPlayer = Bukkit.getPlayer(args[0]);
            if (targetPlayer != null) {
                openEnderChest(sender, targetPlayer);
                return true;
            }
        }
        return false;
    }

    private void openEnderChest(Player viewer) {
        Inventory enderChest = getOrCreateEnderChest(viewer);
        viewer.openInventory(enderChest);
    }

    private void openEnderChest(Player viewer, Player targetPlayer) {
        Inventory targetEnderChest = getOrCreateEnderChest(targetPlayer);
        viewer.openInventory(targetEnderChest);
    }

    private Inventory getOrCreateEnderChest(Player player) {
        return enderChestContents.computeIfAbsent(player, p -> {

            Inventory enderChest = Bukkit.createInventory(p, 27, ChatUtils.stringToComponent(player.getName() + "'s Ender Chest"));
            enderChest.setContents(p.getEnderChest().getContents());
            return enderChest;
        });
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getInventory().getHolder() instanceof Player && event.getClickedInventory() != null) {
            if (event.getInventory().getType().equals(InventoryType.ENDER_CHEST)) {
                updateEnderChestContents(player, event.getInventory().getContents());
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if (event.getInventory().getType().equals(InventoryType.ENDER_CHEST)) {
            updateEnderChestContents(player, event.getInventory().getContents());
        }
    }

    private void updateEnderChestContents(Player player, ItemStack[] contents) {
        Inventory enderChest = enderChestContents.get(player);
        if (enderChest != null) {
            enderChest.setContents(contents);
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList();
        }
        return null;
    }
}
