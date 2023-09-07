package me.lagggpixel.core.modules.homes.commands;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.data.CommandClass;
import me.lagggpixel.core.data.Lang;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.modules.homes.data.Home;
import me.lagggpixel.core.modules.homes.managers.HomeManager;
import me.lagggpixel.core.utils.ChatUtil;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HomeCommands extends CommandClass implements Listener {

    HomeManager homeManager;

    public HomeCommands(HomeManager homeManager) {
        this.homeManager = homeManager;
    }

    @Override
    public String getCommandName() {
        return "home";
    }

    @Override
    public String getCommandDescription() {
        return null;
    }

    @Override
    public List<String> getCommandAliases() {
        return List.of("homes");
    }

    @Override
    public String getCommandPermission() {
        return "core.home.command";
    }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(Lang.PLAYER_ONLY.toComponentWithPrefix(null));
            return true;
        }

        UUID playerUUID = player.getUniqueId();
        User user = Main.getUser(playerUUID);

        if (args.length == 0) {
            openHomesGUI(player, user);
            return true;
        }

        if (args[0].equalsIgnoreCase("list")) {
            // Open homes GUI
            openHomesGUI(player, user);
            return true;
        }

        if (args[0].equalsIgnoreCase("set")) {

            if (args.length != 2) {
                player.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix(null));
                return true;
            }

            // Checks if the player can create more homes

            String homeName = args[1];

            if (user.getHomes().containsKey(homeName)) {
                player.sendMessage(Lang.HOME_ALREADY_EXIST.toComponentWithPrefix(Map.of(
                        "%home%", homeName
                )));
                return true;
            }

            if (!isHomeNameValid(homeName)) {
                player.sendMessage(Lang.HOME_NAME_INVALID.toComponentWithPrefix(null));
                return true;
            }

            Home home = homeManager.createHomeObject(homeName, player.getLocation());
            homeManager.setHome(user, homeName, home);

            player.sendMessage(Lang.HOME_CREATED.toComponentWithPrefix(Map.of(
                    "%home%", homeName
            )));

            return true;
        }

        if (args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("delete")) {

            if (args.length != 2) {
                player.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix(null));
                return true;
            }

            String homeName = args[1];

            if (!user.getHomes().containsKey(homeName)) {
                player.sendMessage(Lang.HOME_DOES_NOT_EXIST.toComponentWithPrefix(Map.of(
                        "%home%", homeName
                )));
                return true;
            }

            homeManager.deleteHome(user, homeName);

            player.sendMessage(Lang.HOME_DELETED.toComponentWithPrefix(Map.of(
                    "%home%", homeName
            )));

            return true;
        }

        player.sendMessage(Lang.INVALID_USAGE.toComponentWithPrefix(null));

        return true;
    }

    private void openHomesGUI(Player player, User user) {
        Inventory gui = player.getServer().createInventory(null, 27, homeManager.HOME_GUI_NAME);

        for (Map.Entry<String, Home> homeEntry : user.getHomes().entrySet()) {
            ItemStack homeItem = createHomeItem(homeEntry.getKey());
            gui.addItem(homeItem);
        }

        player.openInventory(gui);
    }

    private ItemStack createHomeItem(String homeName) {

        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        meta.displayName(ChatUtil.convertStringToComponent(homeName));
        meta.getPersistentDataContainer().set(homeManager.HOME_ITEM_NAMESPACE_KEY, PersistentDataType.STRING, homeName);

        item.setItemMeta(meta);

        return item;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }

    public boolean isHomeNameValid(String homeName) {
        int minLength = 3;
        int maxLength = 20;

        if (homeName.length() < minLength || homeName.length() > maxLength) {
            return false;
        }

        // Check if the home name contains special characters
        if (!homeName.matches("^[a-zA-Z0-9]+$")) {
            return false;
        }

        return true;
    }

}
