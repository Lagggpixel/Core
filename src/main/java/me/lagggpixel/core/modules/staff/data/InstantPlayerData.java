package me.lagggpixel.core.modules.staff.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@Getter
@Setter
public class InstantPlayerData implements ConfigurationSerializable {

  @SerializedName("Health")
  @Expose
  double health;
  @SerializedName("FoodLevel")
  @Expose
  int foodLevel;
  @SerializedName("Saturation")
  @Expose
  float saturation;

  @SerializedName("GameMode")
  @Expose
  GameMode gameMode;
  @SerializedName("IsFlying")
  @Expose
  boolean isFlying;
  @SerializedName("IsSleepingIgnored")
  @Expose
  boolean isSleepingIgnored;
  @SerializedName("AffectsSpawning")
  @Expose
  boolean affectsSpawning;

  @SerializedName("TotalExperience")
  @Expose
  int totalExperience;

  @SerializedName("Inventory")
  @Expose
  ItemStack[] inventory;
  @SerializedName("Armour")
  @Expose
  ItemStack[] armour;

  public InstantPlayerData(Player player) {
    this.health = player.getHealth();
    this.foodLevel = player.getFoodLevel();
    this.saturation = player.getSaturation();

    this.gameMode = player.getGameMode();
    this.isFlying = player.isFlying();
    this.isSleepingIgnored = player.isSleepingIgnored();
    this.affectsSpawning = player.getAffectsSpawning();

    totalExperience = player.getTotalExperience();

    this.inventory = player.getInventory().getContents();
    this.armour = player.getInventory().getArmorContents();
  }

  // Add a static method for deserialization
  @SneakyThrows
  @NotNull
  public static InstantPlayerData deserialize(Map<String, Object> map) {
    InstantPlayerData instantPlayerData = new InstantPlayerData();
    if (map == null || map.isEmpty()) {
      return instantPlayerData;
    }
    instantPlayerData.health = Double.parseDouble(String.valueOf(map.get("health")));
    instantPlayerData.foodLevel = Integer.parseInt(String.valueOf(map.get("foodLevel")));
    instantPlayerData.saturation = Float.parseFloat(String.valueOf(map.get("saturation")));
    instantPlayerData.gameMode = GameMode.valueOf((String) map.get("gameMode"));
    instantPlayerData.isFlying = Boolean.parseBoolean(String.valueOf(map.get("isFlying")));
    instantPlayerData.affectsSpawning = Boolean.parseBoolean(String.valueOf(map.get("affectsSpawning")));
    instantPlayerData.isSleepingIgnored = Boolean.parseBoolean(String.valueOf(map.get("isSleepingIgnored")));
    instantPlayerData.totalExperience = Integer.parseInt(String.valueOf(map.get("totalExperience")));

    instantPlayerData.inventory = itemStackArrayFromBase64((String) map.get("playerInventory"));
    instantPlayerData.armour = itemStackArrayFromBase64((String) map.get("playerArmour"));

    return instantPlayerData;
  }

  private static ItemStack[] deserializeItemStackArray(List<Map<String, Object>> serializedArray) {
    ItemStack[] itemStacks = new ItemStack[serializedArray.size()];

    for (int i = 0; i < serializedArray.size(); i++) {
      if (serializedArray.get(i) != null) {
        itemStacks[i] = ItemStack.deserialize(serializedArray.get(i));
      } else {
        itemStacks[i] = null;
      }
    }

    return itemStacks;
  }

  /**
   * Converts the player inventory to a String array of Base64 strings. First string is the content and second string is the armor.
   *
   * @param playerInventory to turn into an array of strings.
   * @return Array of strings: [ main content, armor content ]
   */
  public static String[] playerInventoryToBase64(PlayerInventory playerInventory) throws IllegalStateException {
    //get the main content part, this doesn't return the armor
    String content = toBase64(playerInventory);
    String armor = itemStackArrayToBase64(playerInventory.getArmorContents());

    return new String[]{content, armor};
  }

