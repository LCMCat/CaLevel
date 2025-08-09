plugins {
    kotlin("jvm") version "2.2.20-Beta2"
    id("com.gradleup.shadow") version "8.3.0"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "tech.ccat.calevel"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
}

dependencies {
    compileOnly(files("lib/kstats-1.0-SNAPSHOT.jar"))
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
    implementation("org.mongodb:mongodb-driver-sync:5.4.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

tasks {
    runServer {
        // Configure the Minecraft version for our task.
        // This is the only required configuration besides applying the plugin.
        // Your plugin's jar (or shadowJar if present) will be used automatically.
        minecraftVersion("1.20")
    }
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
    filesMatching("plugin.yml") {
        expand(props)
    }
}


tasks.shadowJar {
    archiveClassifier.set("")
    minimize()

    // 重定位Kotlin库防止冲突
    relocate("kotlin", "tech.ccat.calevel.shaded.kotlin")
    relocate("com.mongodb", "tech.ccat.calevel.shaded.mongodb") {
        exclude("com.mongodb.driver.websocket.**")
    }
    // 排除不需要的依赖
    exclude("META-INF/maven/**")
    exclude("META-INF/versions/**")

    // 合并服务文件（重要！）
    mergeServiceFiles()
}