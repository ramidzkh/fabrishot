plugins {
    id("fabric-loom") version "0.8.17"
    id("org.cadixdev.licenser") version "0.6.0"
}

group = "me.ramidzkh"
version = "1.5.0"

repositories {
    maven {
        name = "TerraformersMC"
        url = uri("https://maven.terraformersmc.com/releases/")
    }

    maven {
        name = "shedaniel"
        url = uri("https://maven.shedaniel.me/")
    }
}

dependencies {
    minecraft("net.minecraft", "minecraft", "1.17")
    mappings("net.fabricmc", "yarn", "1.17+build.7", classifier = "v2")
    modImplementation("net.fabricmc", "fabric-loader", "0.11.3")

    modImplementation("net.fabricmc.fabric-api", "fabric-api", "0.35.0+1.17")
    modImplementation("com.terraformersmc", "modmenu", "2.0.0-beta.7")
    modImplementation("me.shedaniel.cloth", "cloth-config-fabric", "5.0.34")
}

java {
    sourceCompatibility = JavaVersion.VERSION_16
    targetCompatibility = JavaVersion.VERSION_16
}

license {
    setHeader(file("LICENSE"))
    include("**/*.java")
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"

        if (JavaVersion.current().isJava9Compatible) {
            options.release.set(16)
        } else {
            sourceCompatibility = "16"
            targetCompatibility = "16"
        }
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
