package me.lagggpixel.core.modules.bazaar;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.interfaces.IModule;
import me.lagggpixel.core.modules.bazaar.api.BazaarAPI;
import me.lagggpixel.core.modules.bazaar.api.bazaar.Bazaar;
import me.lagggpixel.core.modules.bazaar.api.bazaar.orders.OrderManager;
import me.lagggpixel.core.modules.bazaar.api.config.MenuConfig;
import me.lagggpixel.core.modules.bazaar.api.edit.EditManager;
import me.lagggpixel.core.modules.bazaar.api.menu.ClickActionManager;
import me.lagggpixel.core.modules.bazaar.api.menu.ItemPlaceholders;
import me.lagggpixel.core.modules.bazaar.api.menu.MenuHistory;
import me.lagggpixel.core.modules.bazaar.bz.BazaarImpl;
import me.lagggpixel.core.modules.bazaar.bz.orders.SQLOrderManager;
import me.lagggpixel.core.modules.bazaar.commands.BazaarCommand;
import me.lagggpixel.core.modules.bazaar.commands.EditCommand;
import me.lagggpixel.core.modules.bazaar.config.BazaarConfig;
import me.lagggpixel.core.modules.bazaar.config.DatabaseConfig;
import me.lagggpixel.core.modules.bazaar.config.DefaultMenuConfig;
import me.lagggpixel.core.modules.bazaar.edit.DefaultEditManager;
import me.lagggpixel.core.modules.bazaar.menu.DefaultClickActionManager;
import me.lagggpixel.core.modules.bazaar.menu.DefaultItemPlaceholders;
import me.lagggpixel.core.modules.bazaar.menu.DefaultMenuHistory;
import me.lagggpixel.core.modules.bazaar.menu.MenuListeners;
import me.lagggpixel.core.modules.bazaar.messageinput.MessageInputManager;
import me.lagggpixel.core.utils.CommandUtils;
import me.lagggpixel.core.libs.containr.Containr;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("LombokGetterMayBeUsed")
public class BazaarModule implements IModule, BazaarAPI {

  private Economy economy = null;
  private BazaarConfig bazaarConfig;
  private MenuConfig menuConfig;
  private DatabaseConfig databaseConfig;
  private Bazaar bazaar;
  private ClickActionManager clickActionManager;
  private ItemPlaceholders itemPlaceholders;
  private MenuHistory menuHistory;
  private OrderManager orderManager;
  private EditManager editManager;
  private MessageInputManager messageInputManager;
  
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
  public void onEnable() {
    RegisteredServiceProvider<Economy> rsp = Main.getInstance().getServer().getServicesManager().getRegistration(Economy.class);
    if (rsp == null) {
      Bukkit.getLogger().severe("No registered Vault provider found!");
      Main.getInstance().getServer().getPluginManager().disablePlugin(Main.getInstance());
      return;
    }
    Containr.init(Main.getInstance());

    economy = rsp.getProvider();

    bazaarConfig = new BazaarConfig(Main.getInstance());
    menuConfig = new DefaultMenuConfig(Main.getInstance());
    databaseConfig = new DatabaseConfig(Main.getInstance());

    bazaar = new BazaarImpl(this);

    clickActionManager = new DefaultClickActionManager(this);
    itemPlaceholders = new DefaultItemPlaceholders(this);

    menuHistory = new DefaultMenuHistory(this);

    orderManager = new SQLOrderManager(this);

    editManager = new DefaultEditManager(this);

    messageInputManager = new MessageInputManager(this);
  }
  
  @Override
  public void onDisable() {
  
  }
  
  @Override
  public void registerCommands() {
    CommandUtils.registerCommand(new BazaarCommand(this));
    CommandUtils.registerCommand(new EditCommand(this));
  }
  
  @Override
  public void registerListeners() {
    Bukkit.getPluginManager().registerEvents(new MenuListeners(this), Main.getInstance());
  }

  @Override
  public JavaPlugin getPlugin() {
    return Main.getInstance();
  }

  @Override
  public Economy getEconomy() {
    return economy;
  }

  public BazaarConfig getBazaarConfig() {
    return bazaarConfig;
  }

  @Override
  public MenuConfig getMenuConfig() {
    return menuConfig;
  }

  public DatabaseConfig getDatabaseConfig() {
    return databaseConfig;
  }

  @Override
  public Bazaar getBazaar() {
    return bazaar;
  }

  @Override
  public ClickActionManager getClickActionManager() {
    return clickActionManager;
  }

  @Override
  public ItemPlaceholders getItemPlaceholders() {
    return itemPlaceholders;
  }

  @Override
  public OrderManager getOrderManager() {
    return orderManager;
  }

  @Override
  public MenuHistory getMenuHistory() {
    return menuHistory;
  }

  @Override
  public EditManager getEditManager() {
    return editManager;
  }

  public MessageInputManager getMessageInputManager() {
    return messageInputManager;
  }
}
