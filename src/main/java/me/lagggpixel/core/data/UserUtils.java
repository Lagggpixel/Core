package me.lagggpixel.core.data;

import com.google.gson.Gson;
import me.lagggpixel.core.Main;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class UserUtils {

    private static final String PATH = "data/users";
    private static final File parentFile = new File(Main.getInstance().getDataFolder(), PATH);

    @NotNull
    public static Map<UUID, User> loadData() {
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        final var map = new ConcurrentHashMap<UUID, User>();
        final var files = parentFile.listFiles();

        if (files == null) {
            return new ConcurrentHashMap<>();
        }

        for (File file : files) {
            final var name = file.getName().split("\\.")[0];
            final UUID uuid;

            try {
                uuid = UUID.fromString(name);
            } catch (IllegalArgumentException e) {
                continue;
            }

            User data = null;
            try {
                data = getDataFromFile(file);
            } catch (FileNotFoundException e) {
                Main.getInstance().getLogger().log(Level.SEVERE, "[Core]: Failed to load one user file.");
            }

            if (data != null) map.put(uuid, data);
        }

        return map;
    }

    public static void saveData(Map<UUID, User> map) {
        for (User value : map.values()) {
            try {
                setData(value);
            } catch (IOException e) {
                Main.getInstance().getLogger().log(Level.SEVERE, "[Core]: Player data did not save correctly");
            }
        }
    }

    @Nullable
    public static User getDataFromFile(File file) throws FileNotFoundException {
        Gson gson = new Gson();

        Reader reader = new FileReader(file);
        return gson.fromJson(reader, User.class);
    }

    public static void setData(User data) throws IOException {
        Gson gson = new Gson();
        File file = getPlayerFile(data.getPlayerUUID());

        Writer writer = new FileWriter(file, false);

        gson.toJson(data, writer);
        writer.flush();
        writer.close();
    }

    public static File getPlayerFile(UUID player) {
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        return new File(parentFile, player.toString() + ".json");
    }
}
