description = "Kotlin Serialization Compiler Plugin"

plugins {
    kotlin("jvm")
    id("jps-compatible")
}

dependencies {
    compileOnly("org.jetbrains.intellij.deps:asm-all:9.1")
    compileOnly(intellijCoreDep()) { includeJars("intellij-core", rootProject = rootProject) }

    compileOnly(project(":compiler:plugin-api"))
    compileOnly(project(":compiler:cli-common"))
    compileOnly(project(":compiler:frontend"))
    compileOnly(project(":compiler:backend"))
    compileOnly(project(":compiler:ir.backend.common"))
    compileOnly(project(":js:js.frontend"))
    compileOnly(project(":js:js.translator"))
    compileOnly(project(":kotlin-util-klib-metadata"))

    runtimeOnly(kotlinStdlib())

    testCompile(projectTests(":compiler:tests-common"))
    testCompile(commonDep("junit:junit"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.1.0")

    testRuntimeOnly(intellijCoreDep()) { includeJars("intellij-core") }
    testRuntimeOnly(intellijDep()) { includeJars("platform-concurrency") }
}

sourceSets {
    "main" { projectDefault() }
    "test" { projectDefault() }
}

tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>> {
    kotlinOptions {
        freeCompilerArgs += "-Xopt-in=org.jetbrains.kotlin.ir.ObsoleteDescriptorBasedAPI"
    }
}

runtimeJar()
sourcesJar()
javadocJar()
testsJar()

projectTest(parallel = true) {
    workingDir = rootDir
}
