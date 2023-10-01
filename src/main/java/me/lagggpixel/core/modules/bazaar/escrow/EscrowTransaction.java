package me.lagggpixel.core.modules.bazaar.escrow;

import me.lagggpixel.core.modules.bazaar.interfaces.Bazaar;
import me.lagggpixel.core.modules.bazaar.interfaces.BazaarSubItem;
import org.bukkit.OfflinePlayer;

import java.util.UUID;
import java.util.function.Consumer;

public interface EscrowTransaction {

    Bazaar getBazaar();

    UUID getUuid();

    OfflinePlayer getSeller();
    OfflinePlayer getBuyer();
    int getAmount();
    double getPrice();
    Escrow.TransactionType getType();
    BazaarSubItem getSubItem();

    Consumer<EscrowTransaction> getOnFill();

    void fill(int amount);

    boolean isFilled();

    void cancel();

    boolean isCancelled();

    default boolean isBuyOrder() {
        return this.getType() == Escrow.TransactionType.BUY;
    }

    default boolean isSellOrder() {
        return this.getType() == Escrow.TransactionType.SELL;
    }

}
