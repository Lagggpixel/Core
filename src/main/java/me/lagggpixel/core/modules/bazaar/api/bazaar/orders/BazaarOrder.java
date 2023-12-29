package me.lagggpixel.core.modules.bazaar.api.bazaar.orders;

import me.lagggpixel.core.modules.bazaar.api.bazaar.Product;
import me.lagggpixel.core.modules.bazaar.api.menu.MenuInfo;
import org.bukkit.inventory.ItemStack;

import java.time.Instant;
import java.util.UUID;

public interface BazaarOrder extends MenuInfo {
    Product getProduct();

    int getAmount();

    double getUnitPrice();

    OrderType getType();

    UUID getPlayer();

    int getFilled();

    Instant getCreatedAt();

    void fill(int amount);

    int getClaimed();

    void claim(int amount);

    boolean isSimilar(BazaarOrder other);

    ItemStack getIcon();

    double getAvailableCoins();

    int getOrderableItems();

    int getAvailableItems();

    int getDatabaseId();
}
