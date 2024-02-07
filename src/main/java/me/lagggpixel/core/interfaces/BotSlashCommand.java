/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.interfaces;

import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandInteraction;

/**
 * @author Lagggpixel
 * @since February 07, 2024
 */
public interface BotSlashCommand {

  SlashCommand getSlashCommand();

  long getCommandId();

  boolean isGlobal();

  void action(SlashCommandInteraction interaction);

}
