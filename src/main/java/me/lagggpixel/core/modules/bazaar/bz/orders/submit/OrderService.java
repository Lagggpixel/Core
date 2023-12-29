package me.lagggpixel.core.modules.bazaar.bz.orders.submit;

import me.lagggpixel.core.modules.bazaar.api.bazaar.orders.BazaarOrder;
import me.lagggpixel.core.modules.bazaar.api.bazaar.orders.InstantBazaarOrder;
import me.lagggpixel.core.modules.bazaar.api.bazaar.orders.InstantSubmitResult;
import me.lagggpixel.core.modules.bazaar.api.bazaar.orders.SubmitResult;

public interface OrderService {
    InstantSubmitResult submit(InstantBazaarOrder order);

    SubmitResult submit(BazaarOrder order);

    int claim(BazaarOrder order);

    int claim(InstantBazaarOrder order);
}
