package dev.tarna.rtp.util

import org.bukkit.command.CommandSender

fun CommandSender.send(message: String) {
    sendMessage(message.color())
}