package me.lagggpixel.core.builders;

import me.lagggpixel.core.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unused"})
public class ItemBuilder {

    private final ItemStack item;
    private final ItemMeta meta;

    public ItemBuilder() {
        item = new ItemStack(Material.DIRT);
        meta = item.getItemMeta();
    }

    public ItemBuilder(ItemStack stack) {
        item = stack;
        meta = item.getItemMeta();
    }

    public ItemBuilder(Component name) {
        item = new ItemStack(Material.DIRT);
        meta = item.getItemMeta();
        setDisplayName(name);
    }

    public ItemBuilder(Material material) {
        item = new ItemStack(material);
        meta = item.getItemMeta();
    }

    public ItemBuilder(Component name, int amount) {
        item = new ItemStack(Material.DIRT, amount);
        meta = item.getItemMeta();
        setDisplayName(name);
    }

    public ItemBuilder(Component name, Material material) {
        item = new ItemStack(material);
        meta = item.getItemMeta();
        setDisplayName(name);
    }

    public ItemBuilder(Component name, Material material, int amount) {
        item = new ItemStack(material, amount);
        meta = item.getItemMeta();
        setDisplayName(name);
    }

    public ItemBuilder setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }
    
    public ItemBuilder setDisplayName(String name) {
        meta.displayName(ChatUtils.stringToComponentCC(name));
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setDisplayName(Component name) {
        meta.displayName(name);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setMaterial(Material material) {
        item.setType(material);
        return this;
    }

    public ItemBuilder setLore(List<Component> lore) {
        meta.lore(lore);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setLore(Component... lore) {
        List<Component> ls = new ArrayList<>();
        Collections.addAll(ls, lore);
        meta.lore(ls);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addLore(List<Component> lore) {
        List<Component> ls;
        if (meta.lore() != null) ls = meta.lore(); else ls = new ArrayList<>();
        assert ls != null;
        ls.addAll(lore);
        meta.lore(ls);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addLore(Component lore) {
        List<Component> ls;
        if (meta.lore() != null) ls = meta.lore(); else ls = new ArrayList<>();
        assert ls != null;
        ls.add(lore);
        meta.lore(ls);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addLore(Component... lore) {
        List<Component> ls;
        if (meta.lore() != null) ls = meta.lore(); else ls = new ArrayList<>();
        assert ls != null;
        Collections.addAll(ls, lore);
        meta.lore(ls);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment) {
        meta.addEnchant(enchantment, 1, false);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        meta.addEnchant(enchantment, level, false);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level, boolean ignoreMaxLevel) {
        meta.addEnchant(enchantment, level, ignoreMaxLevel);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder removeEnchantment(Enchantment enchantment) {
        meta.removeEnchant(enchantment);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addEnchantmentGlint() {
        meta.addEnchant(Enchantment.LUCK, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder removeEnchantmentGlint() {
        meta.removeEnchant(Enchantment.LUCK);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addItemFlag(ItemFlag itemFlag) {
        meta.addItemFlags(itemFlag);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addItemFlags(ItemFlag... itemFlags) {
        meta.addItemFlags(itemFlags);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder removeItemFlag(ItemFlag itemFlag) {
        meta.removeItemFlags(itemFlag);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder removeItemFlags(ItemFlag... itemFlags) {
        meta.removeItemFlags(itemFlags);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder dyeColor(Color color) {
        LeatherArmorMeta leather = (LeatherArmorMeta) meta;
        leather.setColor(color);
        item.setItemMeta(leather);
        return this;
    }

    public ItemStack toItemStack(){
        return item;
    }

}