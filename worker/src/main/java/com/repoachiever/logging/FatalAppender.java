package com.repoachiever.logging;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

/** FatalAppender provides custom flow for logging process. */
@Plugin(name = "FatalAppender", category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE)
public class FatalAppender extends AbstractAppender {
  private ConcurrentMap<String, LogEvent> eventMap = new ConcurrentHashMap<>();

  protected FatalAppender(String name, Filter filter) {
    super(name, filter, null);
  }

  @PluginFactory
  public static FatalAppender createAppender(
      @PluginAttribute("name") String name, @PluginElement("Filter") Filter filter) {
    return new FatalAppender(name, filter);
  }

  @Override
  public void append(LogEvent event) {
    if (event.getLevel().equals(Level.FATAL)) {
      System.exit(1);
    }

    eventMap.put(Instant.now().toString(), event);
  }
}
