plugins {
    id(Plugins.libraryPlugin)
    id(Plugins.kotlinAndroidPlugin)
    id(Plugins.kotlinAndroidExtPlugin)
    id(Plugins.kotlinKaptPlugin)
}

android {

    compileSdkVersion(Config.compileSdkVersion)
    buildToolsVersion(Config.buildToolsVersion)

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }

    defaultConfig {
        versionCode = 2
        versionName = "2.0.0"

        minSdkVersion(Config.minSdkVersion)
        targetSdkVersion(Config.targetSdkVersion)
        consumerProguardFiles("proguard-rules.pro")
    }
}

dependencies {
    testImplementation(Deps.junitLib)
    testImplementation(Deps.robolectricLib)
    powerMockTests()

    simpleLogLib()
    implementation(Deps.annotationLib)
    implementation(Deps.kotlinStdLib)
}
