import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.maven.publish)
}

group = "io.github.andannn"
version = "1.0.2"

kotlin {
    android {
        namespace = "io.github.andannn"
        compileSdk = 36

        withDeviceTestBuilder {}.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    )

    jvm {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_11)
                }
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime.retain)
        }

        getByName("androidDeviceTest").dependencies {
            implementation(libs.compose.ui.test.junit4.android)
            implementation(libs.compose.ui.test.manifest)
            implementation(libs.jetbrains.compose.material3)
            implementation(libs.kotlin.test)
            implementation(libs.espresso.core)
        }
    }
}

mavenPublishing {
    coordinates(group.toString(), name, version.toString())

    pom {
        name = "RetainedModel"
        description = "A small helper for build retained model similar to android ViewModel."
        url = "https://github.com/andannn/RetainedModel"

        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        developers {
            developer {
                id.set("andannn")
                name.set("Andannn")
            }
        }

        scm {
            url = "https://github.com/andannn/RetainedModel.git"
            connection = "scm:git:git://github.com/andannn/RetainedModel.git"
            developerConnection = "scm:git:ssh://git@github.com/andannn/RetainedModel.git"
        }
    }
}
