package com.ubempire.not.a.portal;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class PinappPortalCreate {
    Pinapp p;

    public PinappPortalCreate(Pinapp p) {
        this.p = p;
    }

    public void register() {
        BlockWatch b = new BlockWatch(p);
        p.getServer().getPluginManager().registerEvents(b, p);
        p.log("Registered BLOCK_PLACE, BLOCK_IGNITE, and BLOCK_PHYSICS events successfully.");
    }
}

