plugins {
    `kotlin-dsl`
}

allprojects {
    repositories {
        jcenter()
        google()
    }
}

gradlePlugin {
    plugins {
        register("com.don11995.symbols") {
            id = "com.don11995.symbols"
            implementationClass = "com.don11995.symbols.Symbols"
            version = "1.0"
        }
        register("com.don11995.codestyle") {
            id = "com.don11995.codestyle"
            implementationClass = "com.don11995.codestyle.CodeStyle"
            version = "1.0"
        }
        register("com.don11995.security") {
            id = "com.don11995.security"
            implementationClass = "com.don11995.security.Security"
            version = "1.0"
        }
        register("com.don11995.filter") {
            id = "com.don11995.filter"
            implementationClass = "com.don11995.filter.VariantFilter"
            version = "1.0"
        }
    }
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}

dependencies {
    implementation("com.android.tools.build:gradle:3.5.1")
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.0.1")
}
