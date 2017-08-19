/*
 * This file is part of Pinapp.
 *
 * Copyright (c) 2017 Jeremy Wood
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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