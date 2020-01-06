@file:Suppress("InvalidPackageDeclaration")

import org.gradle.api.Action
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.accessors.runtime.addDependencyTo
import org.gradle.kotlin.dsl.exclude

private const val KAPT_CONFIGURATION = "kapt"
private const val IMPLEMENTATION_CONFIGURATION = "implementation"
private const val DEBUG_IMPLEMENTATION_CONFIGURATION = "debugImplementation"
private const val RELEASE_IMPLEMENTATION_CONFIGURATION = "releaseImplementation"
private const val TEST_IMPLEMENTATION_CONFIGURATION = "testImplementation"

fun DependencyHandlerScope.simpleLogLib(): Dependency? {
    add(KAPT_CONFIGURATION, Deps.simpleLogKapt)
    return add(IMPLEMENTATION_CONFIGURATION, Deps.simpleLogLib)
}

fun DependencyHandlerScope.glideLib(): Dependency? {
    add(KAPT_CONFIGURATION, Deps.glideKapt)
    return add(IMPLEMENTATION_CONFIGURATION, Deps.glideLib)
}

fun DependencyHandlerScope.lifecycleLibs(): Dependency? {
    add(KAPT_CONFIGURATION, Deps.lifecycleKapt)
    add(IMPLEMENTATION_CONFIGURATION, Deps.lifecycleLiveDataLib)
    add(IMPLEMENTATION_CONFIGURATION, Deps.lifecycleLiveDataCoreLib)
    add(IMPLEMENTATION_CONFIGURATION, Deps.lifecycleViewModelLib)
    add(IMPLEMENTATION_CONFIGURATION, Deps.lifecycleViewModelKtxLib)
    return add(IMPLEMENTATION_CONFIGURATION, Deps.lifecycleReactiveLib)
}

fun DependencyHandlerScope.kotlinCoroutinesLibs(): Dependency? {
    add(IMPLEMENTATION_CONFIGURATION, Deps.kotlinCoroutinesCoreLib)
    add(IMPLEMENTATION_CONFIGURATION, Deps.kotlinCoroutinesAndroidLib)
    return add(IMPLEMENTATION_CONFIGURATION, Deps.kotlinCoroutinesRxLib)
}

fun DependencyHandlerScope.constraintLayoutLibs(): Dependency? {
    add(IMPLEMENTATION_CONFIGURATION, Deps.constraintLayoutLib)
    return add(IMPLEMENTATION_CONFIGURATION, Deps.constraintLayoutSolverLib)
}

fun DependencyHandlerScope.navigationLibs(): Dependency? {
    add(IMPLEMENTATION_CONFIGURATION, Deps.navigationFragmentKtxLib)
    return add(IMPLEMENTATION_CONFIGURATION, Deps.navigationUiKtxLib)
}

fun DependencyHandlerScope.koinLibs(): Dependency? {
    add(IMPLEMENTATION_CONFIGURATION, Deps.koinScopeLib)
    addDependencyTo(this,
            TEST_IMPLEMENTATION_CONFIGURATION,
            Deps.koinTestLib,
            Action<ExternalModuleDependency> { exclude("org.mockito") }
    )
    return add(IMPLEMENTATION_CONFIGURATION, Deps.koinViewModelLib)
}

fun DependencyHandlerScope.rxLibs(): Dependency? {
    add(IMPLEMENTATION_CONFIGURATION, Deps.rxJavaLib)
    add(IMPLEMENTATION_CONFIGURATION, Deps.rxAndroidLib)
    return add(IMPLEMENTATION_CONFIGURATION, Deps.rxKotlinLib)
}

fun DependencyHandlerScope.jettyLibs(): Dependency? {
    add(IMPLEMENTATION_CONFIGURATION, Deps.jettyClientLib)
    add(IMPLEMENTATION_CONFIGURATION, Deps.jettyServerLib)
    return add(IMPLEMENTATION_CONFIGURATION, Deps.jettyServletLib)
}

fun DependencyHandlerScope.roomLibs(): Dependency? {
    add(KAPT_CONFIGURATION, Deps.roomKapt)
    add(TEST_IMPLEMENTATION_CONFIGURATION, Deps.roomTestLib)
    add(IMPLEMENTATION_CONFIGURATION, Deps.roomRuntimeLib)
    return add(IMPLEMENTATION_CONFIGURATION, Deps.roomRxLib)
}

fun DependencyHandlerScope.leakCanaryLibs(): Dependency? {
    add(DEBUG_IMPLEMENTATION_CONFIGURATION, Deps.leakCanaryLib)
    return add(IMPLEMENTATION_CONFIGURATION, Deps.leakCanaryWatcherLib)
}

fun DependencyHandlerScope.blockCanaryLibs(): Dependency? {
    add(DEBUG_IMPLEMENTATION_CONFIGURATION, Deps.blockCanaryLib)
    return add(RELEASE_IMPLEMENTATION_CONFIGURATION, Deps.blockCanaryNoOpLib)
}

fun DependencyHandlerScope.okHttpLibs(): Dependency? {
    add(IMPLEMENTATION_CONFIGURATION, Deps.okHttpLib)
    return add(IMPLEMENTATION_CONFIGURATION, Deps.okHttpLoggingLib)
}

fun DependencyHandlerScope.retrofitLibs(): Dependency? {
    add(IMPLEMENTATION_CONFIGURATION, Deps.retrofitLib)
    add(TEST_IMPLEMENTATION_CONFIGURATION, Deps.okHttpMockServerLib)
    return add(IMPLEMENTATION_CONFIGURATION, Deps.retrofitGsonLib)
}

fun DependencyHandlerScope.workManagerLibs(): Dependency? {
    add(IMPLEMENTATION_CONFIGURATION, Deps.workManagerLib)
    return add(IMPLEMENTATION_CONFIGURATION, Deps.workManagerKtxLib)
}

fun DependencyHandlerScope.crashlyticsLibs(): Dependency? {
    addDependencyTo(this,
            IMPLEMENTATION_CONFIGURATION,
            Deps.crashlyticsLib,
            Action<ExternalModuleDependency> { isTransitive = true }
    )
    return addDependencyTo(this,
            IMPLEMENTATION_CONFIGURATION,
            Deps.crashlyticsNdkLib,
            Action<ExternalModuleDependency> { isTransitive = true }
    )
}

// Tests
fun DependencyHandlerScope.coreAndroidTests(): Dependency? {
    add(TEST_IMPLEMENTATION_CONFIGURATION, Deps.testCoreLib)
    add(TEST_IMPLEMENTATION_CONFIGURATION, Deps.testRunnerLib)
    add(TEST_IMPLEMENTATION_CONFIGURATION, Deps.testRulesLib)
    add(TEST_IMPLEMENTATION_CONFIGURATION, Deps.testExtJunitLib)
    add(TEST_IMPLEMENTATION_CONFIGURATION, Deps.testExtTruthLib)
    return add(TEST_IMPLEMENTATION_CONFIGURATION, Deps.googleTruthLib)
}

fun DependencyHandlerScope.powerMockTests(): Dependency? {
    add(TEST_IMPLEMENTATION_CONFIGURATION, Deps.mockitoLib)
    add(TEST_IMPLEMENTATION_CONFIGURATION, Deps.powerMockApiMockitoLib)
    add(TEST_IMPLEMENTATION_CONFIGURATION, Deps.powerMockJunitLib)
    add(TEST_IMPLEMENTATION_CONFIGURATION, Deps.powerMockJunitRuleLib)
    return add(TEST_IMPLEMENTATION_CONFIGURATION, Deps.powerMockXstreamLib)
}
