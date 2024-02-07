/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.discord.slashCommands.commands;

import me.lagggpixel.core.Main;
import me.lagggpixel.core.modules.discord.handlers.LinkHandler;
import me.lagggpixel.core.modules.discord.slashCommands.CoreBotSlashCommand;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandInteractionOption;
import org.javacord.api.interaction.SlashCommandOptionBuilder;
import org.javacord.api.interaction.SlashCommandOptionType;

import java.util.List;
import java.util.UUID;

/**
 * @author Lagggpixel
 * @since February 07, 2024
 */
public class LinkSlashCommand extends CoreBotSlashCommand {
  public LinkSlashCommand() {
    super("link", "Link your minecraft account to discord account",
        List.of(
            new SlashCommandOptionBuilder()
                .setName("token")
                .setType(SlashCommandOptionType.STRING)
                .setRequired(true)
                .build()
        ));
  }

  @Override
  public void action(SlashCommandInteraction interaction) {

    User discordUser = interaction.getUser();

    if (LinkHandler.getInstance().getLinkedPlayer(discordUser) != null) {
      String playerName = Main.getUser(LinkHandler.getInstance().getLinkedPlayer(discordUser)).getPlayerName();
      interaction.createImmediateResponder().setContent("You are already linked to " + playerName)
          .setFlags(MessageFlag.EPHEMERAL).respond();
      return;
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    String token = interaction.getOptionByName("token")
        .flatMap(SlashCommandInteractionOption::getStringValue).get();

    int intToken;
    try {
      intToken = Integer.parseInt(token);
    } catch (NumberFormatException e) {
      interaction.createImmediateResponder().setContent("Invalid token, please do /link in-game first.")
          .setFlags(MessageFlag.EPHEMERAL).respond();
      return;
    }

    UUID uuid = LinkHandler.getInstance().getPlayer(intToken);

    if (uuid == null) {
      interaction.createImmediateResponder().setContent("Invalid token, please do /link in-game first.")
          .setFlags(MessageFlag.EPHEMERAL).respond();
      return;
    }

    if (Main.getUser(uuid).getDiscordId() != null) {
      interaction.createImmediateResponder().setContent(Main.getUser(uuid).getPlayerName() + " is already linked to another discord account.")
          .setFlags(MessageFlag.EPHEMERAL).respond();
      return;
    }

    Main.getUser(uuid).setDiscordId(discordUser.getId());

    interaction.createImmediateResponder().setContent("You are now linked to " + Main.getUser(uuid).getPlayerName())
        .setFlags(MessageFlag.EPHEMERAL).respond();

  }
}
