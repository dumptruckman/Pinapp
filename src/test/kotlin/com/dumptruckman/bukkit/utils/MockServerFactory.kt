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
