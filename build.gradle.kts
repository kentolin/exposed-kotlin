import org.gradle.api.tasks.Exec
import java.io.File

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

tasks.register("runAllMicroservices") {
    dependsOn(":user:run", ":order:run", ":payment:run")
    tasks.findByName(":user:run")?.mustRunAfter()
    tasks.findByName(":order:run")?.mustRunAfter()
    tasks.findByName(":payment:run")?.mustRunAfter()

    doFirst {
        // Clean up any existing PID files
        File("user.pid").delete()
        File("order.pid").delete()
        File("payment.pid").delete()
    }
}

