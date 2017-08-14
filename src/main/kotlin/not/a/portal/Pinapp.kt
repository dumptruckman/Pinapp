package not.a.portal

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

        ConfigurationSerialization.registerClass(WorldSettings::class.java)
    }

    override fun onEnable() {
        if (!File(dataFolder, "config.yml").exists()) {
            saveDefaultConfig()
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
        worldList.forEach { if (it.material != Material.AIR) settingsByMaterial[it.material] = it }
    }

    override fun saveConfig() {
        config.set("worlds", settingsByMaterial.values.toList())
        super.saveConfig()
    }


}