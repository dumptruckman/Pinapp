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
package com.dumptruckman.bukkit.utils

import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.WorldCreator
import org.bukkit.command.SimpleCommandMap
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.SimplePluginManager
import org.mockito.Mockito.*
import java.util.logging.Logger

object MockServerFactory {
    fun createMockedServer(): Server {
        val server = mock(Server::class.java)

        `when`(server.pluginManager).thenReturn(createPluginManager(server))
        `when`(server.createWorld(any(WorldCreator::class.java))).thenAnswer({
            invocation -> MockWorldFactory.createMockedWorld(invocation.arguments[0] as WorldCreator)
        })
        `when`(server.getWorld(any(String::class.java))).thenAnswer({
            invocation -> MockWorldFactory.createMockedWorld(invocation.arguments[0] as String)
        })
        `when`(server.getLogger()).thenReturn(Logger.getLogger("Server"))

        val serverField = Bukkit::class.java.getDeclaredField("server")
        serverField.isAccessible = true
        serverField.set(null, server)

        return server;
    }

    fun createPluginManager(server: Server): PluginManager {
        return SimplePluginManager(server, SimpleCommandMap(server))
    }
}
