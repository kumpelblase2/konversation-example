plugins {
    kotlin("jvm") version "1.5.31"
    id("com.github.johnrengelman.shadow") version "2.0.4"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.eternalwings.de/releases/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly(kotlin("stdlib"))
    implementation("de.eternalwings.bukkit:konversation:1.0.0")
    compileOnly("org.spigotmc:spigot-api:1.17.1-R0.1-SNAPSHOT")
}

tasks.create<Copy>("deployToServer") {
    dependsOn(tasks.clean)
    dependsOn(tasks.shadowJar)
    from("${project.buildDir}/libs/")
    into("${project.projectDir}/server/plugins/")
}
