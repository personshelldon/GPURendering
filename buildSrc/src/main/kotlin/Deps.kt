@file:Suppress("InvalidPackageDeclaration")

object Deps {
    // PLUGINS
    const val gradlePlugin: String =
            "${Plugins.gradlePlugin}:${Versions.gradlePluginVersion}"
    const val kotlinPlugin: String =
            "${Plugins.kotlinPlugin}:${Versions.kotlinVersion}"
    const val fabricToolsPlugin: String =
            "${Plugins.fabricToolsPlugin}:${Versions.fabricPluginVersion}"
    const val gradleUpdatesPlugin: String =
            "${Plugins.gradleUpdatesPlugin}:${Versions.gradleUpdatesVersion}"
    const val gmsPlugin: String = "${Plugins.gmsPlugin}:${Versions.gmsVersion}"
    const val navigationArgsToolsPlugin: String =
            "${Plugins.navigationArgsToolsPlugin}:${Versions.navigationArgsPluginVersion}"

    // KAPT
    const val simpleLogKapt: String = "${Libs.simpleLogKapt}:${Versions.simpleLogVersion}"
    const val glideKapt: String = "${Libs.glideKapt}:${Versions.glideVersion}"
    const val lifecycleKapt: String = "${Libs.lifecycleKapt}:${Versions.lifecycleVersion}"
    const val roomKapt: String = "${Libs.roomKapt}:${Versions.roomVersion}"

    // LIBS
    const val annotationLib: String = "${Libs.annotationLib}:${Versions.annotationVersion}"
    const val simpleLogLib: String = "${Libs.simpleLogLib}:${Versions.simpleLogVersion}"
    // Kotlin base
    const val kotlinStdLib: String = "${Libs.kotlinStdLib}:${Versions.kotlinVersion}"
    const val kotlinReflectLib: String = "${Libs.kotlinReflectLib}:${Versions.kotlinVersion}"
    // Kotlin coroutines
    const val kotlinCoroutinesCoreLib: String =
            "${Libs.kotlinCoroutinesCoreLib}:${Versions.coroutinesVersion}"
    const val kotlinCoroutinesAndroidLib: String =
            "${Libs.kotlinCoroutinesAndroidLib}:${Versions.coroutinesVersion}"
    const val kotlinCoroutinesRxLib: String =
            "${Libs.kotlinCoroutinesRxLib}:${Versions.coroutinesVersion}"

