plugins {
    kotlin("jvm")
    id("jps-compatible")
}

dependencies {
    compile(project(":kotlin-annotations-jvm"))
    compile(project(":compiler:util"))
    compile(project(":compiler:backend-common"))
    compile(project(":compiler:frontend"))
    compile(project(":compiler:frontend.java"))
    compile(project(":compiler:serialization"))
    api(project(":compiler:backend.common.jvm"))
    compileOnly("org.jetbrains.intellij.deps:asm-all:9.1")
    compileOnly(intellijCoreDep()) { includeJars("intellij-core", "guava", rootProject = rootProject) }
    compileOnly(intellijDep()) { includeJars("trove4j", rootProject = rootProject) }
}

sourceSets {
    "main" {
        projectDefault()
    }
    "test" {}
}
