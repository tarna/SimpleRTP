package dev.tarna.rtp.util

import java.time.Duration
import java.time.Instant
import java.util.*


object CooldownManager {
    private val map: MutableMap<UUID, Instant> = HashMap()

    fun setCooldown(key: UUID, duration: Duration?) {
        map[key] = Instant.now().plus(duration)
    }

    fun hasCooldown(key: UUID): Boolean {
        val cooldown = map[key]
        return cooldown != null && Instant.now().isBefore(cooldown)
    }

    fun removeCooldown(key: UUID): Instant? {
        return map.remove(key)
    }

    fun getRemainingCooldown(key: UUID): Duration {
        val cooldown = map[key]
        val now = Instant.now()
        return if (cooldown != null && now.isBefore(cooldown)) {
            Duration.between(now, cooldown)
        } else {
            Duration.ZERO
        }
    }
}