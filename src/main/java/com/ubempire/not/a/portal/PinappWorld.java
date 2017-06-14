package com.ubempire.not.a.portal;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.PortalCreateEvent;

class PinappWorld implements Listener {

    @EventHandler
    public void onPortalCreate(PortalCreateEvent event) {
        event.setCancelled(true);
    }
}
