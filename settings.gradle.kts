pluginManagement {
    repositories { mavenLocal(); mavenCentral(); google(); gradlePluginPortal() }
}

buildscript {
    repositories { mavenLocal(); mavenCentral(); google(); gradlePluginPortal() }

    dependencies {
        classpath("com.soywiz.korge.settings:com.soywiz.korge.settings.gradle.plugin:5.3.2")
    }
}

apply(plugin = "com.soywiz.korge.settings")
