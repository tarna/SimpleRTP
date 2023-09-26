package dev.tarna.rtp.util

import org.bukkit.Location
import org.bukkit.World

object Util {
    fun formatTime(seconds: Long): String {
        var seconds = seconds
        val hours = seconds / 3600
        seconds %= 3600
        val minutes = seconds / 60
        seconds %= 60
        return if (hours > 0) {
            String.format("%02d hours %02d minutes %02d seconds", hours, minutes, seconds)
        } else if (minutes > 0) {
            String.format("%02d minutes %02d seconds", minutes, seconds)
        } else {
            String.format("%02d seconds", seconds)
        }
    }

    fun getRandomLocation(world: World, minX: Int, maxX: Int, minZ: Int, maxZ: Int): Location {
        val x = (minX..maxX).random()
        val z = (minZ..maxZ).random()
        val y = world.getHighestBlockYAt(x, z)
        val location = world.getBlockAt(x, y, z).location
        return if (location.block.isLiquid) {
            getRandomLocation(world, minX, maxX, minZ, maxZ)
        } else {
            location.y += 1
            location
        }
    }
}