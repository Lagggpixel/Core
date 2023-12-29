package me.lagggpixel.core.modules.bazaar.bz.orders;

import me.lagggpixel.core.modules.bazaar.api.BazaarAPI;
import me.lagggpixel.core.modules.bazaar.api.bazaar.Product;
import me.lagggpixel.core.modules.bazaar.api.bazaar.orders.InstantBazaarOrder;
import me.lagggpixel.core.modules.bazaar.api.bazaar.orders.OrderType;
import me.lagggpixel.core.modules.bazaar.api.config.MenuConfig;
import me.lagggpixel.core.modules.bazaar.api.config.MessagePlaceholder;
import me.lagggpixel.core.modules.bazaar.utils.Utils;
import me.zort.containr.internal.util.ItemBuilder;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class DefaultInstantBazaarOrder implements InstantBazaarOrder {
    private final Product product;
    private final int amount;
    private final double price;
    private final OrderType type;
    private final UUID player;
    private int realAmount = 0;
    private double realPrice = 0;

    public DefaultInstantBazaarOrder(Product product, int amount, OrderType type, UUID player, double price) {
        this.product = product;
        this.amount = amount;
        this.type = type;
        this.player = player;
        this.price = price;
    }

    @Override
    public Product getProduct() {
        return product;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public OrderType getType() {
        return type;
    }

    @Override
    public UUID getPlayer() {
        return player;
    }

    @Override
    public ItemStack getIcon() {
        BazaarAPI bazaarApi = product.getProductCategory().getCategory().getBazaar().getBazaarApi();
        MenuConfig menuConfig = bazaarApi.getMenuConfig();

        return menuConfig.replaceLorePlaceholders(
                ItemBuilder.newBuilder(product.getItem()).appendLore("%confirm-instant-lore%").build(),
                "confirm-instant-lore",
                new MessagePlaceholder("price", Utils.getTextPrice(price)),
                new MessagePlaceholder("amount", String.valueOf(amount)),
                new MessagePlaceholder("product", product.getName()));
    }

    @Override
    public int getRealAmount() {
        return realAmount;
    }

    @Override
    public void setRealAmount(int realAmount) {
        this.realAmount = realAmount;
    }

    @Override
    public double getRealPrice() {
        return realPrice;
    }

    @Override
    public void setRealPrice(double realPrice) {
        this.realPrice = realPrice;
    }
}
