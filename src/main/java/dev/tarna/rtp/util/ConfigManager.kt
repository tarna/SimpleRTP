package dev.tarna.rtp.util

import dev.tarna.rtp.RTP
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.InputStreamReader

class ConfigManager(val plugin: RTP) {
    lateinit var file: File
    lateinit var config: FileConfiguration

    init {
        plugin.saveDefaultConfig()
        loadConfigFile()
        plugin.logger.info("Config loaded")
    }

    private fun loadConfigFile() {
        file = plugin.dataFolder.resolve("config.yml")
        if (!file.exists()) {
            plugin.saveResource("config.yml", false)
        }
        config = YamlConfiguration.loadConfiguration(file)
        matchConfig()
    }

    private fun matchConfig() {
        try {
            var hasUpdated = false
            val stream = plugin.getResource(file.name) ?: error("Failed to load config file")
            val inputStream = InputStreamReader(stream)
            val defaultConfig = YamlConfiguration.loadConfiguration(inputStream)
            for (key in defaultConfig.getConfigurationSection("")!!.getKeys(true)) {
                if (!config.contains(key)) {
                    config.set(key, defaultConfig.get(key))
                    hasUpdated = true
                }
            }
            for (key in config.getConfigurationSection("")!!.getKeys(true)) {
                if (!defaultConfig.contains(key)) {
                    config.set(key, null)
                    hasUpdated = true
                }
            }
            if (hasUpdated) {
                config.save(file)
                plugin.logger.info("Updated config file")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun reload() {
        plugin.reloadConfig()
        loadConfigFile()
    }
}