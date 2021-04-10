plugins {
    id("fabric-loom") version "0.6.48"
    id("org.cadixdev.licenser") version "0.5.0"
}

group = "me.ramidzkh"
version = "1.4.0"

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
    minecraft("net.minecraft", "minecraft", "1.16.5")
    mappings("net.fabricmc", "yarn", "1.16.5+build.6", classifier = "v2")
    modImplementation("net.fabricmc", "fabric-loader", "0.11.3")

    modImplementation("net.fabricmc.fabric-api", "fabric-api", "0.32.5+1.16")
    modImplementation("com.terraformersmc", "modmenu", "1.16.9")
    modImplementation("me.shedaniel.cloth", "cloth-config-fabric", "4.11.19")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

license {
    header = file("LICENSE")
    include("**/*.java")
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"

        if (JavaVersion.current().isJava9Compatible) {
            options.release.set(8)
        } else {
            sourceCompatibility = "8"
            targetCompatibility = "8"
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
