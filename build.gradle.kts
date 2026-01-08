plugins {
    kotlin("jvm") version "1.9.24"
    application

}


group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.mysql:mysql-connector-j:8.0.33")
    testImplementation(kotlin("test"))
}
application {
    mainClass.set("org.example.MainKt")
}



tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    archiveFileName.set("PengelolaanDataMahasiswa-NurAisah.jar")

    manifest {
        attributes["Main-Class"] = "org.example.MainKt"
    }

    from(
        configurations.runtimeClasspath.get().map {
            if (it.isDirectory) it else zipTree(it)
        }
    )
}




tasks.test {
    useJUnitPlatform()
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
}

kotlin {
    jvmToolchain(17)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
