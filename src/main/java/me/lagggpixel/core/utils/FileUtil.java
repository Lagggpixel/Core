/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.utils;

import me.lagggpixel.core.Main;

import java.io.*;
import java.util.logging.Level;

/**
 *  @author    Lagggpixel
 * @since January 27, 2024 January 22, 2024
 */
public class FileUtil {
  
  public static void copyToDefault(String resourcePath) {
    resourcePath = resourcePath.replace('\\', '/');
    InputStream in = Main.getInstance().getResource(resourcePath);
    if (in == null) {
      throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found");
    } else {
      File outFile = new File(Main.getInstance().getDataFolder(), resourcePath);
      int lastIndex = resourcePath.lastIndexOf(47);
      File outDir = new File(Main.getInstance().getDataFolder(), resourcePath.substring(0, Math.max(lastIndex, 0)));
      if (!outDir.exists()) {
        outDir.mkdirs();
      }
      
      try {
        if (outFile.exists()) {
          Main.log(Level.WARNING, "Could not save " + resourcePath + " to " + outFile + " because " + outFile.getName() + " already exists.");
        } else {
          OutputStream out = new FileOutputStream(outFile);
          byte[] buf = new byte[1024];
          
          int len;
          while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
          }
          
          out.close();
          in.close();
        }
      } catch (IOException var10) {
        Main.log(Level.SEVERE, "Could not save " + outFile.getName() + " to " + outFile);
        ExceptionUtils.handleException(var10);
      }
    }
  }

  
}
