package dev.tarna.rtp

import dev.tarna.rtp.commands.RTPCommand
import dev.tarna.rtp.util.ConfigManager
import org.bstats.bukkit.Metrics
import org.bstats.charts.SingleLineChart
import org.bukkit.plugin.java.JavaPlugin

class RTP : JavaPlugin() {
    override fun onEnable() {
        val start = System.currentTimeMillis()
        ConfigManager(this)
        getCommand("rtp")?.setExecutor(RTPCommand(this))
        loadMetrics()
        logger.info("RTP has been enabled in ${System.currentTimeMillis() - start}ms")
    }

    override fun onDisable() {
        val start = System.currentTimeMillis()
        logger.info("RTP has been disabled in ${System.currentTimeMillis() - start}ms")
    }

    private fun loadMetrics() {
        Metrics(this, 24388)
        logger.info("Metrics have been loaded")
    }
}