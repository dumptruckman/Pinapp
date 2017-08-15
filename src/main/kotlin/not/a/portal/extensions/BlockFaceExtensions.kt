package not.a.portal.extensions

import org.bukkit.block.Block
import org.bukkit.block.BlockFace

infix fun BlockFace.of(block: Block): Block = block.getRelative(this)