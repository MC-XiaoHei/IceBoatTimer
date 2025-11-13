plugins {
    kotlin("jvm") version "2.2.21"
    kotlin("plugin.serialization") version "1.9.22"
    id("com.gradleup.shadow") version "9.2.2"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "cn.xor7.xiaohei"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
}

@Suppress("VulnerableLibrariesLocal")
dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("dev.jorel:commandapi-spigot-shade:11.0.0")
    implementation("dev.jorel:commandapi-kotlin-spigot:11.0.0")
}

tasks.runServer {
    minecraftVersion("1.20.4")
}

val targetJavaVersion = 17
kotlin {
    jvmToolchain(targetJavaVersion)
}

tasks.build {
    dependsOn("shadowJar")
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("paper-plugin.yml") {
        expand(props)
    }
}

tasks.shadowJar {
    relocate("dev.jorel.commandapi", "cn.xor7.xiaohei.iceBoatTimer.libs.commandapi")
}