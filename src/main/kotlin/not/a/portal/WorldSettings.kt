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