import korlibs.korge.gradle.korge

plugins {
    alias(libs.plugins.korge)
}

korge {
    id = "com.github.awcz.torch"
    name = "Torch 2D"
    title = name
    targetJvm()
    targetJs()
}