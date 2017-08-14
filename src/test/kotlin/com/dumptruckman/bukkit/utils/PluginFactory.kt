package com.dumptruckman.bukkit.utils

import not.a.portal.Pinapp
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.java.JavaPluginLoader
import java.io.File

object PluginFactory {

    fun createPluginInstance(): Pinapp {
        val server = MockServerFactory.createMockedServer()
        val pluginLoader = JavaPluginLoader(server)
        val description = PluginDescriptionFile("Pinapp", "3.0", "not.a.portal.Pinapp")
        val dataFolder = File("./build/server/plugins/dataFolder")
        val file = File(".build/lib/Pinapp-3.0-SNAPSHOT.jar")

        return Pinapp(pluginLoader, description, dataFolder, file)
    }
}