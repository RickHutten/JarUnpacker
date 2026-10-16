import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.intellij.platform")
}

group = "io.github.rickhutten"
version = "0.1.0"

dependencies {
    intellijPlatform {
        intellijIdea("2025.3.6.1")
        testFramework(TestFrameworkType.Platform)
    }
}
