package com.repoachiever.logging;

import com.repoachiever.logging.common.LoggingConfigurationHelper;
import com.repoachiever.service.state.StateService;
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
 * Service used for logging message transfer to RepoAchiever API Server allocation.
 */
@Plugin(name = "transferappender", category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE)
public class TransferAppender extends AbstractAppender {
    protected TransferAppender(String name, Filter filter) {
        super(name, filter, null, false, null);
    }

    @PluginFactory
    public static TransferAppender createAppender(
            @PluginAttribute("name") String name, @PluginElement("Filter") Filter filter) {
        return new TransferAppender(name, filter);
    }

    @Override
    public void append(LogEvent event) {
        String message = event.getMessage().getFormattedMessage();

        if (LoggingConfigurationHelper.isMessageTransferable(message)) {
            StateService.addLogMessage(
                    LoggingConfigurationHelper.extractTransferableMessage(message));
        }
    }
}
