import groovyjarjarantlr4.v4.runtime.misc.RuleDependencyChecker.checkDependencies

plugins {
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.compose) apply false
    id("com.google.gms.google-services") version "4.4.4" apply false
}

subprojects {
    pluginManager.withPlugin("com.android.application") {
        configure<com.android.build.gradle.BaseExtension> {
            lintOptions {
                disable("NewerVersionAvailable")
                disable("GradleDependency")
                disable("AndroidGradlePluginVersion")
            }
        }
    }

    pluginManager.withPlugin("com.android.library") {
        configure<com.android.build.gradle.BaseExtension> {
            lintOptions {
                disable("NewerVersionAvailable")
                disable("GradleDependency")
                disable("AndroidGradlePluginVersion")
            }
        }
    }
}