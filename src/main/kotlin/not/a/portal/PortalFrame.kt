package not.a.portal

import not.a.portal.extensions.getRelative
import not.a.portal.extensions.of
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

    private fun validate(): Boolean {
        validated = true

        val upBlock = UP of struckBlock
        if (upBlock.type != Material.AIR) return false

        var checkedX = 0
        var checkedZ = 0

        var posXResult = Result.AIR
        var negXResult = Result.AIR
        var posZResult = Result.AIR
        var negZResult = Result.AIR

        var posXColumnOffset = 1
        var negXColumnOffset = -1
        var posZColumnOffset = 1
        var negZColumnOffset = -1

        for (h in 1..MAX_SIZE) {
            if (h <= posXColumnOffset) {
                posXResult = checkBlock(checkedX, posXResult, xOff = h)
                when {
                    posXResult == Result.AIR -> checkedX++
                    posXColumnOffset == 1 && posXResult == Result.SAME_TYPE -> posXColumnOffset = h
                }
            }

            if (h >= negXColumnOffset) {
                negXResult = checkBlock(checkedX, negXResult, xOff = -h)
                when {
                    negXResult == Result.AIR -> checkedX++
                    negXColumnOffset == -1 && negXResult == Result.SAME_TYPE -> negXColumnOffset = -h
                }
            }

            if (h <= posZColumnOffset) {
                posZResult = checkBlock(checkedZ, posZResult, zOff = h)
                when {
                    posZResult == Result.AIR -> checkedZ++
                    posZColumnOffset == 1 && posZResult == Result.SAME_TYPE -> posZColumnOffset = h
                }
            }

            if (h >= negZColumnOffset) {
                negZResult = checkBlock(checkedZ, negZResult, zOff = -h)
                when {
                    negZResult == Result.AIR -> checkedZ++
                    negZColumnOffset == -1 && negZResult == Result.SAME_TYPE -> negZColumnOffset = -h
                }
            }

        }

        return false
    }



    private fun checkBlock(checkedCount: Int, result: Result, xOff: Int = 0, zOff: Int = 0): Result {
        if (result.isContinuable && checkedCount <= MAX_SIZE) {
            val block = struckBlock.getRelative(xOff = xOff, zOff = zOff)
            return when {
                block.type == struckBlock.type -> Result.SAME_TYPE
                block.type == Material.AIR -> Result.AIR
                else -> Result.DEAD_END
            }
        }
        return result
    }

    fun createPortal() {
        if (isNotValid) throw IllegalStateException("Portal can only be created for a valid portal frame")
    }

    private enum class Result(val isContinuable: Boolean = false) {
        AIR(true), SAME_TYPE, DEAD_END
    }

    companion object {
        private const val MAX_SIZE = 22
    }
}