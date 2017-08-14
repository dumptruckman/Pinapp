package com.dumptruckman.bukkit.utils

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.WorldCreator
import org.mockito.Mockito.*
import java.io.File

object MockWorldFactory {

    val serverFolder = File("./build/server/")
    init {
        serverFolder.mkdirs()
    }

    fun createMockedWorld(worldCreator: WorldCreator): World {
        return createMockedWorld(worldCreator.name())
    }

    fun createMockedWorld(name: String): World {
        val world = mock(World::class.java)

        val file = File(serverFolder, name)
        file.mkdirs()
        `when`(world.worldFolder).thenReturn(file)
        `when`(world.name).thenReturn(name)
        `when`(world.spawnLocation).thenReturn(Location(world, 0.0, 0.0, 0.0))

        return world
    }
}