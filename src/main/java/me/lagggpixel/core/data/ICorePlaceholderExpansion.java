package me.lagggpixel.core.data;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.lagggpixel.core.Main;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public abstract class ICorePlaceholderExpansion extends PlaceholderExpansion {
  
  protected String version = Main.getInstance().getPluginMeta().getVersion();
  
  @Override
  public abstract @NotNull String getIdentifier();

  @Override
  public @NotNull String getAuthor() {
    return "Lagggpixel";
  }

  @Override
  public @NotNull String getVersion() {
    return version;
  }

  @Override
  public abstract String onRequest(OfflinePlayer player, @NotNull String params);
}
