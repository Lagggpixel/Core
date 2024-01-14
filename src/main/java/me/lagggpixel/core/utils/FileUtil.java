package me.lagggpixel.core.utils;

import me.lagggpixel.core.Main;

import java.io.*;
import java.util.logging.Level;

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
