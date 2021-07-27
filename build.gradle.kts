plugins {
    id("fabric-loom") version "0.9.17"
    id("org.cadixdev.licenser") version "0.6.1"
}

group = "me.ramidzkh"
version = "1.5.2"

repositories {
    maven {
        name = "TerraformersMC"
        url = uri("https://maven.quiltmc.org/repository/release/")

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
    minecraft("net.minecraft", "minecraft", "1.17.1")
    mappings("net.fabricmc", "yarn", "1.17.1+build.1", classifier = "v2")
    modImplementation("net.fabricmc", "fabric-loader", "0.11.6")

    modImplementation("net.fabricmc.fabric-api", "fabric-api", "0.36.1+1.17")
    modImplementation("com.terraformersmc", "modmenu", "v2.0.2+1.17.2035ad2")
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
        options.release.set(16)
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
