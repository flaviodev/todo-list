val kotlin_version: String  by project
val logback_version: String by project
val jackson_version: String by project
val kluent_version: String by project
val spek_version: String by project

buildscript {
    repositories {
        maven {
            url = uri ("https://plugins.gradle.org/m2/")
        }
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.61")
    }
}

plugins {
    java
}

allprojects {
    group = "com.flaviodev"
    version = "0.0.1"

    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        mavenLocal()
        jcenter()
        maven { url = uri("https://kotlin.bintray.com/ktor") }
    }

    dependencies {
        implementation("ch.qos.logback:logback-classic:$logback_version")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jackson_version")
        implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jackson_version")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")

        implementation("org.amshove.kluent:kluent:$kluent_version")
        implementation("org.spekframework.spek2:spek-dsl-jvm:$spek_version")

        implementation("org.spekframework.spek2:spek-runner-junit5:$spek_version")
    }
}

subprojects {
    version = "1.0"
}

project(":todolist-shared") {

}

project(":todolist-restapi") {
    dependencies {
        implementation(project(":todolist-shared"))
    }
}