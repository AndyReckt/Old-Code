/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.defaults.commands.admin

import cc.fyre.stark.engine.command.Command
import org.bukkit.ChatColor
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import java.util.*

object SetSpawnCommand {
    private val RADIAL: Array<BlockFace> = arrayOf(BlockFace.WEST, BlockFace.NORTH_WEST, BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST)
    private val notches: EnumMap<BlockFace, Int> = EnumMap(BlockFace::class.java)

    @Command(["setspawn"], permission = "op")
    @JvmStatic
    fun setspawn(sender: Player) {
        val location = sender.location
        val face = yawToFace(location.yaw)
        sender.world.setSpawnLocation(location.blockX, location.blockY, location.blockZ, faceToYaw(face).toFloat(), 0.0f)
        sender.sendMessage("${ChatColor.GOLD}Set the spawn for " + ChatColor.WHITE + sender.world.name + ChatColor.GOLD + ".")
    }

    private fun yawToFace(yaw: Float): BlockFace {
        return RADIAL[Math.round(yaw / 45.0f) and 0x7]
    }

    private fun faceToYaw(face: BlockFace): Int {
        return wrapAngle(45 * faceToNotch(face))
    }

    private fun faceToNotch(face: BlockFace): Int {
        val notch = notches[face]
        return notch ?: 0
    }

    private fun wrapAngle(angle: Int): Int {
        var wrappedAngle: Int
        wrappedAngle = angle
        while (wrappedAngle <= -180) {
            wrappedAngle += 360
        }
        while (wrappedAngle > 180) {
            wrappedAngle -= 360
        }
        return wrappedAngle
    }

    init {
        for (i in RADIAL.indices) {
            notches[RADIAL[i]] = i
        }
    }
}
