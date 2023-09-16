package me.lagggpixel.core.modules.spawn.managers;

import lombok.Getter;
import me.lagggpixel.core.Main;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class SpawnManager {

    private final File dataFolder = new File(Main.getInstance().getDataFolder(), "data/modules/spawn");
    private final String dataFileName = "spawn_data.yml";
    private final File file = new File(dataFolder, dataFileName);

    @Getter
    private Location spawnLocation;

    public void loadSpawnLocation() {

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        spawnLocation = config.getSerializable("location", Location.class);

    }

    public void saveSpawnLocation() {
        if (spawnLocation == null) {
            return;
        }

        try {
            if (!dataFolder.exists()) {
                dataFolder.mkdir();
            }
            if (!file.exists()) {
                file.createNewFile();
            }

            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

            config.set("location", spawnLocation);
            config.save(file);

        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
