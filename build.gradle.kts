plugins {
    id("fabric-loom") version "0.12-SNAPSHOT"
}

group = "me.ramidzkh"
version = "1.8.0"

repositories {
    maven {
        name = "TerraformersMC"
        url = uri("https://maven.terraformersmc.com/releases/")

        content {
            includeGroup("com.terraformersmc")
        }
    }

    maven {
        name = "shedaniel"
        url = uri("https://maven.shedaniel.me/")

        content {
            includeGroup("me.shedaniel.cloth")
        }
    }
}

dependencies {
    minecraft("net.minecraft", "minecraft", "1.19")
    mappings("net.fabricmc", "yarn", "1.19+build.1")
    modImplementation("net.fabricmc", "fabric-loader", "0.14.7")

    modImplementation("net.fabricmc.fabric-api", "fabric-api", "0.55.3+1.19")
    modImplementation("com.terraformersmc", "modmenu", "4.0.0")
    modImplementation("me.shedaniel.cloth", "cloth-config-fabric", "7.0.69")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(17)
    }

    withType<AbstractArchiveTask> {
        from(rootProject.file("LICENSE"))
    }

    processResources {
        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
            expand("version" to project.version)
        }
    }
}
