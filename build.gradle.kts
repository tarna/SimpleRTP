import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import xyz.jpenilla.runpaper.task.RunServer

plugins {
    kotlin("jvm") version "1.8.21"
    kotlin("plugin.serialization") version "1.9.0"

    id("com.github.johnrengelman.shadow") version "8.1.1"

    id("net.minecrell.plugin-yml.bukkit") version "0.5.3"
    id("xyz.jpenilla.run-paper") version "2.1.0"
}

group = "dev.tarna"
version = "1.0.0"

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        mavenCentral()

        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }

    dependencies {
        compileOnly("org.spigotmc:spigot-api:1.20.1-R0.1-SNAPSHOT")
    }

    kotlin {
        jvmToolchain(17)
    }
}

repositories {
    maven("https://jitpack.io")
}

dependencies {
}

tasks.withType<RunServer> {
    minecraftVersion("1.20.1")
}

bukkit {
    name = "SimpleRTP"
    apiVersion = "1.20"
    version = "${project.version}"
    authors = listOf("Tarna")
    main = "dev.tarna.rtp.RTP"
    load = BukkitPluginDescription.PluginLoadOrder.POSTWORLD

    commands {
        register("rtp") {
            description = "Randomly teleport to a location"
            aliases = listOf("randomteleport", "randomtp")
        }
    }
}