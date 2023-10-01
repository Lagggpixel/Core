package me.lagggpixel.core.modules.bazaar.interfaces;

import me.lagggpixel.core.modules.bazaar.escrow.Escrow;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public interface Bazaar {

    String ITEMS_PATH = "items.json";
    String FILE_NAME = "bazaar.yml";

    double BAZAAR_TAX = 1.1;

    class BazaarItemNotFoundException extends Exception {

        public BazaarItemNotFoundException(String message) {
            super(message);
        }

        public BazaarItemNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }

    }

    class BazaarIOException extends Exception {

        public BazaarIOException(String message) {
            super(message);
        }

        public BazaarIOException(String message, Throwable cause) {
            super(message, cause);
        }

    }

    YamlConfiguration getBazaarConfig();

    Escrow getEscrow();

    List<BazaarCategory> getCategories();
    List<BazaarSubItem> getRawItems();

    void setCategories(List<BazaarCategory> categories);
    void setRawItems(List<BazaarSubItem> rawItems);

    BazaarItemData getItemData(String name) throws BazaarItemNotFoundException;

    void set(String path, Object value) throws BazaarIOException;
    Object get(String path);

    <T> T get(String path, Class<T> clazz);

    default int getCategoryAmount() {
        return this.getCategories().size();
    }

}
