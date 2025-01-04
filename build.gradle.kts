import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import xyz.jpenilla.runpaper.task.RunServer

plugins {
    kotlin("jvm") version "2.0.20"

    id("com.gradleup.shadow") version "8.3.5"

    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "dev.tarna"
version = "1.1.0"

repositories {
    maven("https://jitpack.io")
}

repositories {
    mavenCentral()

    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.21.4-R0.1-SNAPSHOT")
}

kotlin {
    jvmToolchain(21)
}

tasks.withType<RunServer> {
    minecraftVersion("1.21.4")
    jvmArgs("-DPaper.IgnoreJavaVersion=true", "-Dcom.mojang.eula.agree=true")
}

bukkit {
    name = "SimpleRTP"
    apiVersion = "1.21"
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