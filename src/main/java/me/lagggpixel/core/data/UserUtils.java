package me.lagggpixel.core.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.lagggpixel.core.Main;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class UserUtils {

    private final static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final String path = Main.getInstance().getDataFolder() + "data/users/";

    public static @NotNull HashMap<UUID, User> loadData() {
        HashMap<UUID, User> userdata = new HashMap<>();
        File folder = new File(path);

        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();

            assert files != null;
            for (File file : files) {
                if (file.isFile()) {

                    try (FileReader reader = new FileReader(file)) {
                        String name = file.getName();
                        int pos = name.lastIndexOf(".");
                        UUID uuid;
                        if (pos > 0) {
                            uuid = UUID.fromString(name.substring(0, pos));
                        }
                        else {
                            uuid = UUID.fromString(name);
                        }
                        User user  = gson.fromJson(reader, User.class);
                        userdata.put(uuid, user);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        return userdata;
    }


    public static void saveData(@NotNull HashMap<UUID, User> data) {
        data.forEach(((uuid, user) -> {

            File dataFile = new File(path, uuid + ".json");

            try (FileWriter writer = new FileWriter(dataFile)) {
                gson.toJson(user, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

    }
}
