package me.lagggpixel.core.modules.bazaar.api.bazaar.orders;

public interface FillResult {
    int getAmount();

    double getPrice();

    void undoFill();
}
