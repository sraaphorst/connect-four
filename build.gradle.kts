plugins {
    application
    kotlin("jvm") version "2.0.0"
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "org.vorpal"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

javafx {
    version = "22.0.1"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    testImplementation(kotlin("test"))
//    implementation("org.openjfx:javafx-base:17.0.2")
//    implementation("org.openjfx:javafx-controls:17.0.2")
//    implementation("org.openjfx:javafx-graphics:17.0.2")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

application {
    // Define the main class of your application
    mainClass.set("org.vorpal.connect.MainKt")
}