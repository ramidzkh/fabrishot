plugins {
    id("fabric-loom") version "0.5.26"
}

group = "me.ramidzkh"
version = "1.0.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    minecraft("net.minecraft", "minecraft", "1.16.3")
    mappings("net.fabricmc", "yarn", "1.16.3+build.11", classifier = "v2")
    modCompile("net.fabricmc", "fabric-loader", "0.9.3+build.207")

    modImplementation("net.fabricmc.fabric-api", "fabric-api", "0.21.0+build.407-1.16")
    modImplementation("io.github.prospector:modmenu:1.14.6+build.31")
    modImplementation("me.shedaniel.cloth:config-2:4.8.1") {
        exclude(group = "net.fabricmc.fabric-api")
    }
}
