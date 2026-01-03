plugins {
    id("kool.androidlib-conventions")
    id("kool.lib-conventions")
    id("kool.publish-conventions")
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrainsCompose)
}

kotlin {
    compilerOptions {
        optIn.add("de.fabmax.kool.modules.compose.InternalKoolComposeAPI")
    }
    sourceSets {
        commonMain.dependencies {
            api(libs.kotlin.coroutines)
            api(compose.runtime)
            implementation("me.dvyy.compose.minimal:runtime")
            implementation("me.dvyy.compose.minimal:modifier")
            implementation("me.dvyy.compose.minimal:modifier-composed")
            implementation(project(":kool-core"))
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
