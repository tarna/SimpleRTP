package dev.tarna.rtp

import dev.tarna.rtp.commands.RTPCommand
import org.bukkit.plugin.java.JavaPlugin

class RTP : JavaPlugin() {
    override fun onEnable() {
        // Plugin startup logic
        getCommand("rtp")?.setExecutor(RTPCommand(this))
        saveDefaultConfig()
        logger.info("RTP has been enabled")
    }

    override fun onDisable() {
        // Plugin shutdown logic
        logger.info("RTP has been disabled")
    }
}
