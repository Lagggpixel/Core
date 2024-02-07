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

import java.util.UUID;

/**
 * @author Lagggpixel
 * @since February 07, 2024
 */
public class UnlinkSlashCommand extends CoreBotSlashCommand {
  public UnlinkSlashCommand() {
    super("unlink", "Unlink your minecraft account to discord account", null);
  }

  @Override
  public void action(SlashCommandInteraction interaction) {

    User discordUser = interaction.getUser();

    if (LinkHandler.getInstance().getLinkedPlayer(discordUser) == null) {
      interaction.createImmediateResponder().setContent("You are not linked to anyone currently.")
          .setFlags(MessageFlag.EPHEMERAL).respond();
      return;
    }


    UUID uuid = LinkHandler.getInstance().getLinkedPlayer(discordUser);

    if (uuid == null) {
      interaction.createImmediateResponder().setContent("You are not linked to anyone currently.")
          .setFlags(MessageFlag.EPHEMERAL).respond();
      return;
    }

    if (Main.getUser(uuid).getDiscordId() == null) {
      interaction.createImmediateResponder().setContent("You are not linked to anyone currently.")
          .setFlags(MessageFlag.EPHEMERAL).respond();
      return;
    }

    Main.getUser(uuid).setDiscordId(null);

    interaction.createImmediateResponder().setContent("You are no longer linked to " + Main.getUser(uuid).getPlayerName())
        .setFlags(MessageFlag.EPHEMERAL).respond();
  }
}
