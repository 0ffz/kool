plugins {
    id("kool.androidlib-conventions")
    id("kool.lib-conventions")
    id("kool.publish-conventions")
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrainsCompose)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.kotlin.coroutines)
            api(compose.runtime)
            implementation(project(":kool-core"))
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
