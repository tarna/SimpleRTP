package dev.tarna.rtp.util

import org.bukkit.ChatColor

fun String.color() = ChatColor.translateAlternateColorCodes('&', this)