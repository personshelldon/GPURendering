plugins {
    id(Plugins.applicationPlugin)
    id(Plugins.kotlinAndroidPlugin)
    id(Plugins.kotlinAndroidExtPlugin)
    id(Plugins.kotlinKaptPlugin)
}

android {
    compileSdkVersion(Config.compileSdkVersion)
    buildToolsVersion(Config.buildToolsVersion)

    defaultConfig {
        applicationId = Config.applicationId
        minSdkVersion(Config.minSdkVersion)
        targetSdkVersion(Config.targetSdkVersion)
        versionCode = 1_00_00 * Config.majorVersion +
                1_00 * Config.minorVersion +
                Config.buildVersion
        versionName = "${Config.majorVersion}.${Config.minorVersion}.${Config.buildVersion}"
    }
    buildTypes {
        getByName("release") {
            isDebuggable = false
            isJniDebuggable = false
            isRenderscriptDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            isZipAlignEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
            proguardFiles("proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(Deps.kotlinStdLib)
    implementation(Deps.appCompatLib)
    implementation(Deps.coreKtxLib)
    constraintLayoutLibs()
    workManagerLibs()
    simpleLogLib()
    koinLibs()

    implementation(project(":utils"))
}
