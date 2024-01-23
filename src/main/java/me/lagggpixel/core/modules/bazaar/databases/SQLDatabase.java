/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by external developers.
 *
 * You are hereby granted the right to view, copy, edit, distribute the code.
 */

package me.lagggpixel.core.modules.bazaar.databases;

import java.sql.Connection;

/**
 *  @author    Lagggpixel
 *  </p>
 *  Created on January 22, 2024
 */
public interface SQLDatabase {
    Connection getConnection();

    boolean isConnected();

    Type getType();

    enum Type {
        SQLITE("file", SQLiteDatabase.class),
        MYSQL("mysql", MySQLDatabase.class);

        private final String configName;
        private final Class<? extends SQLDatabase> databaseClass;

        Type(String configName, Class<? extends SQLDatabase> databaseClass) {
            this.configName = configName;
            this.databaseClass = databaseClass;
        }

        public String getConfigName() {
            return configName;
        }

        /**
 *  @author    Lagggpixel
 *  </p>
 *  Created on January 22, 2024
 */
public class<? extends SQLDatabase> getDatabaseClass() {
            return databaseClass;
        }
    }
}
