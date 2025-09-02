import dev.slne.surf.surfapi.gradle.util.withSurfApiBukkit

        plugins {
            id("dev.slne.surf.surfapi.gradle.paper-plugin")
        }

        dependencies {
            implementation("com.github.stefvanschie.inventoryframework:IF:0.11.3")
        }

group = "de.schnitzel"

surfPaperPluginApi {
    mainClass("de.schnitzel.nutrition.Nutrition")
    generateLibraryLoader(false)
    authors.add("Schnitzel")

    runServer {
        withSurfApiBukkit()
    }
}