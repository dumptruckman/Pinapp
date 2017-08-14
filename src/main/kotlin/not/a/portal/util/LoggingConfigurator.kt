package not.a.portal.util

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.PatternLayout
import ch.qos.logback.classic.spi.Configurator
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.encoder.LayoutWrappingEncoder
import ch.qos.logback.core.spi.ContextAwareBase

class LoggingConfigurator : ContextAwareBase(), Configurator {
    override fun configure(lc: LoggerContext) {
        addInfo("Setting up logging configuration.")

        val ca = ConsoleAppender<ILoggingEvent>()
        ca.setContext(lc)
        ca.setName("console")
        val encoder = LayoutWrappingEncoder<ILoggingEvent>()
        encoder.setContext(lc)

        val layout = PatternLayout().apply {
            setPattern("%d{HH:mm:ss} [%thread/%level]: [%logger{36}] %msg%n")
        }

        layout.setContext(lc)
        layout.start()
        encoder.setLayout(layout)

        ca.setEncoder(encoder)
        ca.start()

        val rootLogger = lc.getLogger(Logger.ROOT_LOGGER_NAME)
        rootLogger.level
        rootLogger.addAppender(ca)
    }
}