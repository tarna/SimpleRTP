package dev.tarna.rtp.commands

import dev.tarna.rtp.RTP
import dev.tarna.rtp.util.CooldownManager
import dev.tarna.rtp.util.Util
import dev.tarna.rtp.util.color
import dev.tarna.rtp.util.send
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.time.Duration
import java.util.UUID

class RTPCommand(val plugin: RTP) : CommandExecutor {
    private val currentlyTeleporting = mutableSetOf<UUID>()

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.send("&cOnly players can use this command")
            return true
        }

        val permission = plugin.config.getString("permissions.rtp") ?: "rtp.use"
        if (!sender.hasPermission(permission)) {
            val permissionMessage = plugin.config.getString("messages.no-permission") ?: "&cYou do not have permission to use this command"
            sender.send(permissionMessage)
            return true
        }

        if (currentlyTeleporting.contains(sender.uniqueId)) {
            val teleportingMessage = plugin.config.getString("messages.teleporting") ?: "&cYou are already teleporting. Please wait."
            sender.send(teleportingMessage)
            return true
        }

        val timeLeft = CooldownManager.getRemainingCooldown(sender.uniqueId)
        if (timeLeft.isNegative || timeLeft.isZero) {
            val waitingMessage = plugin.config.getString("messages.waiting") ?: "&aPlease wait while we find a location for you..."
            sender.send(waitingMessage)

            val countdownSeconds = plugin.config.getInt("settings.countdown")
            if (countdownSeconds <= 0) {
                val location = getRandomLocation()
                teleport(sender, location)
                return true
            }

            val countdownMessage = plugin.config.getString("messages.countdown") ?: "&aTeleporting in {time} seconds..."
            val playerLocation = sender.location

            object : BukkitRunnable() {
                private var seconds = countdownSeconds
                private var cancelOnMove = plugin.config.getBoolean("settings.cancel-on-move")
                private lateinit var location: Location

                init {
                    currentlyTeleporting.add(sender.uniqueId)
                    plugin.server.scheduler.runTaskAsynchronously(plugin, Runnable { location = getRandomLocation() })
                }

                override fun run() {
                    if (cancelOnMove && playerLocation.distanceSquared(sender.location) > 1) {
                        val cancelMessage = plugin.config.getString("messages.cancel") ?: "&cTeleportation cancelled because you moved"
                        sender.send(cancelMessage)
                        currentlyTeleporting.remove(sender.uniqueId)
                        cancel()
                        return
                    }
                    if (seconds == 0) {
                        teleport(sender, location)
                        cancel()
                        return
                    }
                    val message = countdownMessage
                        .replace("{time}", seconds.toString())
                        .color()
                    sender.send(message)
                    seconds--
                }
            }.runTaskTimer(plugin, 0, 20)
            return true
        } else {
            var cooldownMessage = plugin.config.getString("messages.cooldown") ?: "&cYou must wait {time} seconds before using this command again"
            cooldownMessage = cooldownMessage
                .replace("{time}", Util.formatTime(timeLeft.seconds))
                .color()
            sender.send(cooldownMessage)
            return true
        }
    }

    private fun teleport(player: Player, location: Location) {
        currentlyTeleporting.remove(player.uniqueId)
        val bypassPermission = plugin.config.getString("permissions.bypass") ?: "rtp.cooldown.bypass"
        if (!player.hasPermission(bypassPermission)) {
            val cooldown = plugin.config.getLong("cooldown.rtp")
            CooldownManager.setCooldown(player.uniqueId, Duration.ofSeconds(cooldown))
        }

        player.teleport(location)

        val x = location.x
        val y = location.y
        val z = location.z

        var message = plugin.config.getString("messages.teleported") ?: "&aYou have been teleported to &e$x, $y, $z"
        message = message
            .replace("{x}", x.toString())
            .replace("{y}", y.toString())
            .replace("{z}", z.toString())
            .replace("{world}", location.world?.name ?: "world")
            .replace("{player}", player.name)
            .color()
        player.send(message)
    }

    private fun getRandomLocation(): Location {
        val minX = plugin.config.getInt("border.x-min")
        val maxX = plugin.config.getInt("border.x-max")
        val minZ = plugin.config.getInt("border.z-min")
        val maxZ = plugin.config.getInt("border.z-max")
        val worldName = plugin.config.getString("world") ?: "world"
        val world = Bukkit.getWorld(worldName) ?: Bukkit.getWorlds().first()
        return Util.getRandomLocation(world, minX, maxX, minZ, maxZ)
    }
}