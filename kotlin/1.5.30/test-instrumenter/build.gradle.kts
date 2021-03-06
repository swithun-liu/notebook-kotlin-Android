import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.pill.PillExtension

plugins {
    java
    kotlin("jvm")
    id("jps-compatible")
}

pill {
    variant = PillExtension.Variant.FULL
}

dependencies {
    compile(kotlinStdlib())
    compileOnly(intellijCoreDep()) { includeJars("intellij-core") }
    compileOnly("org.jetbrains.intellij.deps:asm-all:9.1")
}

sourceSets {
    "main" { projectDefault() }
}

tasks {
    "jar" {
        this as Jar
        manifest {
            attributes["Manifest-Version"] = 1.0
            attributes["PreMain-Class"] = "org.jetbrains.kotlin.testFramework.TestInstrumentationAgent"
        }
    }
}