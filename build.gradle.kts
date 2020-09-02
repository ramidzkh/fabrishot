plugins {
    id("fabric-loom") version "0.5.12"
}

group = "me.ramidzkh"
version = "1.0.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    minecraft("net.minecraft", "minecraft", "1.16.2")
    mappings("net.fabricmc", "yarn", "1.16.2+build.26", classifier = "v2")
    modCompile("net.fabricmc", "fabric-loader", "0.9.2+build.206")
    modImplementation("net.fabricmc.fabric-api", "fabric-api", "0.19.0+build.398-1.16")
}