  /**
   * A method to serialize an {@link ItemStack} array to Base64 String.
   * <p>
   * Based off of {@link #toBase64(Inventory)}.
   *
   * @param items to turn into a Base64 String.
   * @return Base64 string of the items.
   * @throws IllegalStateException
   */
  public static String itemStackArrayToBase64(ItemStack[] items) throws IllegalStateException {
    try {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

      // Write the size of the inventory
      dataOutput.writeInt(items.length);

      // Save every element in the list
      for (ItemStack item : items) {
        dataOutput.writeObject(item);
      }

      // Serialize that array
      dataOutput.close();
      return Base64Coder.encodeLines(outputStream.toByteArray());
    } catch (Exception e) {
      throw new IllegalStateException("Unable to save item stacks.", e);
    }
  }

  /**
   * A method to serialize an inventory to Base64 string.
   *
   * @param inventory to serialize
   * @return Base64 string of the provided inventory
   */
  public static String toBase64(Inventory inventory) throws IllegalStateException {
    try {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

      // Write the size of the inventory
      dataOutput.writeInt(inventory.getSize());

      // Save every element in the list
      for (int i = 0; i < inventory.getSize(); i++) {
        dataOutput.writeObject(inventory.getItem(i));
      }

      // Serialize that array
      dataOutput.close();
      return Base64Coder.encodeLines(outputStream.toByteArray());
    } catch (Exception e) {
      throw new IllegalStateException("Unable to save item stacks.", e);
    }
  }

  /**
   * A method to get an {@link Inventory} from an encoded, Base64, string.
   *
   * @param data Base64 string of data containing an inventory.
   * @return Inventory created from the Base64 string.
   */
  public static Inventory fromBase64(String data) throws IOException {
    try {
      ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
      BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
      Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt());

      // Read the serialized inventory
      for (int i = 0; i < inventory.getSize(); i++) {
        inventory.setItem(i, (ItemStack) dataInput.readObject());
      }

      dataInput.close();
      return inventory;
    } catch (ClassNotFoundException e) {
      throw new IOException("Unable to decode class type.", e);
    }
  }

  /**
   * Gets an array of ItemStacks from Base64 string.
   * <p>
   * Base off of {@link #fromBase64(String)}.
   *
   * @param data Base64 string to convert to ItemStack array.
   * @return ItemStack array created from the Base64 string.
   */
  public static ItemStack[] itemStackArrayFromBase64(String data) throws IOException {
    try {
      ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
      BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
      ItemStack[] items = new ItemStack[dataInput.readInt()];

      // Read the serialized inventory
      for (int i = 0; i < items.length; i++) {
        items[i] = (ItemStack) dataInput.readObject();
      }

      dataInput.close();
      return items;
    } catch (ClassNotFoundException e) {
      throw new IOException("Unable to decode class type.", e);
    }
  }

  public void restorePlayerData(Player player) {
    player.setHealth(health);
    player.setFoodLevel(foodLevel);
    player.setSaturation(saturation);

    player.setGameMode(gameMode);
    player.setFlying(isFlying);
    player.setSleepingIgnored(isSleepingIgnored);
    player.setAffectsSpawning(affectsSpawning);

    player.setTotalExperience(totalExperience);

    player.getInventory().setArmorContents(armour);
    player.getInventory().setContents(inventory);

    player.getInventory().setArmorContents(armour);
    player.getInventory().setContents(inventory);
  }

  @Override
  public @NotNull Map<String, Object> serialize() {
    Map<String, Object> data = new HashMap<>();

    data.put("health", health);
    data.put("foodLevel", foodLevel);
    data.put("saturation", saturation);
    data.put("gameMode", gameMode.toString());
    data.put("isFlying", isFlying);
    data.put("affectsSpawning", affectsSpawning);
    data.put("isSleepingIgnored", isSleepingIgnored);
    data.put("totalExperience", totalExperience);

    data.put("inventory", itemStackArrayToBase64(inventory));
    data.put("armour", itemStackArrayToBase64(armour));

    return data;
  }
}