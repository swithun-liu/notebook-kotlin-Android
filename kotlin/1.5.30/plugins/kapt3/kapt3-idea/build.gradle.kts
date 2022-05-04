
plugins {
    kotlin("jvm")
    id("jps-compatible")
}

dependencies {
    compile(kotlinStdlib())

    compileOnly(project(":compiler:frontend"))
    compileOnly(project(":idea")) { isTransitive = false }
    compileOnly(project(":idea:kotlin-gradle-tooling"))
    compileOnly(project(":idea:idea-core"))
    compileOnly(project(":idea:idea-gradle"))
    compileOnly("org.jetbrains.intellij.deps:asm-all:9.1")
    compileOnly(intellijDep())
    compileOnly(intellijPluginDep("java"))
    compileOnly(intellijPluginDep("gradle"))
    compileOnly(intellijPluginDep("android"))
}

sourceSets {
    "main" { projectDefault() }
    "test" {}
}

runtimeJar()

sourcesJar()

javadocJar()
