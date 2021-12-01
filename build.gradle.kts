plugins {
    id("fabric-loom") version "0.10.62"
}

group = "me.ramidzkh"
version = "1.6.0"

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
    minecraft("net.minecraft", "minecraft", "1.18")
    mappings("net.fabricmc", "yarn", "1.18+build.1", classifier = "v2")
    modImplementation("net.fabricmc", "fabric-loader", "0.12.7")

    modImplementation("net.fabricmc.fabric-api", "fabric-api", "0.43.1+1.18")
    modImplementation("com.terraformersmc", "modmenu", "3.0.0")
    modImplementation("me.shedaniel.cloth", "cloth-config-fabric", "6.0.42")
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
