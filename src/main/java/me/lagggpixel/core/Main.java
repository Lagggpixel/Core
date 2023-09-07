package me.lagggpixel.core;

import me.lagggpixel.core.data.DelayTeleport;
import me.lagggpixel.core.data.Lang;
import me.lagggpixel.core.data.User;
import me.lagggpixel.core.data.UserUtils;
import me.lagggpixel.core.listeners.onPlayerJoin;
import me.lagggpixel.core.modules.Module;
import me.lagggpixel.core.modules.homes.HomeModule;
import me.lagggpixel.core.modules.spawn.SpawnModule;
import me.lagggpixel.core.utils.TeleportUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public final class Main extends JavaPlugin {

    private static Main INSTANCE;
    private static HashMap<UUID, User> userData;

    private final Map<UUID, DelayTeleport> delayTeleportMap = new HashMap<>();

    private final @NotNull HashMap<String, Module> modules = new HashMap<>();
    private final @NotNull Module homeModule = new HomeModule();
    private final @NotNull Module spawnModule = new SpawnModule();

    @Override
    public void onEnable() {

        INSTANCE = this;

        this.loadLangConfig();

        userData = UserUtils.loadData();

        registerListeners();

        modules.put(homeModule.getId(), homeModule);
        modules.put(spawnModule.getId(), spawnModule);

        modules.forEach((k, v) -> {
            v.initialize();
            v.registerCommands();
            v.registerListeners();
        });

    }

    @Override
    public void onDisable() {
        UserUtils.saveData(userData);
    }

    public static @Nonnull Main getInstance() {
        return INSTANCE;
    }

    public static @Nonnull HashMap<UUID, User> getUserData() {
        return userData;
    }

    public static User getUser(UUID uuid) {
        return userData.get(uuid);
    }

    public static @NotNull PluginManager getPluginManager() {
        return INSTANCE.getServer().getPluginManager();
    }

    public Map<UUID, DelayTeleport> getDelayTeleportMap() {
        return delayTeleportMap;
    }

    private void loadLangConfig() {
        File langFile = new File(getDataFolder(), "lang.yml");
        if (!langFile.exists()) {
            try {
                getDataFolder().mkdir();
                langFile.createNewFile();
                InputStream defConfigStream = this.getResource("lang.yml");
                if (defConfigStream != null) {
                    YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(langFile);
                    defConfig.save(langFile);
                    Lang.setFile(defConfig);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Main.getInstance().getServer().getLogger().severe("Couldn't create language file.");
                Main.getInstance().getServer().getLogger().severe("This is a fatal error. Now disabling");
                this.onDisable();
            }
        }
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(langFile);
        for (Lang item : Lang.values()) {
            if (conf.getString(item.getPath()) == null) {
                conf.set(item.getPath(), item.getDefault());
            }
        }
        Lang.setFile(conf);
        try {
            conf.save(langFile);
        } catch (IOException e) {
            Main.getInstance().getServer().getLogger().log(Level.WARNING, "Failed to save lang.yml.");
            Main.getInstance().getServer().getLogger().log(Level.WARNING, "Report this stack trace to the plugin creator.");
            e.printStackTrace();
        }
    }

    private void registerListeners() {
        new onPlayerJoin();

        TeleportUtils.PlayerTeleportCancelListener listener = new TeleportUtils.PlayerTeleportCancelListener();
        this.getServer().getPluginManager().registerEvents(listener, Main.getInstance());
    }
}
