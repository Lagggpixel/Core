package me.lagggpixel.core.modules.merchant.data;

import com.cryptomorin.xseries.XItemStack;
import de.tr7zw.changeme.nbtapi.NBTItem;
import lombok.Data;
import me.lagggpixel.core.Main;
import me.lagggpixel.core.builders.ItemBuilder;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.modules.economy.managers.EconomyManager;
import me.lagggpixel.core.modules.merchant.MerchantModule;
import me.lagggpixel.core.utils.ChatUtils;
import me.lagggpixel.core.utils.InventoryUtils;
import me.lagggpixel.core.utils.NumberUtil;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.ArmorStandTrait;
import net.citizensnpcs.trait.LookClose;
import net.citizensnpcs.trait.SkinTrait;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.text.DecimalFormat;
import java.util.*;

@Data
public class Merchant implements Listener {
  
  private final Component name;
  
  private final String skinSignature;
  private final String skinValue;
  
  private int interactionIteration = 0;
  
  private NPC npc;
  private ArmorStand stand;
  private ArmorStand click;
  
  private final List<MerchantItem> items;
  private final Location location;
  
  private final boolean villager;
  private final Villager.Profession profession;
  
  public Merchant(String name, String skinValue, String skinSignature, List<MerchantItem> items, Location location, boolean villager, Villager.Profession profession) {
    this.name = ChatUtils.stringToComponentCC(name);
    
    this.skinSignature = skinSignature;
    this.skinValue = skinValue;
    
    this.items = items;
    
    this.location = location;
    
    this.villager = villager;
    this.profession = profession;
  }
  
  public Merchant(Component name, String skinValue, String skinSignature, List<MerchantItem> items, Location location, boolean villager, Villager.Profession profession) {
    this.name = name;
    
    this.skinSignature = skinSignature;
    this.skinValue = skinValue;
    
    this.items = items;
    
    this.location = location;
    
    this.villager = villager;
    this.profession = profession;
  }
  
  private List<ItemStack> getSold(User user) {
    return user.getMerchantSold();
  }
  
  private ItemStack createBuyBack(User user) {
    DecimalFormat formatter = new DecimalFormat("#,###");
    formatter.setGroupingUsed(true);
    if (!getSold(user).isEmpty()) {
      ItemStack lastSold = getSold(user).get(getSold(user).size() - 1).clone();
      NBTItem nbt = new NBTItem(lastSold);
      return new ItemBuilder(nbt.getItem())
          .setLore(List.of(
              ChatUtils.stringToComponentCC("&7Cost"),
              ChatUtils.stringToComponentCC("&6" + formatter.format(nbt.getDouble("merchantCost")) + " &6coins"),
              ChatUtils.stringToComponentCC(""),
              ChatUtils.stringToComponentCC("&eClick to buyback!")
          ))
          .toItemStack();
    } else {
      return new ItemBuilder(Material.HOPPER)
          .setDisplayName("&aSell Item")
          .setLore(List.of(
              ChatUtils.stringToComponentCC("&7Click items in your inventory to"),
              ChatUtils.stringToComponentCC("&7sell them to this merchant!")))
          .toItemStack();
    }
  }
  
  public void createNpc() {
    List<Object> npcData = spawnNpc(this.location, ChatUtils.componentToString(this.name), this.skinValue, this.skinSignature, true, true, this.villager, this.profession);
    
    this.npc = (NPC) npcData.get(0);
    
    npc.getEntity().setMetadata("merchant", new FixedMetadataValue(Main.getInstance(), true));
    npc.getEntity().setMetadata("merchantName", new FixedMetadataValue(Main.getInstance(), name));
  }
  
