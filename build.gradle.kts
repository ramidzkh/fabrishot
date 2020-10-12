plugins {
    id("fabric-loom") version "0.5.26"
}

group = "me.ramidzkh"
version = "1.2.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    minecraft("net.minecraft", "minecraft", "1.16.3")
    mappings("net.fabricmc", "yarn", "1.16.3+build.40", classifier = "v2")
    modCompile("net.fabricmc", "fabric-loader", "0.10.1+build.209")

    modImplementation("net.fabricmc.fabric-api", "fabric-api", "0.24.0+build.411-1.16")
    modImplementation("io.github.prospector:modmenu:1.14.6+build.31")
    modImplementation("me.shedaniel.cloth:config-2:4.8.2") {
        exclude(group = "net.fabricmc.fabric-api")
    }
}
