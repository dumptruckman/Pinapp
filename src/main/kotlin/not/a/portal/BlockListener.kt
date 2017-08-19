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

import com.okkero.skedule.schedule
import not.a.portal.Permissions.CREATE_PORTAL
import not.a.portal.extensions.cannot
import not.a.portal.extensions.of
import not.a.portal.util.log
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockIgniteEvent

class BlockListener(val plugin: Pinapp) : Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun blockIgnite(event: BlockIgniteEvent) {
        val player: Player = event.player ?: return
        val block: Block = BlockFace.DOWN of event.block

        val settings = plugin.getWorldSettings(block.type) ?: return

        if (player cannot CREATE_PORTAL) return

        if (settings.worldName == block.world.name) {
            log.trace { "${player.name} attempted to light a portal frame block that goes to the same world" }
            return
        }

        val frame = PortalFrame(block)

        if (frame.isValid) {
            Bukkit.getScheduler().schedule(plugin) {
                waitFor(1)
                frame.createPortal()
            }
        } else {
            log.trace { "${player.name} attempted to light an incomplete portal frame"}
        }
    }
}