  @EventHandler
  public void onRightClick(NPCRightClickEvent event) {
    if (!event.getNPC().equals(this.npc)) return;
    
    Player player = event.getClicker();
    User user = Main.getUser(player);
    
    Inventory inventory = Bukkit.createInventory(null, 54, this.name);
    
    InventoryUtils.fillBorder(inventory);
    
    DecimalFormat formatter = new DecimalFormat("#,###");
    formatter.setGroupingUsed(true);
    
    for (MerchantItem item : this.items) {
      ItemStack stack = new ItemStack(item.getMaterial());
      
      if (stack.getItemMeta().lore() != null) {
        if (Objects.requireNonNull(stack.getItemMeta().lore())
            .stream().map(ChatUtils::componentToString).noneMatch(l -> l.contains("Right-Click for more trading options!"))) {
          ItemMeta meta = stack.getItemMeta();
          List<Component> lore = meta.lore();
          if (lore == null) {
            lore = new ArrayList<>();
          }
          lore.addAll(List.of(ChatUtils.stringToComponentCC(" "),
              ChatUtils.stringToComponentCC("&7Cost"),
              ChatUtils.stringToComponentCC(" "),
              ChatUtils.stringToComponentCC("&6" + formatter.format(item.getCost()) + " &6coins"),
              ChatUtils.stringToComponentCC(" "),
              ChatUtils.stringToComponentCC(" "),
              ChatUtils.stringToComponentCC("&eClick to trade!"),
              ChatUtils.stringToComponentCC(" "),
              ChatUtils.stringToComponentCC("&eRight-Click for more trading options!")));
          
          
          meta.lore(lore);
          stack.setItemMeta(meta);
        }
      }
      
      NBTItem nbt = new NBTItem(stack);
      
      nbt.setBoolean("merchantItem", true);
      nbt.setString("merchantName", ChatUtils.componentToString(this.name));
      nbt.setInteger("merchantCost", item.getCost());
      
      inventory.addItem(nbt.getItem());
    }
    
    inventory.setItem(49, createBuyBack(user));
    player.openInventory(inventory);
  }
  
  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    if (!event.getView().title().equals(this.name)) return;
    
