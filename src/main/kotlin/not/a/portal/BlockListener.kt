package not.a.portal

import not.a.portal.Permissions.CREATE_PORTAL
import not.a.portal.extensions.cannot
import not.a.portal.util.log
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockIgniteEvent

class BlockListener(val plugin: Pinapp) : Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun blockIgnite(event: BlockIgniteEvent) {
        val player: Player = event.player ?: return
        val block: Block = event.block

        val settings = plugin.getWorldSettings(block.type) ?: return

        if (player cannot CREATE_PORTAL) return

        if (settings.worldName == block.world.name) {
            log.trace { "${player.name} attempted to light a portal frame block that goes to the same world" }
            return
        }

        val frame = PortalFrame(block)

        if (frame.isValid) frame.createPortal() else {
            log.trace { "${player.name} attempted to light an incomplete portal frame"}
            return
        }
    }
}