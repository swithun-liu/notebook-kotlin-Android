/*
 * Copyright 2010-2021 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.jps.build;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.JUnit3RunnerWithInners;
import org.jetbrains.kotlin.test.KotlinTestUtils;
import org.jetbrains.kotlin.test.util.KtTestUtil;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.generators.tests.TestsPackage}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("jps-plugin/testData/incremental/lookupTracker/jvm")
@TestDataPath("$PROJECT_ROOT")
@RunWith(JUnit3RunnerWithInners.class)
public class JvmLookupTrackerTestGenerated extends AbstractJvmLookupTrackerTest {
    private void runTest(String testDataFilePath) throws Exception {
        KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
    }

    public void testAllFilesPresentInJvm() throws Exception {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("jps-plugin/testData/incremental/lookupTracker/jvm"), Pattern.compile("^([^\\.]+)$"), null, false);
    }

    @TestMetadata("classifierMembers")
    public void testClassifierMembers() throws Exception {
        runTest("jps-plugin/testData/incremental/lookupTracker/jvm/classifierMembers/");
    }

    @TestMetadata("conventions")
    public void testConventions() throws Exception {
        runTest("jps-plugin/testData/incremental/lookupTracker/jvm/conventions/");
    }

    @TestMetadata("expressionType")
    public void testExpressionType() throws Exception {
        runTest("jps-plugin/testData/incremental/lookupTracker/jvm/expressionType/");
    }

    @TestMetadata("java")
    public void testJava() throws Exception {
        runTest("jps-plugin/testData/incremental/lookupTracker/jvm/java/");
    }

    @TestMetadata("localDeclarations")
    public void testLocalDeclarations() throws Exception {
        runTest("jps-plugin/testData/incremental/lookupTracker/jvm/localDeclarations/");
    }

    @TestMetadata("packageDeclarations")
    public void testPackageDeclarations() throws Exception {
        runTest("jps-plugin/testData/incremental/lookupTracker/jvm/packageDeclarations/");
    }

    @TestMetadata("SAM")
    public void testSAM() throws Exception {
        runTest("jps-plugin/testData/incremental/lookupTracker/jvm/SAM/");
    }

    @TestMetadata("simple")
    public void testSimple() throws Exception {
        runTest("jps-plugin/testData/incremental/lookupTracker/jvm/simple/");
    }

    @TestMetadata("syntheticProperties")
    public void testSyntheticProperties() throws Exception {
        runTest("jps-plugin/testData/incremental/lookupTracker/jvm/syntheticProperties/");
    }
}