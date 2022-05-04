/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.idea.debugger.test

import com.intellij.debugger.impl.DescriptorTestCase
import com.intellij.debugger.impl.OutputChecker
import com.intellij.execution.configurations.JavaParameters
import com.intellij.execution.process.ProcessOutputTypes
import com.intellij.openapi.roots.LibraryOrderEntry
import com.intellij.openapi.roots.ModifiableRootModel
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.roots.OrderRootType
import com.intellij.openapi.roots.ui.configuration.libraryEditor.NewLibraryEditor
import com.intellij.openapi.util.ThrowableComputable
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.PsiFile
import com.intellij.testFramework.EdtTestUtil
import com.intellij.xdebugger.XDebugSession
import org.jetbrains.kotlin.codegen.forTestCompile.ForTestCompileRuntime
import org.jetbrains.kotlin.config.JvmTarget
import org.jetbrains.kotlin.idea.debugger.evaluate.KotlinDebuggerCaches
import org.jetbrains.kotlin.idea.debugger.evaluate.compilation.CodeFragmentCompiler
import org.jetbrains.kotlin.idea.debugger.test.preference.*
import org.jetbrains.kotlin.idea.debugger.test.util.BreakpointCreator
import org.jetbrains.kotlin.idea.debugger.test.util.KotlinOutputChecker
import org.jetbrains.kotlin.idea.debugger.test.util.LogPropagator
import org.jetbrains.kotlin.idea.test.ConfigLibraryUtil
import org.jetbrains.kotlin.idea.test.PluginTestCaseBase
import org.jetbrains.kotlin.test.*
import org.jetbrains.kotlin.test.KotlinBaseTest.TestFile
import org.jetbrains.kotlin.test.testFramework.runWriteAction
import org.jetbrains.kotlin.test.util.KtTestUtil
import org.jetbrains.kotlin.test.TargetBackend
import org.junit.ComparisonFailure
import java.io.File

internal const val KOTLIN_LIBRARY_NAME = "KotlinLibrary"
internal const val TEST_LIBRARY_NAME = "TestLibrary"

class TestFiles(val originalFile: File, val wholeFile: TestFile, files: List<TestFile>) : List<TestFile> by files

@WithMutedInDatabaseRunTest
abstract class KotlinDescriptorTestCase : DescriptorTestCase() {
    private lateinit var testAppDirectory: File
    private lateinit var sourcesOutputDirectory: File

    private lateinit var librarySrcDirectory: File
    private lateinit var libraryOutputDirectory: File

    private lateinit var mainClassName: String

    override fun getTestAppPath(): String = testAppDirectory.absolutePath
    override fun getTestProjectJdk() = PluginTestCaseBase.fullJdk()

    private fun systemLogger(message: String) = println(message, ProcessOutputTypes.SYSTEM)

    private var breakpointCreator: BreakpointCreator? = null
    private var logPropagator: LogPropagator? = null

    private var oldValues: OldValuesStorage? = null

    override fun runBare() {
        testAppDirectory = KtTestUtil.tmpDir("debuggerTestSources")
        sourcesOutputDirectory = File(testAppDirectory, "src").apply { mkdirs() }

        librarySrcDirectory = File(testAppDirectory, "libSrc").apply { mkdirs() }
        libraryOutputDirectory = File(testAppDirectory, "lib").apply { mkdirs() }

        super.runBare()
    }

    override fun setUp() {
        super.setUp()

        KotlinDebuggerCaches.LOG_COMPILATIONS = true
        logPropagator = LogPropagator(::systemLogger).apply { attach() }
    }

    override fun tearDown() {
        KotlinDebuggerCaches.LOG_COMPILATIONS = false

        oldValues?.revertValues()
        oldValues = null

        detachLibraries()

        logPropagator?.detach()
        logPropagator = null

        super.tearDown()
    }

    open fun useIrBackend() = false

    fun doTest(path: String) {
        val wholeFile = File(path)
        val wholeFileContents = FileUtil.loadFile(wholeFile, true)

        val testFiles = createTestFiles(wholeFile, wholeFileContents)
        val preferences = DebuggerPreferences(myProject, wholeFileContents)

        oldValues = SettingsMutators.mutate(preferences)

        val rawJvmTarget = preferences[DebuggerPreferenceKeys.JVM_TARGET]
        val jvmTarget = JvmTarget.fromString(rawJvmTarget) ?: error("Invalid JVM target value: $rawJvmTarget")

        val compilerFacility = DebuggerTestCompilerFacility(testFiles, jvmTarget, useIrBackend())

        for (library in preferences[DebuggerPreferenceKeys.ATTACH_LIBRARY]) {
            if (library.startsWith("maven("))
                addMavenDependency(compilerFacility, library)
            else
                compilerFacility.compileExternalLibrary(library, librarySrcDirectory, libraryOutputDirectory)
        }

        compilerFacility.compileLibrary(librarySrcDirectory, libraryOutputDirectory)
        mainClassName = compilerFacility.compileTestSources(myModule, sourcesOutputDirectory, File(appOutputPath), libraryOutputDirectory)

        breakpointCreator = BreakpointCreator(
            project,
            ::systemLogger,
            preferences
        ).apply { createAdditionalBreakpoints(wholeFileContents) }

        createLocalProcess(mainClassName)
        doMultiFileTest(testFiles, preferences)
    }

