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