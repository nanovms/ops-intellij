import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.intellij") version "0.7.3"
    kotlin("jvm") version "1.4.32"
}

group = "com.nanovms.ops"
version = "1.0.5"

listOf("compileKotlin", "compileTestKotlin").forEach {
    tasks.getByName<KotlinCompile>(it) {
        kotlinOptions.jvmTarget = "1.8"
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version = "2021.1.1"
}
tasks.getByName<org.jetbrains.intellij.tasks.PatchPluginXmlTask>("patchPluginXml") {
    changeNotes("""
      Add change notes here.<br>
      <em>most HTML tags may be used</em>""")
}