    open fun addMavenDependency(compilerFacility: DebuggerTestCompilerFacility, library: String) {
    }

    private fun createTestFiles(wholeFile: File, wholeFileContents: String): TestFiles {
        val testFiles = org.jetbrains.kotlin.test.TestFiles.createTestFiles(
            wholeFile.name,
            wholeFileContents,
            object : org.jetbrains.kotlin.test.TestFiles.TestFileFactoryNoModules<TestFile>() {
                override fun create(fileName: String, text: String, directives: Directives): TestFile {
                    return TestFile(fileName, text, directives)
                }
            }
        )

        val wholeTestFile = TestFile(wholeFile.name, wholeFileContents)
        return TestFiles(wholeFile, wholeTestFile, testFiles)
    }

    abstract fun doMultiFileTest(files: TestFiles, preferences: DebuggerPreferences)

    override fun initOutputChecker(): OutputChecker {
        return KotlinOutputChecker(
            getTestDirectoryPath(),
            testAppPath,
            appOutputPath,
            targetBackend(),
            getExpectedOutputFile()
        )
    }

    override fun setUpModule() {
        super.setUpModule()
        attachLibraries()
    }

    override fun setUpProject() {
        LocalFileSystem.getInstance().refreshAndFindFileByIoFile(File(appDataPath))
        super.setUpProject()
        File(appOutputPath).mkdirs()
    }

    override fun createBreakpoints(file: PsiFile?) {
        if (file != null) {
            val breakpointCreator = this.breakpointCreator ?: error(BreakpointCreator::class.java.simpleName + " should be set")
            breakpointCreator.createBreakpoints(file)
        }
    }

    override fun createJavaParameters(mainClass: String?): JavaParameters {
        return super.createJavaParameters(mainClass).apply {
            ModuleRootManager.getInstance(myModule).orderEntries.asSequence().filterIsInstance<LibraryOrderEntry>()
            classPath.add(ForTestCompileRuntime.runtimeJarForTests())
            classPath.add(libraryOutputDirectory)
        }
    }

    private fun attachLibraries() {
        runWriteAction {
            val kotlinStdlibJar = ForTestCompileRuntime.runtimeJarForTests()
            val kotlinStdlibSourcesJar = ForTestCompileRuntime.runtimeSourcesJarForTests()

            val model = ModuleRootManager.getInstance(myModule).modifiableModel
            attachLibrary(model, KOTLIN_LIBRARY_NAME, kotlinStdlibJar, kotlinStdlibSourcesJar)
            attachLibrary(model, TEST_LIBRARY_NAME, libraryOutputDirectory, librarySrcDirectory)
            model.commit()
        }
    }

    private fun detachLibraries() {
        EdtTestUtil.runInEdtAndGet(ThrowableComputable {
            ConfigLibraryUtil.removeLibrary(module, KOTLIN_LIBRARY_NAME)
            ConfigLibraryUtil.removeLibrary(module, TEST_LIBRARY_NAME)
        })
    }

    private fun attachLibrary(model: ModifiableRootModel, libraryName: String, classes: File, sources: File) {
        val customLibEditor = NewLibraryEditor().apply {
            name = libraryName

            addRoot(VfsUtil.getUrlForLibraryRoot(classes), OrderRootType.CLASSES)
            addRoot(VfsUtil.getUrlForLibraryRoot(sources), OrderRootType.SOURCES)
        }

        ConfigLibraryUtil.addLibrary(customLibEditor, model, null)
    }

    override fun checkTestOutput() {
        if (KotlinTestUtils.isAllFilesPresentTest(getTestName(false))) {
            return
        }

        try {
            super.checkTestOutput()
        } catch (e: ComparisonFailure) {
            KotlinTestUtils.assertEqualsToFile(getExpectedOutputFile(), e.actual)
        }
    }

    open fun fragmentCompilerBackend() = CodeFragmentCompiler.Companion.FragmentCompilerBackend.JVM

    protected fun targetBackend(): TargetBackend =
        when (fragmentCompilerBackend()) {
            CodeFragmentCompiler.Companion.FragmentCompilerBackend.JVM ->
                if (useIrBackend()) TargetBackend.JVM_IR_WITH_OLD_EVALUATOR else TargetBackend.JVM_WITH_OLD_EVALUATOR
            CodeFragmentCompiler.Companion.FragmentCompilerBackend.JVM_IR ->
                if (useIrBackend()) TargetBackend.JVM_IR_WITH_IR_EVALUATOR else TargetBackend.JVM_WITH_IR_EVALUATOR
        }

    protected fun getExpectedOutputFile(): File {
        if (useIrBackend()) {
            val irOut = File(getTestDirectoryPath(), getTestName(true) + ".ir.out")
            if (irOut.exists()) return irOut
        }
        return File(getTestDirectoryPath(), getTestName(true) + ".out")
    }

    override fun getData(dataId: String): Any? {
        if (XDebugSession.DATA_KEY.`is`(dataId)) {
            return myDebuggerSession?.xDebugSession
        }

        return super.getData(dataId)
    }

    override fun runTest() {
        runTest { super.runTest() }
    }

    protected fun getTestDirectoryPath(): String = javaClass.getAnnotation(TestMetadata::class.java).value
}