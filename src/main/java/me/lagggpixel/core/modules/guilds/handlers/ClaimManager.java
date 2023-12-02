package me.lagggpixel.core.modules.guilds.handlers;

import lombok.Getter;
import me.lagggpixel.core.Main;
import me.lagggpixel.core.modules.guilds.data.Claim;
import me.lagggpixel.core.modules.guilds.data.Guild;
import me.lagggpixel.core.utils.ChatUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

@Getter
public class ClaimManager {
  private final HashMap<UUID, Guild> claiming = new HashMap<>();
  private final HashSet<Claim> claims = new HashSet<>();
  private final NamespacedKey wandNameSpacedKey = new NamespacedKey(Main.getInstance(), "claiming_wand");


  public Claim getClaimAt(Location location) {
    for (Claim claim : claims) {
      if (claim.isInside(location, false)) {
        return claim;
      }
    }
    return null;
  }

  public ItemStack getWand() {

    ItemStack stack = new ItemStack(Material.GOLDEN_HOE);

    ItemMeta meta = stack.getItemMeta();

    meta.displayName(ChatUtils.stringToComponentCC("&aClaiming Wand"));

    meta.lore(List.of(
        ChatUtils.stringToComponentCC("&7This is the claiming wand."),
        ChatUtils.stringToComponentCC("&aLeft-click the ground&7 to set the first position."),
        ChatUtils.stringToComponentCC("&aRight-click the ground&7 to set the second position."),
        ChatUtils.stringToComponentCC("&aShift and left-click&7 to claim land after setting points."),
        ChatUtils.stringToComponentCC("&aRight-click the air twice&7 to clear your selection.")
    ));
    meta.getPersistentDataContainer().set(wandNameSpacedKey, PersistentDataType.STRING, "CLAIMING_WAND");

    stack.setItemMeta(meta);

    return stack;
  }


  public boolean isWand(ItemStack stack) {
    if (stack == null) {
      return false;
    }
    String data = stack.getItemMeta().getPersistentDataContainer().get(wandNameSpacedKey, PersistentDataType.STRING);
    return data != null && data.equals("CLAIMING_WAND");
  }

}