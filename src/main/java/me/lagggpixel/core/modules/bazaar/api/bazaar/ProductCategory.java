package me.lagggpixel.core.modules.bazaar.api.bazaar;

import me.lagggpixel.core.modules.bazaar.api.menu.MenuInfo;
import me.zort.containr.GUI;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface ProductCategory extends MenuInfo {
    ItemStack getIcon();

    void setIcon(ItemStack icon);

    ItemStack getRawIcon();

    String getName();

    void setName(String name);

    List<Product> getProducts();

    void addProduct(Product product);

    void removeProduct(Product product);

    GUI getMenu();

    GUI getEditMenu();

    Category getCategory();
}
