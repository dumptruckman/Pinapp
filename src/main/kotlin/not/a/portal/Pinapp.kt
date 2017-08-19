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
package not.a.portal

import ch.qos.logback.classic.Level
import not.a.portal.extensions.registerEvents
import not.a.portal.util.Logging
import not.a.portal.util.log
import org.bukkit.Material
import org.bukkit.configuration.serialization.ConfigurationSerialization
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.java.JavaPluginLoader
import java.io.File
import java.util.stream.Collectors

class Pinapp : JavaPlugin {

    constructor() : super()

    internal constructor(loader: JavaPluginLoader, description: PluginDescriptionFile, dataFolder: File, file: File)
            : super(loader, description, dataFolder, file)

    private val settingsByMaterial = mutableMapOf<Material, WorldSettings>()

    override fun onLoad() {
        Logging.init(this)
        Logging.setLogLevel(Level.TRACE)
        log.debug { "DEBUG output enabled" }
        log.trace { "TRACE output enabled" }

        ConfigurationSerialization.registerClass(WorldSettings::class.java)
    }

    override fun onEnable() {
        if (!File(dataFolder, "config.yml").exists()) {
            saveDefaultConfig()
        }
        reloadConfig()

        if (isEnabled) { // Events won't be registered when unit testing.
            BlockListener(this).registerEvents(this)
        }
    }

    override fun reloadConfig() {
        super.reloadConfig()

        val worldList: List<WorldSettings> = config.getList("worlds", mutableListOf<WorldSettings>())
                .stream().filter {
            it is WorldSettings || it is Map<*, *>
        }.map {
            if (it is WorldSettings) {
                it
            } else {
                try {
                    WorldSettings.deserialize(it as Map<String, Any>)
                } catch(e: Exception) {
                    log.error(e) { "Could not interpret settings $it" }
                    WorldSettings("", Material.AIR)
                }
            }
        }.collect(Collectors.toList())

        config.set("worlds", worldList)

        settingsByMaterial.clear()
        worldList.filter { it.material != Material.AIR }.forEach {  settingsByMaterial[it.material] = it }
    }

    override fun saveConfig() {
        config.set("worlds", settingsByMaterial.values.toList())
        super.saveConfig()
    }

    fun getWorldSettings(material: Material) = settingsByMaterial[material]
}