plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ktor) apply false
}

allprojects {
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        mavenCentral()
    }
}

// build.gradle.kts (root)
tasks.register("runAllMicroservices") {
    dependsOn(":user:run", ":order:run", ":payment:run")
    // Ensure parallel execution
    tasks.findByName(":user:run")?.mustRunAfter()
    tasks.findByName(":order:run")?.mustRunAfter()
    tasks.findByName(":payment:run")?.mustRunAfter()
}