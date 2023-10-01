package me.lagggpixel.core.modules.bazaar.interfaces;

import java.util.List;

public interface BazaarItemData {

    double getProductAmount();

    double getBuyPrice();
    double getSellPrice();

    BazaarItemVolume getBuyVolume();
    int getLast7dInstantBuyVolume();

    BazaarItemVolume getSellVolume();
    int getLast7dInstantSellVolume();

    List<BazaarOffer> getOrders();
    List<BazaarOffer> getOffers();

    interface BazaarItemVolume {
        double getAmount();
        double getVolume();
    }

}
