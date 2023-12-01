package me.lagggpixel.core.modules.guilds.managers;


import lombok.Getter;
import me.lagggpixel.core.modules.guilds.data.Claim;
import me.lagggpixel.core.utils.ChatUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashSet;
import java.util.List;

@Getter
public class ClaimManager {
  @Getter
  private static final HashSet<Claim> claims = new HashSet<>();

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

    stack.setItemMeta(meta);

    return stack;
  }


  public boolean isWand(ItemStack stack) {

    return stack != null
        && stack.getType() == getWand().getType() && getWand().getItemMeta() != null
        && stack.getItemMeta() != null && stack.getItemMeta().hasDisplayName()
        && stack.getItemMeta().displayName() == getWand().getItemMeta().displayName()
        && stack.getItemMeta().lore() != null;

  }

}