    if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR)) return;
    
    ItemStack item = event.getCurrentItem();
    
    event.setCancelled(true);
    
    NBTItem nbt = new NBTItem(item);
    
    Player player = (Player) event.getWhoClicked();
    User user = Main.getUser(player);
    
    DecimalFormat formatter = new DecimalFormat("#,###");
    formatter.setGroupingUsed(true);
    
    if (nbt.hasTag("merchantItem") || nbt.hasTag("merchantSold")) {
      if (event.isRightClick() && nbt.hasTag("merchantItem")) {
        Gui gui = new Gui(ChatUtils.stringToComponentCC("Shop Trading Options"), 54, new HashMap<>());
        
        InventoryUtils.fillEmpty(gui);
        gui.addItem(49, buildCloseButton());
        gui.addItem(48, buildBackButton(List.of(ChatUtils.stringToComponentCC("&7To " + this.name))));
        
        gui.addItem(20, buildShopOption(nbt.getItem(), 1, player, gui));
        gui.addItem(21, buildShopOption(nbt.getItem(), 5, player, gui));
        gui.addItem(22, buildShopOption(nbt.getItem(), 10, player, gui));
        gui.addItem(23, buildShopOption(nbt.getItem(), 32, player, gui));
        gui.addItem(24, buildShopOption(nbt.getItem(), 64, player, gui));
        
        gui.getClickEvents().put(ChatUtils.stringToComponentCC("&aGo Back"), () -> {
          player.openInventory(event.getInventory());
        });
        
        gui.show(player);
        
        return;
      }
      
      if (EconomyManager.getInstance().getBalance(player) < nbt.getInteger("merchantCost")) {
        player.sendMessage(ChatUtils.stringToComponentCC("&cYou do not have enough coins to purchase this item!"));
        return;
      }
      
      EconomyManager.getInstance().withdraw(player, nbt.getInteger("merchantCost"));
      
      if (nbt.hasTag("merchantItem")) {
        XItemStack.giveOrDrop(player, new ItemStack(item.getType()));
        Component displayName = item.getItemMeta().displayName();
        Component message;
        if (displayName == null) {
          message = ChatUtils.stringToComponentCC("&aYou have purchased null &afor &6" + formatter.format(nbt.getInteger("merchantCost")) + " coins&a!");
        }
        else {
          message = ChatUtils.stringToComponentCC("&aYou have purchased ")
              .append(displayName)
              .append(ChatUtils.stringToComponentCC(" &afor &6" + formatter.format(nbt.getInteger("merchantCost")) + " coins&a!"));
        }
        player.sendMessage(message);
      } else {
        nbt = new NBTItem(getSold(user).get(getSold(user).size() - 1));
        nbt.setBoolean("merchantSold", null);
        player.getInventory().addItem(nbt.getItem());
        Component displayName = item.getItemMeta().displayName();
        Component message;
        if (displayName == null) {
          message =
              ChatUtils.stringToComponentCC("&aYou have bought back null &afor " + "&6" + formatter.format(nbt.getInteger("merchantCost")) + " coins&a!");
        }
        else {
          message =
              ChatUtils.stringToComponentCC("&aYou have bought back ")
                  .append(displayName)
                  .append(ChatUtils.stringToComponentCC(" &afor " + "&6" + formatter.format(nbt.getInteger("merchantCost")) + " coins&a!"));
        }
        player.sendMessage(message);
        getSold(user).remove(getSold(user).size() - 1);
        event.getInventory().setItem(49, createBuyBack(user));
        
      }
      player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 2);
    } else if (event.getClickedInventory() != null && event.getClickedInventory().equals(event.getWhoClicked().getInventory())) {
      double price = MerchantModule.getInstance().getPriceHandler().getPrice(item);
      Component displayName = item.getItemMeta().displayName();
      Component message;
      if (displayName == null) {
        message =
            ChatUtils.stringToComponentCC("&aYou have sold null &afor &6" + NumberUtil.formatInt((int) price) + " coins&a!");
      }
      else {
        message =
            ChatUtils.stringToComponentCC("&aYou have sold ")
                .append(displayName)
                .append(ChatUtils.stringToComponentCC(" &afor &6"
                    + NumberUtil.formatInt((int) price) + " coins&a!"));
      }
      player.sendMessage(message);
      EconomyManager.getInstance().deposit(player, price);
      nbt = new NBTItem(item);
      nbt.setDouble("merchantCost", price);
      nbt.setBoolean("merchantSold", true);
      getSold(user).add(nbt.getItem());
      event.getInventory().setItem(49, createBuyBack(user));
      event.setCurrentItem(null);
      player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 2);
    }
  }
  
  private ItemStack buildShopOption(ItemStack item, int amount, Player player, Gui gui) {
    ItemStack clone = item.clone();
    ItemMeta meta = clone.getItemMeta();
    List<Component> lore = meta.lore();
    
    NBTItem nbt = new NBTItem(clone);
    
    int costForOne = nbt.getInteger("merchantCost");
    
    Component displayName = meta.displayName();
    assert displayName != null;
    meta.displayName(displayName.append(ChatUtils.stringToComponentCC(" &7x" + amount)));
    
    assert lore != null;
    for (int i = 0; i < 7; i++) {
      lore.remove(lore.size() - 1);
    }
    
    lore.add(ChatUtils.stringToComponentCC(" "));
    lore.add(ChatUtils.stringToComponentCC("&7Cost"));
    lore.add(ChatUtils.stringToComponentCC("&6" + (costForOne * amount) + " coins"));
    lore.add(ChatUtils.stringToComponentCC(" "));
    lore.add(ChatUtils.stringToComponentCC("&eClick to purchase!"));
    meta.lore(lore);
    clone.setItemMeta(meta);
    
    clone.setAmount(amount);
    
    gui.getClickEvents().put(meta.displayName(), () -> {
      if (EconomyManager.getInstance().hasEnough(player, costForOne * amount)) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        formatter.setGroupingUsed(true);
        
        EconomyManager.getInstance().withdraw(player, costForOne * amount);
        
        XItemStack.giveOrDrop(player, new ItemStack(item.getType(), amount));
        
        Component displayName1 = meta.displayName();
        Component message1;
        if (displayName1 == null) {
          message1 = ChatUtils.stringToComponentCC("&aYou have purchased null &afor " + formatter.format((long) costForOne * amount) + " coins&a!");
        }
        else {
          message1 =
              ChatUtils.stringToComponentCC("&aYou have purchased ")
                  .append(displayName1)
                  .append(ChatUtils.stringToComponentCC(" &afor " + formatter.format((long) costForOne * amount) + " coins&a!"));
        }
        player.sendMessage(message1);
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 2);
      }
    });
    
    return clone;
  }
  
  // Utility methods
  
  public ArrayList<Object> spawnNpc(Location location, String name, String skinValue, String skinSignature, boolean skin, boolean look, boolean villager, Villager.Profession profession) {
    NPC npc = CitizensAPI.getNPCRegistry().createNPC(villager ? EntityType.VILLAGER : EntityType.PLAYER, "");
    
    npc.spawn(location);
    
    npc.getEntity().setCustomNameVisible(false);
    
    npc.getEntity().setMetadata("createdAt", new FixedMetadataValue(Main.getInstance(), System.currentTimeMillis()));
    
    if (villager) ((Villager) npc.getEntity()).setProfession(profession);
    
    npc.getEntity().getLocation().setDirection(location.getWorld().getSpawnLocation().toVector().subtract(location.toVector()).normalize());
    
    NPC standNPC = CitizensAPI.getNPCRegistry().createNPC(EntityType.ARMOR_STAND, name, npc.getEntity().getLocation().add(0, !villager ? 1.95 : 2.15, 0));
    standNPC.spawn(npc.getEntity().getLocation().add(0, !villager ? 1.95 : 2.15, 0));
    
    ArmorStandTrait stand = standNPC.getOrAddTrait(ArmorStandTrait.class);
    stand.setGravity(false);
    stand.setVisible(false);
    stand.setMarker(true);
    
    standNPC.getEntity().teleport(npc.getEntity().getLocation().add(0, !villager ? 1.95 : 2.15, 0));
    standNPC.getEntity().setMetadata("merchant", new FixedMetadataValue(Main.getInstance(), true));
    standNPC.getEntity().setMetadata("merchantName", new FixedMetadataValue(Main.getInstance(), name));
    standNPC.getEntity().setMetadata("NPC", new FixedMetadataValue(Main.getInstance(), true));
    
    NPC clickNPC = CitizensAPI.getNPCRegistry().createNPC(EntityType.ARMOR_STAND, ChatColor.YELLOW + "" + ChatColor.BOLD + "CLICK", npc.getEntity().getLocation().add(0, !villager ? 1.6 : 1.8, 0));
    clickNPC.spawn(npc.getEntity().getLocation().add(0, !villager ? 1.6 : 1.8, 0));
    
    ArmorStandTrait click = clickNPC.getOrAddTrait(ArmorStandTrait.class);
    click.setGravity(false);
    click.setVisible(false);
    click.setMarker(true);
    
    clickNPC.getEntity().teleport(npc.getEntity().getLocation().add(0, !villager ? 1.7 : 1.8, 0));
    
    if (skin) {
      SkinTrait skinTrait = npc.getOrAddTrait(SkinTrait.class);
      skinTrait.setSkinPersistent("npc", skinSignature, skinValue);
      
      npc.addTrait(skinTrait);
    }
    
    npc.data().set(NPC.NAMEPLATE_VISIBLE_METADATA, false);
    
    if (look) {
      LookClose lookClose = npc.getOrAddTrait(LookClose.class);
      lookClose.lookClose(true);
      
      npc.addTrait(lookClose);
    }
    
    Chunk chunk = npc.getEntity().getLocation().getChunk();
    chunk.load();
    
    return new ArrayList<>(Arrays.asList(npc, stand, click));
  }
  
  private ItemStack buildCloseButton() {
    NBTItem item = new NBTItem(new ItemBuilder(Material.BARRIER).setDisplayName("&cClose").toItemStack());
    
    item.setBoolean("close", true);
    
    return item.getItem();
  }
  
  public ItemStack buildBackButton(List<Component> lore) {
    NBTItem item = new NBTItem(new ItemBuilder( Material.ARROW)
        .setDisplayName("&aGo Back")
        .addLore(lore).toItemStack());
    
    item.setBoolean("back", true);
    
    return item.getItem();
  }
}
