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
  
  // Guild module
  GUILD_FAILED_TO_CREATE("guild.failed_to_create", "&cFailed to create guild."),
  GUILD_ALREADY_IN_GUILD("guild.already_in_guild", "&cYou are already in a guild!"),
  GUILD_NOT_IN_GUILD("guild.not_in_guild", "&cYou are not in a guild!"),
  GUILD_ALREADY_EXIST("guild.already_exist", "&cA guild named '&e%guild%&c' already exists."),
  GUILD_DOES_NOT_EXIST("guild.does_not_exist", "&cThe guild '&e%guild%&c' does not exist."),
  GUILD_NOT_LEADER_DISBAND("guild.disband_not_leader", "Only the guild leader can disband the guild!"),
  GUILD_CREATED("guild.created", "&aYou've successfully created a guild called '&e%guild%&a'."),
  GUILD_LIMIT_REACHED("guild.limit_reached", "&cYou cannot create more guilds."),
  GUILD_DISBANDED_LEADER("guild.disbanded.leader", "&aYou've disbanded the guild '&e%guild%&a'."),
  GUILD_DISBANDED_MEMBER("guild.disbanded.member", "&aYour guild has been disbanded."),
  GUILD_NAME_INVALID("guild.invalid_name", "&cPlease create a guild with a different name. (Guild names cannot start with numbers and cannot contain special characters and have a length of 3-16 characters)"),
  GUILD_CREATED_OTHER_PLAYER("guild.admin.created_other_player", "&aYou have created a guild for %player%, named '&e%guild%&a'."),
  GUILD_DELETED_OTHER_PLAYER("guild.admin.deleted_other_player", "&aYou have deleted %player%'s guild, named '&e%guild%&a'."),
  GUILD_NAM_EXISTS("guild.tag_exists", "&cThe guild name '&e%tag%&c' already exists."),
  GUILD_MUST_BE_OFFICER("guild.must_be_officer", "&cYou must be an officer in the guild to do that."),
  GUILD_MUST_BE_LEADER("guild.must_be_leader", "&cYou must be the guild leader to do that."),
  GUILD_SETHOME_ACKNOWLEDGE("guild.sethome.acknowledge", "&aYou have set your guild's home location to %x%, %y%, %z%."),
  GUILD_SETHOME_BROADCAST("guild.sethome.broadcast", "&a%player% has set their guild's home location to %x%, %y%, %z%."),
  GUILD_SETHOME_NOT_IN_CLAIM("guild.sethome.not_in_claim", "&cYou are not in your guild's claim!"),
  GUILD_INVITE_PLAYER_ALREADY_JOINED("guild.invite.player_already_joined", "&c%player% is already in your guild!"),
  GUILD_INVITE_PLAYER_ALREADY_INVITED("guild.invite.player_already_invited", "&c%player% is already invited to your guild!"),
  GUILD_INVITED_PLAYER_ACKNOWLEDGE("guild.invite.invited_player.acknowledge", "&aYou have invited %player% to your guild!"),
  GUILD_INVITED_PLAYER_BROADCAST("guild.invite.invited_player.broadcast", "&a%player% has have invited %target% to your guild!"),
  GUILD_INVITED_PLAYER_NOTIFY("guild.invite.invited_player.notify", "&a%player% has invited you to their guild %guild%!"),
  GUILD_INVITED_PLAYER_TOOLTIP("guild.invite.invited_player.tooltip", "&eClick to join the guild!"),
  GUILD_CLAIM_NO_INTERACT("guild.claim.no_interact", "&cYou cannot interact with anything on this claim!"),
  GUILD_CLAIM_MESSAGES_ENTERING_ALLY("guild.claim.claim_messages.entering.ally", "&eNow entering &a%guild%&e."),
  GUILD_CLAIM_MESSAGES_ENTERING_FRIENDLY("guild.claim.claim_messages.entering.friendly", "&eNow entering &a%guild%&e."),
  GUILD_CLAIM_MESSAGES_ENTERING_ENEMY("guild.claim.claim_messages.entering.enemy", "&eNow entering &c%guild%&e."),
  GUILD_CLAIM_MESSAGES_ENTERING_SYSTEM("guild.claim.claim_messages.entering.system", "&eNow Entering &7Wilderness&e."),
  GUILD_CLAIM_MESSAGES_LEAVING_ALLY("guild.claim.claim_messages.leaving.ally", "&eNow leaving &a%guild%&e."),
  GUILD_CLAIM_MESSAGES_LEAVING_FRIENDLY("guild.claim.claim_messages.leaving.friendly", "&eNow leaving &a%guild%&e."),
  GUILD_CLAIM_MESSAGES_LEAVING_ENEMY("guild.claim.claim_messages.leaving.enemy", "&eNow leaving &c%guild%&e."),
  GUILD_CLAIM_MESSAGES_LEAVING_SYSTEM("guild.claim.claim_messages.leaving.system", "&eNow Leaving &7Wilderness&e."),
  GUILD_WAND_MESSAGES_CLEAR("guild.claim.wand_messages.clear", "&cRight-click the wand again to clear your selection!"),
  GUILD_WAND_MESSAGES_CLEARED("guild.claim.wand_messages.cleared", "&aYou have successfully cleared your wand selection!"),
  GUILD_WAND_MESSAGES_INVALID_SELECTION("guild.claim.wand_messages.invalid_selection", "&cYou do not have a valid selection!"),
  GUILD_WAND_MESSAGES_FIRST_POINT("guild.claim.wand_messages.first_point", "&eYou have set the &afirst&e point to &7(X:&e %x% &7Z:&e %z%&7)"),
  GUILD_WAND_MESSAGES_SECOND_POINT("guild.claim.wand_messages.second_point", "&eYou have set the &asecond&e point to &7(X:&e %x% &7Z:&e %z%&7)"),
  GUILD_WAND_MESSAGES_OVERCLAIM("guild.claim.wand_messages.overclaim", "&cYou cannot claim over claimed faction land!"),
  GUILD_WAND_MESSAGES_TOO_CLOSE("guild.claim.wand_messages.too_close", "&cYour claim is too close to another faction claim!"),
  GUILD_WAND_MESSAGES_TOO_FAR("guild.claim.wand_messages.too_far", "&cYou must claim closer to previous claims!"),
  GUILD_WAND_MESSAGES_OTHER("guild.claim.wand_messages.other", "&cYou cannot claim here!"),
  GUILD_WAND_MESSAGES_TOO_SMALL("guild.claim.wand_messages.too_small", "&cYour claim is too small!"),
  GUILD_WAND_MESSAGES_COST_TOO_MUCH("guild.claim.wand_messages.cost_too_much", "&eThis claim will cost &c$%amount%&e!"),
  GUILD_WAND_MESSAGES_COST_ENOUGH("guild.claim.wand_messages.cost_enough", "&eThis claim will cost &a$%amount%&e!"),
  GUILD_WAND_MESSAGES_INVALID_FUNDS("guild.claim.wand_messages.invalid_funds", "&cYour faction does not have enough money to purchase this claim!"),
  GUILD_WAND_MESSAGES_BROADCAST("guild.claim.wand_messages.broadcast", "&7%player% &ehas claimed land for your faction!"),
  GUILD_PLACEHOLDER_COLOR_ENEMY("guild.placeholders.color", "&c"),
  GUILD_PLACEHOLDER_COLOR_FRIENDLY("guild.placeholders.color", "&a"),
  GUILD_PLACEHOLDER_COLOR_ALLY("guild.placeholders.color", "&6"),
  GUILD_PLACEHOLDER_SYSTEM("guild.placeholders.system", "&7Wilderness"),
  GUILD_PLACEHOLDER_NO_GUILD("guild.placeholders.no_guild", "&7None"),

  // Home Module
  HOME_ALREADY_EXIST("home.already_exist", "&cA home named '&e%home%&c' already exists."),
  HOME_DOES_NOT_EXIST("home.does_not_exist", "&cThe home '&e%home%&c' does not exist."),
  HOME_CREATED("home.created", "&aYou've successfully created a home called '&e%home%&a'."),
  HOME_LIMIT_REACHED("home.limit_reached", "&cYou cannot create more homes."),
  HOME_DELETED("home.deleted", "&aYou've deleted the home '&e%home%&a'."),
  HOME_NAME_INVALID("home.invalid_name", "&cPlease create a home with a different name. (Home names cannot start with numbers and cannot contain special characters)"),
  HOME_CREATED_OTHER_PLAYER("home.admin.created_other_player", "&aYou have created a home for %player%, named '&e%home%&a'."),
  HOME_DELETED_OTHER_PLAYER("home.admin.deleted_other_player", "&aYou have deleted %player%'s home, named '&e%home%&a'."),
  HOME_TELEPORTED_TO_HOME_OTHER_PLAYER("home.admin.teleported_to_home_other_player", "&aYou have teleported to %player%'s home, named '&e%home%&a'."),
  HOME_NO_HOMES_OTHER_PLAYER("home.admin.no_homes_other_player", "&a%player% does not have any homes."),
  
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
  STAFF_MODE_ENABLED("staff.mode.enabled", "&aStaff mode has been enabled."),
  STAFF_MODE_DISABLED("staff.mode.disabled", "&aStaff mode has been disabled."),
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
  
  public Component toComponent() {
    return ChatUtils.stringToComponent(LANG.getString(this.path, def));
  }
  
  public Component toComponent(Map<String, String> placeholders) {
    String[] var1 = {LANG.getString(this.path, def)};
    if (placeholders != null) {
      placeholders.forEach((k, v) -> var1[0] = var1[0].replace(k, v));
    }
    return ChatUtils.stringToComponent(LANG.getString(this.path, def));
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

  public TextComponent toTextComponent() {
    return ChatUtils.stringToTextComponent(LANG.getString(this.path, def));
  }

  public TextComponent toTextComponent(Map<String, String> placeholders) {
    String[] var1 = {LANG.getString(this.path, def)};
    if (placeholders != null) {
      placeholders.forEach((k, v) -> var1[0] = var1[0].replace(k, v));
    }
    return ChatUtils.stringToTextComponent(LANG.getString(this.path, def));
  }

  public TextComponent toTextComponentWithPrefix() {

    String[] var1 = {Lang.LANG.getString(Lang.PREFIX.path, Lang.PREFIX.def) + LANG.getString(this.path, def)};

    return ChatUtils.stringToTextComponentCC(var1[0]);
  }

  public TextComponent toTextComponentWithPrefix(Map<String, String> placeholders) {

    String[] var1 = {Lang.LANG.getString(Lang.PREFIX.path, Lang.PREFIX.def) + LANG.getString(this.path, def)};
    if (placeholders != null) {
      placeholders.forEach((k, v) -> var1[0] = var1[0].replace(k, v));
    }

    return ChatUtils.stringToTextComponentCC(var1[0]);
  }


  public String toString() {
    return LANG.getString(this.path, def);
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