    // Kotlin ext
    const val coreKtxLib: String = "${Libs.coreKtxLib}:${Versions.coreKtxVersion}"
    const val fragmentKtxLib: String = "${Libs.fragmentKtxLib}:" +
            Versions.fragmentKtxVersion
    // Lifecycle
    const val lifecycleLiveDataLib: String =
            "${Libs.lifecycleLiveDataLib}:${Versions.lifecycleVersion}"
    const val lifecycleLiveDataCoreLib: String =
            "${Libs.lifecycleLiveDataCoreLib}:${Versions.lifecycleVersion}"
    const val lifecycleViewModelLib: String =
            "${Libs.lifecycleViewModelLib}:${Versions.lifecycleVersion}"
    const val lifecycleViewModelKtxLib: String =
            "${Libs.lifecycleViewModelKtxLib}:${Versions.lifecycleVersion}"
    const val lifecycleReactiveLib: String =
            "${Libs.lifecycleReactiveLib}:${Versions.lifecycleVersion}"
    const val glideLib: String = "${Libs.glideLib}:${Versions.glideVersion}"
    // App compat
    const val appCompatLib: String = "${Libs.appCompatLib}:${Versions.appCompatVersion}"
    const val constraintLayoutLib: String =
            "${Libs.constraintLayoutLib}:${Versions.constraintLayoutVersion}"
    const val constraintLayoutSolverLib: String =
            "${Libs.constraintLayoutSolverLib}:${Versions.constraintLayoutVersion}"
    const val materialLib: String = "${Libs.materialLib}:${Versions.materialVersion}"
    // Navigation
    const val navigationFragmentKtxLib: String =
            "${Libs.navigationFragmentKtxLib}:${Versions.navigationVersion}"
    const val navigationUiKtxLib: String = "${Libs.navigationUiKtxLib}:" +
            Versions.navigationVersion
    const val gsonLib: String = "${Libs.gsonLib}:${Versions.gsonVersion}"
    // Koin
    const val koinScopeLib: String = "${Libs.koinScopeLib}:${Versions.koinVersion}"
    const val koinViewModelLib: String = "${Libs.koinViewModelLib}:${Versions.koinVersion}"
    // Rx
    const val rxAndroidLib: String = "${Libs.rxAndroidLib}:${Versions.rxAndroidVersion}"
    const val rxJavaLib: String = "${Libs.rxJavaLib}:${Versions.rxJavaVersion}"
    const val rxKotlinLib: String = "${Libs.rxKotlinLib}:${Versions.rxKotlinVersion}"
    // Jetty
    const val jettyClientLib: String = "${Libs.jettyClientLib}:${Versions.jettyVersion}"
    const val jettyServletLib: String = "${Libs.jettyServletLib}:${Versions.jettyVersion}"
    const val jettyServerLib: String = "${Libs.jettyServerLib}:${Versions.jettyVersion}"
    // Room
    const val roomRuntimeLib: String = "${Libs.roomRuntimeLib}:${Versions.roomVersion}"
    const val roomRxLib: String = "${Libs.roomRxLib}:${Versions.roomVersion}"
    // Leak/block canary
    const val leakCanaryLib: String = "${Libs.leakCanaryLib}:${Versions.leakCanaryVersion}"
    const val leakCanaryWatcherLib: String =
            "${Libs.leakCanaryWatcherLib}:${Versions.leakCanaryVersion}"
    const val blockCanaryLib: String = "${Libs.blockCanaryLib}:" +
            Versions.blockCanaryVersion
    const val blockCanaryNoOpLib: String = "${Libs.blockCanaryNoOpLib}:" +
            Versions.blockCanaryVersion
    const val firebaseCoreLib: String = "${Libs.firebaseCoreLib}:" +
            Versions.firebaseVersion
    // OkHttp
    const val okHttpLib: String = "${Libs.okHttpLib}:${Versions.okHttpVersion}"
    const val okHttpLoggingLib: String = "${Libs.okHttpLoggingLib}:" +
            Versions.okHttpVersion
    // Retrofit
    const val retrofitLib: String = "${Libs.retrofitLib}:${Versions.retrofitVersion}"
    const val retrofitGsonLib: String = "${Libs.retrofitGsonLib}:" +
            Versions.retrofitVersion
    // Work Manager
    const val workManagerLib: String = "${Libs.workManagerLib}:" +
            Versions.workManagerVersion
    const val workManagerKtxLib: String = "${Libs.workManagerKtxLib}:" +
            Versions.workManagerVersion
    // Crashlytics
    const val crashlyticsLib: String = "${Libs.crashlyticsLib}:" +
            "${Versions.crashlyticsVersion}@aar"
    const val crashlyticsNdkLib: String =
            "${Libs.crashlyticsNdkLib}:${Versions.crashlyticsNdkVersion}@aar"

    // TESTS
    const val orchestratorLib: String =
            "${Libs.orchestratorLib}:${Versions.orchestratorVersion}"
    const val mockitoLib: String = "${Libs.mockitoLib}:${Versions.mockitoVersion}"
    const val powerMockApiMockitoLib: String =
            "${Libs.powerMockApiMockitoLib}:${Versions.powerMockVersion}"
    const val powerMockJunitLib: String = "${Libs.powerMockJunitLib}:${Versions.powerMockVersion}"
    const val powerMockJunitRuleLib: String =
            "${Libs.powerMockJunitRuleLib}:${Versions.powerMockVersion}"
    const val powerMockXstreamLib: String =
            "${Libs.powerMockXstreamLib}:${Versions.powerMockVersion}"
    const val junitLib: String = "${Libs.junitLib}:${Versions.junitVersion}"
    const val robolectricLib: String = "${Libs.robolectricLib}:" +
            Versions.robolectricVersion
    const val koinTestLib: String = "${Libs.koinTestLib}:${Versions.koinVersion}"
    const val roomTestLib: String = "${Libs.roomTestLib}:${Versions.roomVersion}"
    const val testCoreLib: String = "${Libs.testCoreLib}:${Versions.testCoreVersion}"
    const val testRunnerLib: String = "${Libs.testRunnerLib}:${Versions.testRunnerVersion}"
    const val testRulesLib: String = "${Libs.testRulesLib}:${Versions.testRulesVersion}"
    const val testExtJunitLib: String = "${Libs.testExtJunitLib}:" +
            Versions.testJunitVersion
    const val testExtTruthLib: String = "${Libs.testExtTruthLib}:" +
            Versions.testTruthVersion
    const val googleTruthLib: String = "${Libs.googleTruthLib}:" +
            Versions.googleTruthVersion
    const val mockkLib: String = "${Libs.mockkLib}:${Versions.mockkVersion}"
    const val okHttpMockServerLib: String = "${Libs.okHttpMockServerLib}:" +
            Versions.okHttpVersion

}
