package me.lagggpixel.core.data;

import lombok.Getter;
import me.lagggpixel.core.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.configuration.file.YamlConfiguration;

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
  
  // Bazaar Module
  
  // Chat Module
  CHAT_STAFF_CHAT("chat.staffchat.format", "&c[Staff-Chat] &r&b%sender% &r&7» &r%message%"),
  CHAT_STAFF_CHAT_TOGGLE_ON("chat.staffchat.toggle.on", "&c[Staff-Chat] &aYou have toggled staff chat on."),
  CHAT_STAFF_CHAT_TOGGLE_OFF("chat.staffchat.toggle.off", "&c[Staff-Chat] &aYou have toggled staff chat off."),
  
  // Chat-games Module
  
  // Discord Module
  
  // Economy Module
  ECONOMY_BALANCE_SELF("economy.balance.self", "&aYour balance is &e%balance%&a."),
  ECONOMY_BALANCE_OTHER("economy.balance.other", "&a%player%'s balance is &e%balance%&a."),
  ECONOMY_INVALID_AMOUNT("economy.invalid_amount", "&cThat is not a valid amount. Please provide a valid number."),
  ECONOMY_SET("economy.economy.set", "&aYou have set %player%'s balance to &e%balance%&a."),
  ECONOMY_GIVE("economy.economy.give", "&aYou have given %player% &e%amount%&a. Their new balance is &e%balance%&a."),
  ECONOMY_REMOVE("economy.economy.remove", "&aYou have removed &e%amount%&a from %player%'s balance. Their new balance is &e%balance%&a."),
  ECONOMY_BALTOP_NO_PLAYER_FOUND("economy.baltop.no_player_found", "&cNo players found."),
  ECONOMY_BALTOP_HEADER("economy.baltop.header", "&aTop Players by Balance:"),
  ECONOMY_BALTOP_LISTING("economy.baltop.listing", "&a%position%. %player%: &e%balance%&a"),
  
  // Home Module
  HOME_ALREADY_EXIST("home.already_exist", "&cA home named '&e%home%&c' already exists."),
  HOME_DOES_NOT_EXIST("home.does_not_exist", "&cThe home '&e%home%&c' does not exist."),
  HOME_CREATED("home.created", "&aYou've successfully created a home called '&e%home%&a'."),
  HOME_LIMIT_REACHED("home.limit_reached", "&cYou cannot create more homes."),
  HOME_DELETED("home.deleted", "&aYou've deleted the home '&e%home%&a'."),
  HOME_NAME_INVALID("home.invalid_name", "&cPlease create a home with a different name. (Home names cannot start with numbers and cannot contain special characters)"),
  HOME_CREATED_OTHER_PLAYER("home.admin.created_other_player", "&aYou have created a home for %player%, named '&e%home%&a'."),
  HOME_DELETED_OTHER_PLAYER("home.admin.deleted_other_player", "&aYou have deleted %player%'s home, named '&e%home%&a'."),
  TELEPORTED_TO_HOME_OTHER_PLAYER("home.admin.teleported_to_home_other_player", "&aYou have teleported to %player%'s home, named '&e%home%&a'."),
  NO_HOMES_OTHER_PLAYER("home.admin.no_homes_other_player", "&a%player% does not have any homes."),

  // Inventory Module
  INVENTORY_CLEARED_SELF("inventory.cleared.self", "&aYou have cleared your inventory."),
  INVENTORY_CLEARED_OTHER("inventory.cleared.other", "&aYou have cleared %player%'s inventory."),
  INVENTORY_OPENED_SELF("inventory.opened.self", "&aYou have opened your own inventory."),
  INVENTORY_OPENED_OTHER("inventory.opened.other", "&aYou have opened %player%'s inventory"),
  INVENTORY_CLONED_PUBLIC("inventory.cloned.public", "&cYou have cloned %player%'s inventory to your own."),
  INVENTORY_CLONED_SILENT("inventory.cloned.silent", "&cYou have silently cloned %player%'s inventory"),
  
  // Restart Module
  
  // Rtp Module
  RTP_NO_SAFE_LOCATION("rtp.no_safe_location", "&aNo safe location was found, please try again."),
  RTP_ATTEMPTING_TO_FIND_LOCATION("rtp.attempting_to_find_location", "&aAttempting to find a location. Attempt %num% out of %max%."),
  
  // Skip-night Module
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
  
  // Spawn module
  SPAWN_NAME("spawn.name", "spawn"),
  SPAWN_NO_SET_SPAWN("spawn.no_set_spawn", "&aNo spawn is set, therefore you cannot teleport to spawn."),
  SPAWN_SUCCESSFULLY_SET("spawn.successfully_set", "&aYou have successfully set the new spawn location to your current location."),
  SPAWN_TELEPORTED_OTHER("spawn.teleported_other", "&aYou have teleported %player% to spawn."),
  
  // Staff Module
  STAFF_VANISHED_SELF("staff.vanish.self.true", "&aYou are now in vanish mode."),
  STAFF_UNVANISHED_SELF("staff.vanish.self.false", "&aYou are no longer in vanish mode."),
  STAFF_VANISHED_OTHER("staff.vanish.other.true", "&a%player% is now in vanish mode."),
  STAFF_UNVANISHED_OTHER("staff.vanish.other.false", "&a%player% is no longer in vanish mode."),
  STAFF_VANISHED_OTHER_NOTIFY("staff.vanish.other_notify.true", "&aYou are now in vanish mode. Action performed by %player%."),
  STAFF_UNVANISHED_OTHER_NOTIFY("staff.vanish.other_notify.false", "&aYou are no longer in vanish mode. Action performed by %player%."),
  STAFF_GAMEMODE_INVALID("staff.gamemode.invalid", "&aInvalid gamemode: %gamemode%."),
  STAFF_GAMEMODE_SELF("staff.gamemode.self", "&aYou are now in gamemode %gamemode%."),
  STAFF_GAMEMODE_OTHER("staff.gamemode.other", "&a%player% is now in gamemode %gamemode%."),
  STAFF_GAMEMODE_OTHER_NOTIFY("staff.gamemode.other_notify", "&aYou are now in gamemode %gamemode%. Action performed by %player%.");
  
  // Warp Module
  
  ;
  
  /**
   * -- GETTER --
   * Get the path to the string.
   */
  private final String path;
  private final String def;
  public static YamlConfiguration LANG;
  
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
  
  public TextComponent toComponent() {
    return ChatUtils.stringToComponent(LANG.getString(this.path, def));
  }
  
  public Component toComponent(Map<String, String> placeholders) {
    String[] var1 = {LANG.getString(this.path, def)};
    if (placeholders != null) {
      placeholders.forEach((k, v) -> var1[0] = var1[0].replace(k, v));
    }
    return ChatUtils.stringToComponent(LANG.getString(this.path, def));
  }
  
  public String toString() {
    return LANG.getString(this.path, def);
  }
  
  public Component toComponentWithPrefix() {
    
    String[] var1 = {Lang.LANG.getString(Lang.PREFIX.path, Lang.PREFIX.def) + LANG.getString(this.path, def)};
    
    return ChatUtils.stringToComponentCC(var1[0]);
  }
  public Component toComponentWithPrefix(Map<String, String> placeholders) {
    
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