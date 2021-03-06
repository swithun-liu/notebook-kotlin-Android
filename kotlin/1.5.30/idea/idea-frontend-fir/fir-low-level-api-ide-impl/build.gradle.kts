plugins {
    kotlin("jvm")
    id("jps-compatible")
}

dependencies {
    compile(project(":compiler:psi"))
    compile(project(            ":idea-frontend-fir:idea-fir-low-level-api"))
    compile(project(":idea:idea-frontend-independent"))
    compile(project(":idea-frontend-api"))
    compile(project(":idea:idea-core"))
    compile(project(":compiler:fir:fir2ir"))
    compile(project(":compiler:fir:fir2ir:jvm-backend"))
    compile(project(":compiler:ir.serialization.common"))
    compile(project(":compiler:fir:resolve"))
    compile(project(":compiler:fir:checkers"))
    compile(project(":compiler:fir:checkers:checkers.jvm"))
    compile(project(":compiler:fir:java"))
    compile(project(":compiler:fir:jvm"))
    implementation(project(":compiler:ir.psi2ir"))
    implementation(project(":compiler:fir:entrypoint"))
    compile("org.jetbrains.intellij.deps:asm-all:9.1")
    compile(intellijDep())
    compile(intellijCoreDep())

// <temp>
    compile(project(":idea:idea-core"))
// </temp>

// <neededFor>`AbstractFirLazyResolveTest` uses fir implementation of references which are not in classpath otherwise
    testRuntimeOnly(project(":idea-frontend-fir"))
// </neededFor>
    testCompile(projectTests(":compiler:test-infrastructure-utils"))
    testCompile(projectTests(":compiler:test-infrastructure"))
    testCompile(projectTests(":compiler:tests-common-new"))

    testImplementation("org.opentest4j:opentest4j:1.2.0")
    testCompile(toolsJar())
    testCompile(projectTests(":idea"))
    testCompile(project(":idea:idea-fir"))
    testCompile(projectTests(":compiler:tests-common"))
    testCompile(projectTests(":compiler:fir:analysis-tests:legacy-fir-tests"))
    testCompile(projectTests(":idea:idea-test-framework"))
    testCompile(project(":kotlin-test:kotlin-test-junit"))
    testCompile(commonDep("junit:junit"))

    compile(intellijPluginDep("java"))
}

sourceSets {
    "main" { projectDefault() }
    "test" { projectDefault() }
}

projectTest(parallel = true ) {
    dependsOn(":dist")
    workingDir = rootDir
    val useFirIdeaPlugin = kotlinBuildProperties.useFirIdeaPlugin
    doFirst {
        if (!useFirIdeaPlugin) {
            error("Test task in the module should be executed with -Pidea.fir.plugin=true")
        }
    }
}

testsJar()

