package me.lagggpixel.core.data;

import lombok.Getter;
import me.lagggpixel.core.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@Getter
public enum Lang {
  
  PREFIX("prefix", "&#fb0000I&#f4011bn&#ec0335f&#e50450i&#de056bn&#d60685i&#cf08a0t&#c709bae &#c00ad5M&#b90bf0i&#b216fbn&#ac2bf7e&#a63ff3c&#a054efr&#9a68eba&#947de6f&#8e91e2t&#88a6dee&#82badar&#7ccfd6s &r&7» &r"),
  PLAYER_ONLY("player_only", "&cThis command is for players only."),
  INVALID_USAGE("invalid_usage", "&cInvalid usage. Type /help for more information."),
  PLAYER_NOT_FOUND("player_not_found", "&c%player% is not found."),
  
  TELEPORTATION_IN_TIME("teleportation.time", "&aTeleporting in %time% seconds..."),
  TELEPORTATION_CANCELED("teleportation.canceled", "&cTeleportation canceled due to %reason%."),
  TELEPORTATION_SUCCESS("teleportation.success", "You have been teleported to %name%."),
  
  HOME_ALREADY_EXIST("home.already_exist", "&cA home named '&e%home%&c' already exists."),
  HOME_DOES_NOT_EXIST("home.does_not_exist", "&cThe home '&e%home%&c' does not exist."),
  HOME_CREATED("home.created", "&aYou've successfully created a home called '&e%home%&a'."),
  HOME_LIMIT_REACHED("home.limit_reached", "&cYou cannot create more homes."),
  HOME_DELETED("home.deleted", "&aYou've deleted the home '&e%home%&a'."),
  HOME_NAME_INVALID("home.invalid_name", "&cPlease create a home with a different name. (Home names cannot start with numbers and cannot contain special characters)"),
  
  INVENTORY_CLEARED_SELF("inventory.cleared.self", "&aYou have cleared your inventory."),
  INVENTORY_CLEARED_OTHER("inventory.cleared.other", "&aYou have cleared %player%'s inventory."),
  INVENTORY_OPENED_SELF("inventory.opened.self", "&aYou have opened your own inventory."),
  INVENTORY_OPENED_OTHER("inventory.opened.other", "&aYou have opened %player%'s inventory"),
  INVENTORY_CLONED_PUBLIC("inventory.cloned.public", "&cYou have cloned %player%'s inventory to your own."),
  INVENTORY_CLONED_SILENT("inventory.cloned.silent", "&cYou have silently cloned %player%'s inventory"),
  
  SPAWN_NAME("spawn.name", "spawn"),
  SPAWN_NO_SET_SPAWN("spawn.no_set_spawn", "&aNo spawn is set, therefore you cannot teleport to spawn,"),
  SPAWN_SUCCESSFULLY_SET("spawn.successfully_set", "&aYou have successfully set the new spawn location to your current location"),
  
  SN_WORLD_NO_OVERWORLD("skipnight.world_not_overworld", "&cYou must be in the overworld to start a vote!"),
  SN_IN_BED_VOTED_YES("skipnight.in_bed_voted_yes", "&aYou are now in bed, automatically voting yes."),
  SN_IN_BED_NO_VOTE_IN_PROGRESS("skipnight.in_bed_no_vote_in_progress", "&aStart a vote to skip the night?"),
  SN_VOTED_SELF("skipnight.voted_self", "&aYou voted &9&l%status% &r&a."),
  SN_AFK("skipnight.afk", "&aYou are currently afk, your vote will not count."),
  SN_BACK_FROM_AFK("skipnight.back_from_afk", "&aWelcome back from afk."),
  SN_VOTE_STARTED("skipnight.vote_started", "&a%player% has started a vote to skip the night."),
  SN_LEFT_WORLD("skipnight.left_world", "&aYou left the world, your vote will not count."),
  SN_ALREADY_VOTED("skipnight.already_voted", "&cYou have already voted!"),
  SN_CAN_ONLY_VOTE_AT_NIGHT("skipnight.can_only_vote_at_night", "&aYou can only start a vote at night!"),
  SN_NO_VOTE_WHILE_AFK("skipnight.no_vote_while_afk", "You cannot start a vote while afk!"),
  SN_VOTE_ALREADY_IN_PROGRESS("skipnight.vote_already_in_progress", "&aVote already in progress!"),
  SN_TEN_SECOND_LEFT("skipnight.ten_seconds_left", "&9&l10 &r&aseconds left to vote!"),
  SN_ALREADY_DAY_BOSS_BAR("skipnight.boss_bar.already_night", "It is already day time!"),
  SN_VOTE_PASSED_BOSS_BAR("skipnight.boss_bar.vote_passed", "Vote passed!"),
  SN_VOTE_FAILED_BOSS_BAR("skipnight.boss_bar.vote_failed", "Vote failed!"),
  SN_ALL_PLAYERS_VOTED_BOSS_BAR("skipnight.boss_bar.all_players_voted", "All players have voted!"),
  
  RTP_NO_SAFE_LOCATION("rtp.no_safe_location", "&aNo safe location was found, please try again."),
  RTP_ATTEMPTING_TO_FIND_LOCATION("rtp.attempting_to_find_location", "&aAttempting to find a location. Attempt %num% out of %max%.");
  
  /**
   * -- GETTER --
   * Get the path to the string.
   */
  private final String path;
  private final String def;
  private static YamlConfiguration LANG;
  
  /**
   * Lang enum constructor.
   *
   * @param path  The string path.
   * @param start The default string.
   */
  Lang(String path, String start) {
    this.path = path;
    this.def = start;
  }
  
  /**
   * Set the {@code YamlConfiguration} to use.
   *
   * @param config The config to set.
   */
  public static void setFile(YamlConfiguration config) {
    LANG = config;
  }
  
  public TextComponent toTextComponent() {
    return ChatUtils.stringToComponent(LANG.getString(this.path, def));
  }
  
  public String toString() {
    return LANG.getString(this.path, def);
  }
  
  public Component toComponentWithPrefix(@Nullable Map<String, String> placeholders) {
    
    String[] var1 = {Lang.LANG.getString(Lang.PREFIX.path, Lang.PREFIX.def) + LANG.getString(this.path, def)};
    if (placeholders != null) {
      placeholders.forEach((k, v) -> var1[0] = var1[0].replace(k, v));
    }
    
    return ChatUtils.stringToComponentCC(var1[0]);
  }
  
  /**
   * Get the default value of the path.
   *
   * @return The default value of the path.
   */
  public String getDefault() {
    return this.def;
  }
  
}