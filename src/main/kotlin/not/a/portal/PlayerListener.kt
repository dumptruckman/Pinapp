package not.a.portal

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerPortalEvent

class PlayerListener(val plugin: Pinapp) : Listener {

    @EventHandler(ignoreCancelled = true)
    fun playerPortal(event: PlayerPortalEvent) {
        val player = event.player
        event.portalTravelAgent.
    }
}