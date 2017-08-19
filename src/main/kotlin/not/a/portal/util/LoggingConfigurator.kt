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