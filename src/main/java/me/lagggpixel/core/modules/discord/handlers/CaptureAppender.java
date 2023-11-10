package me.lagggpixel.core.modules.discord.handlers;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Plugin(name = "CaptureAppender", category = "Core", elementType = "appender", printObject = true)
public class CaptureAppender extends AbstractAppender {
  
  private static final List<String> capturedLogs = new ArrayList<>();
  
  protected CaptureAppender(String name, Filter filter, Layout<? extends Serializable> layout) {
    super(name, filter, layout);
  }
  
  @Override
  public void append(LogEvent event) {
    capturedLogs.add(event.getMessage().getFormattedMessage());
  }
  
  // Factory method to create the appender
  @PluginFactory
  public static CaptureAppender createAppender(
      @PluginAttribute("name") String name,
      @PluginAttribute("ignoreExceptions") boolean ignoreExceptions,
      @PluginAttribute("otherAttribute") String otherAttribute) {
    return new CaptureAppender(name, null, null);
  }
  
  // Method to get the captured logs
  public static List<String> getCapturedLogs() {
    return new ArrayList<>(capturedLogs);
  }
  
  // Clear captured logs
  public static void clearCapturedLogs() {
    capturedLogs.clear();
  }
}
