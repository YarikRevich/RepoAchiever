package com.repoachiever.logging;

import com.repoachiever.service.state.StateService;
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

/**
 * Service used for logging fatal level application state changes.
 */
@Plugin(name = "fatalappender", category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE)
public class FatalAppender extends AbstractAppender {
  protected FatalAppender(String name, Filter filter) {
    super(name, filter, null, false, null);
  }

  @PluginFactory
  public static FatalAppender createAppender(
      @PluginAttribute("name") String name, @PluginElement("Filter") Filter filter) {
    return new FatalAppender(name, filter);
  }

  @Override
  public void append(LogEvent event) {
    if (event.getLevel().equals(Level.FATAL)) {
      StateService.setExit(true);
    }
  }
}
