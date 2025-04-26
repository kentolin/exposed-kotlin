pluginManagement {
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}



rootProject.name = "exposed-kotlin"


include(":mb")
include(":di")
include(":database")
include(":user")
include(":order")
include(":payment")

