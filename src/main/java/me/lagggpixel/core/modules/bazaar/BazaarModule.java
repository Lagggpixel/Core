package me.lagggpixel.core.modules.bazaar;

import lombok.Getter;
import me.lagggpixel.core.Main;
import me.lagggpixel.core.modules.Module;
import me.lagggpixel.core.modules.bazaar.impl.SkyblockBazaar;
import me.lagggpixel.core.modules.bazaar.interfaces.Bazaar;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class BazaarModule extends Module {

    @Getter
    private static Bazaar bazaar;

    @NotNull
    @Override
    public String getId() {
        return "bazaar";
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void initialize() {
        try {
            bazaar = new SkyblockBazaar();
        } catch (Bazaar.BazaarIOException | Bazaar.BazaarItemNotFoundException ex) {
            Main.log(Level.SEVERE, "&cFailed to initialize bazaar: &8" + ex.getMessage());
        }
    }

    @Override
    public void registerCommands() {

    }

    @Override
    public void registerListeners() {

    }

}
