plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(project(":common"))
    implementation(project(":database"))
    implementation(project(":order"))

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.serialization.kotlinx.json)

    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)

    implementation(libs.exposed.java.time)

}