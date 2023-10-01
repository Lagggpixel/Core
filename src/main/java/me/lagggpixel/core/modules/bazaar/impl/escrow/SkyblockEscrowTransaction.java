package me.lagggpixel.core.modules.bazaar.impl.escrow;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.lagggpixel.core.modules.bazaar.escrow.Escrow;
import me.lagggpixel.core.modules.bazaar.escrow.EscrowTransaction;
import me.lagggpixel.core.modules.bazaar.interfaces.Bazaar;
import me.lagggpixel.core.modules.bazaar.interfaces.BazaarSubItem;
import org.bukkit.OfflinePlayer;

import java.util.UUID;
import java.util.function.Consumer;

@Data
@AllArgsConstructor
public class SkyblockEscrowTransaction implements EscrowTransaction {

    private final Bazaar bazaar;

    private final UUID uuid;

    private final OfflinePlayer seller;
    private final OfflinePlayer buyer;
    private final double price;
    private int amount;

    private final BazaarSubItem subItem;

    private final Escrow.TransactionType type;

    private final Consumer<EscrowTransaction> onFill;

    private boolean cancelled;
    private boolean filled;

    @Override
    public void fill(int amount) {
        if (this.cancelled) return;

        this.amount -= amount;

        if (this.amount <= 0) {
            this.filled = true;
            this.onFill.accept(this);

            this.cancel();
        }
    }

    @Override
    public void cancel() {
        this.cancelled = true;

        this.bazaar.getEscrow().removeTransaction(this.getUuid());
    }

}
