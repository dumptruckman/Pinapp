package not.a.portal

import not.a.portal.extensions.getRelative
import not.a.portal.extensions.of
import not.a.portal.util.log
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace.*

class PortalFrame(val struckBlock: Block) {

    private var validated = false
    private var valid = false

    val isNotValid: Boolean
        get() = isValid
    val isValid: Boolean
        get() {
            if (validated) {
                return valid
            } else {
                valid = validate()
                return valid
            }
        }

    var orientation = Orientation.INVALID
        private set

    val bottomCorner: Block
        get() = if (isValid) _bottomCorner ?: throw IllegalStateException("Frame tracing failed") else
            throw IllegalStateException("Frame is not valid")
    val topCorner: Block
        get() = if (isValid) _topCorner ?: throw IllegalStateException("Frame tracing failed") else
            throw IllegalStateException("Frame is not valid")

    private var _bottomCorner: Block? = null
    private var _topCorner: Block? = null

    private fun validate(): Boolean {
        validated = true

        val upBlock = UP of struckBlock
        if (upBlock.type != Material.AIR) return false

        val baseGuide = FrameGuide(struckBlock)

        val posXBaseLength = baseGuide.trace(xOff = 1, yCheck = 1)
        val negXBaseLength = baseGuide.trace(xOff = -1, yCheck = 1)
        val posZBaseLength = baseGuide.trace(zOff = 1, yCheck = 1)
        val negZBaseLength = baseGuide.trace(zOff = -1, yCheck = 1)

        if (posXBaseLength > 0 && negXBaseLength > 0 && posXBaseLength + negXBaseLength <= MAX_SIZE) {
            log.trace { "Frame has valid x-wise base" }

            val posXBaseCorner = struckBlock.getRelative(xOff = posXBaseLength)
            val negXBaseCorner = struckBlock.getRelative(xOff = posXBaseLength)

            val posXSideLength = FrameGuide(posXBaseCorner).trace(yOff = 1, xCheck = -1)
            val negXSideLength = FrameGuide(negXBaseCorner).trace(yOff = 1, xCheck = 1)

            if (posXSideLength > 0 && negXSideLength > 0 && posXSideLength == negXSideLength) {
                log.trace { "Frame has valid x-wise sides" }

                val topCorner = posXBaseCorner.getRelative(yOff = posXSideLength)
                val topGuide = FrameGuide(topCorner)
                val topLength = topGuide.trace(xOff = -1, yCheck = -1)
                if (topLength == posXBaseLength + negXBaseLength) {
                    log.trace { "Frame has valid x-wise top" }

                    orientation = Orientation.X
                    _bottomCorner = negXBaseCorner
                    _topCorner = topCorner
                    return true
                }
            }
        }

        if (posZBaseLength > 0 && negZBaseLength > 0 && posZBaseLength + negZBaseLength <= MAX_SIZE) {
            log.trace { "Frame has valid z-wise base" }

            val posZBaseCorner = struckBlock.getRelative(xOff = posZBaseLength)
            val negZBaseCorner = struckBlock.getRelative(xOff = posZBaseLength)

            val posZSideLength = FrameGuide(posZBaseCorner).trace(yOff = 1, xCheck = -1)
            val negZSideLength = FrameGuide(negZBaseCorner).trace(yOff = 1, xCheck = 1)

            if (posZSideLength > 0 && negZSideLength > 0 && posZSideLength == negZSideLength) {
                log.trace { "Frame has valid z-wise sides" }

                val topCorner = posZBaseCorner.getRelative(yOff = posZSideLength)
                val topGuide = FrameGuide(topCorner)
                val topLength = topGuide.trace(xOff = -1, yCheck = -1)
                if (topLength == posZBaseLength + negZBaseLength) {
                    log.trace { "Frame has valid z-wise top" }

                    orientation = Orientation.Z
                    _bottomCorner = negZBaseCorner
                    _topCorner = topCorner
                    return true
                }
            }
        }

        return false
    }

    fun createPortal() {
        if (isNotValid) throw IllegalStateException("Portal can only be created for a valid portal frame")

        val height = topCorner.y - bottomCorner.z - 1
        val length = when (orientation) {
            Orientation.X -> topCorner.x - bottomCorner.x
            Orientation.Z -> topCorner.z - bottomCorner.z
            else -> throw IllegalStateException("Orientation should not be INVALID")
        }

        for (v in 1..height) {
            for (h in 1..length) {
                val block = when (orientation) {
                    Orientation.X -> bottomCorner.getRelative(yOff = v, xOff = h)
                    Orientation.Z -> bottomCorner.getRelative(yOff = v, zOff = h)
                    else -> throw IllegalStateException("Orientation should not be INVALID")
                }
                block.type = Material.PORTAL
            }
        }

        log.trace { "Portal created of type ${struckBlock.type}" }
    }

    companion object {
        private const val MAX_SIZE = 22
    }

    enum class Orientation {
        X, Z, INVALID
    }

    private class FrameGuide(val originBlock: Block) {

        fun isCorrectType(block: Block) = block.type == originBlock.type

        fun trace(xOff: Int = 0, yOff: Int = 0, zOff: Int = 0, xCheck: Int = 0, yCheck: Int = 0, zCheck: Int = 0): Int {
            for (i in 1..MAX_SIZE) {
                val checkBlock = originBlock.getRelative(xOff * i, yOff * i, zOff * i)
                if (isCorrectType(checkBlock)) {
                    val turnBlock = checkBlock.getRelative(xCheck, yCheck, zCheck)
                    if (isCorrectType(turnBlock)) {
                        return i
                    } else if (turnBlock.type != Material.AIR) {
                        return 0
                    }
                } else {
                    return 0
                }
            }
            return 0
        }
    }
}