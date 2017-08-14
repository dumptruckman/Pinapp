package not.a.portal

import org.bukkit.Material
import org.bukkit.World
import org.bukkit.configuration.serialization.ConfigurationSerializable

class WorldSettings(var worldName: CharSequence,
                    var material: Material,
                    var environment: World.Environment = World.Environment.NORMAL,
                    var seed: CharSequence? = null,
                    var generator: CharSequence? = null) : ConfigurationSerializable {

    override fun serialize(): Map<String, Any> {
        val result = mutableMapOf<String, Any>()
        result["name"] = worldName
        result["material"] = material
        result["environment"] = environment
        if (seed != null) result["seed"] = seed.toString()
        if (generator != null) result["generator"] = generator.toString()
        return result
    }

    companion object {

        @JvmStatic
        fun deserialize(data: Map<String, Any>): WorldSettings {
            if ("name" !in data) throw IllegalArgumentException("World config must have 'name'")
            val name = data["name"].toString()

            if ("material" !in data) throw IllegalArgumentException("World config must have 'material'")
            val material = Material.getMaterial(data["material"].toString())
            if (material == null) throw IllegalArgumentException("${data["material"]} is not a valid material")

            val environment by lazy {
                try {
                    World.Environment.valueOf((data["environment"]?.toString() ?: "NORMAL").toUpperCase())
                } catch(e: Exception) {
                    World.Environment.NORMAL
                }
            }

            return WorldSettings(name, material, environment, data["seed"]?.toString(), data["generator"]?.toString())
        }

    }
}