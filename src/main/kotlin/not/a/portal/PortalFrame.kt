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
        get() = !isValid
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

        val xBaseLength = posXBaseLength + negXBaseLength
        val zBaseLength = posZBaseLength + negZBaseLength

        if (posXBaseLength > 0 && negXBaseLength > 0 && xBaseLength <= MAX_SIZE && xBaseLength + 1 >= MIN_H_SIZE) {
            log.trace { "Frame has valid x-wise base of length ${xBaseLength + 1}" }

            val posXBaseCorner = struckBlock.getRelative(xOff = posXBaseLength)
            val negXBaseCorner = struckBlock.getRelative(xOff = -negXBaseLength)

            val posXSideLength = FrameGuide(posXBaseCorner).trace(yOff = 1, xCheck = -1)
            val negXSideLength = FrameGuide(negXBaseCorner).trace(yOff = 1, xCheck = 1)

            if (posXSideLength > 0 && negXSideLength > 0 && posXSideLength == negXSideLength
                    && posXSideLength + 1 >= MIN_V_SIZE) {
                log.trace { "Frame has valid x-wise sides of ${posXSideLength + 1}" }

                val topCorner = posXBaseCorner.getRelative(yOff = posXSideLength)
                val topGuide = FrameGuide(topCorner)
                val topLength = topGuide.trace(xOff = -1, yCheck = -1)
                if (topLength == posXBaseLength + negXBaseLength) {
                    log.trace { "Frame has valid x-wise top" }

                    return checkCenter(Orientation.X, negXBaseCorner, topCorner)
                }
            }
        }

        if (posZBaseLength > 0 && negZBaseLength > 0 && zBaseLength <= MAX_SIZE && zBaseLength + 1 >= MIN_H_SIZE) {
            log.trace { "Frame has valid z-wise base of length ${zBaseLength + 1}" }

            val posZBaseCorner = struckBlock.getRelative(zOff = posZBaseLength)
            val negZBaseCorner = struckBlock.getRelative(zOff = -negZBaseLength)

            val posZSideLength = FrameGuide(posZBaseCorner).trace(yOff = 1, zCheck = -1)
            val negZSideLength = FrameGuide(negZBaseCorner).trace(yOff = 1, zCheck = 1)

            if (posZSideLength > 0 && negZSideLength > 0 && posZSideLength == negZSideLength
                    && posZSideLength + 1 >= MIN_V_SIZE) {
                log.trace { "Frame has valid z-wise sides of ${posZSideLength + 1}" }

                val topCorner = posZBaseCorner.getRelative(yOff = posZSideLength)
                val topGuide = FrameGuide(topCorner)
                val topLength = topGuide.trace(zOff = -1, yCheck = -1)
                if (topLength == posZBaseLength + negZBaseLength) {
                    log.trace { "Frame has valid z-wise top" }

                    return checkCenter(Orientation.Z, negZBaseCorner, topCorner)
                }
            }
        }

        return false
    }

    private fun checkCenter(orientation: Orientation, bottomCorner: Block, topCorner: Block): Boolean {
        val height = topCorner.y - bottomCorner.y - 1
        val length = when (orientation) {
            Orientation.X -> topCorner.x - bottomCorner.x - 1
            Orientation.Z -> topCorner.z - bottomCorner.z - 1
            else -> throw IllegalStateException("Orientation should not be INVALID")
        }

        for (v in 1..height) {
            for (h in 1..length) {
                val block = when (orientation) {
                    Orientation.X -> bottomCorner.getRelative(yOff = v, xOff = h)
                    Orientation.Z -> bottomCorner.getRelative(yOff = v, zOff = h)
                    else -> throw IllegalStateException("Orientation should not be INVALID")
                }
                if (block.type != Material.AIR) return false
            }
        }

        log.trace { "Frame has hollow center" }

        this.orientation = orientation
        _bottomCorner = bottomCorner
        _topCorner = topCorner
        return true
    }

    fun createPortal() {
        if (isNotValid) throw IllegalStateException("Portal can only be created for a valid portal frame")

        val height = topCorner.y - bottomCorner.y - 1
        val length = when (orientation) {
            Orientation.X -> topCorner.x - bottomCorner.x - 1
            Orientation.Z -> topCorner.z - bottomCorner.z - 1
            else -> throw IllegalStateException("Orientation should not be INVALID")
        }

        for (v in 1..height) {
            for (h in 1..length) {
                val block = when (orientation) {
                    Orientation.X -> bottomCorner.getRelative(yOff = v, xOff = h)
                    Orientation.Z -> bottomCorner.getRelative(yOff = v, zOff = h)
                    else -> throw IllegalStateException("Orientation should not be INVALID")
                }
                block.setType(Material.PORTAL, false)
                block.setData(orientation.dataValue, false)
            }
        }

        log.trace { "Portal created with ${struckBlock.type} frame" }
    }

    companion object {
        private const val MAX_SIZE = 22
        private const val MIN_H_SIZE = 4
        private const val MIN_V_SIZE = 5
    }

    enum class Orientation(val dataValue: Byte) {
        X(1), Z(2), INVALID(0)
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