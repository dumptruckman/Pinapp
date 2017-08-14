package not.a.portal.util

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import mu.KLogger
import mu.KotlinLogging
import org.bukkit.plugin.Plugin
import org.slf4j.LoggerFactory

val Any.log by lazy {
    Logging.getLogger()
}

object Logging {

    init {
        setLogLevel(Level.INFO)
    }

    var pluginName: String = ""

    fun init(plugin: Plugin) {
        pluginName = plugin.name
    }

    fun getLogger(): KLogger {
        return KotlinLogging.logger(pluginName)
    }

    fun setLogLevel(level: Level) {
        val logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger
        logger.level = level
    }
}