/*
 * Copyright (c) 2024 Infinite Minecrafter's Developers.
 *
 * This file was created by the developers of Infinite Minecrafter's.
 *
 * You are hereby granted the right to view the code for personal or educational purposes.
 * However, you are not allowed to copy, distribute, or resell the code without
 * explicit permission from the lead developer of Infinite Minecrafter's.
 */

package me.lagggpixel.core.modules.discord.handlers;

import lombok.Getter;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lagggpixel
 * @since January 22, 2024
 */
@Plugin(name = "CaptureAppender", category = "Core", elementType = "appender", printObject = true)
public class CaptureAppender extends AbstractAppender {
  
  @Getter
  private static final List<String> capturedLogs = new ArrayList<>();
  
  protected CaptureAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions, Property[] properties) {
    super(name, filter, layout, ignoreExceptions, properties);
    this.start();
  }
  
  @Override
  public void append(LogEvent event) {
    ConsoleHandler.onLogEvent(event);
    capturedLogs.add(event.getMessage().getFormattedMessage());
  }

  /**
   * Creates a CaptureAppender with the given name and ignoreExceptions flag.
   *
   * @param  name              the name of the CaptureAppender
   * @param  ignoreExceptions  a flag indicating whether exceptions should be ignored
   * @return                   a new CaptureAppender instance
   */
  @PluginFactory
  public static CaptureAppender createAppender(
      @PluginAttribute("name") String name,
      @PluginAttribute("ignoreExceptions") boolean ignoreExceptions) {
    return new CaptureAppender(name, null, null, ignoreExceptions, Property.EMPTY_ARRAY);
  }

  /**
   * Clears the list of captured logs.
   */
  public static void clearCapturedLogs() {
    capturedLogs.clear();
  }
}
