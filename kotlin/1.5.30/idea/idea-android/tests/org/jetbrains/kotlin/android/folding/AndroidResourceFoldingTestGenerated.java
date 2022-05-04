/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.android.folding;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.JUnit3RunnerWithInners;
import org.jetbrains.kotlin.test.KotlinTestUtils;
import org.jetbrains.kotlin.test.TargetBackend;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.generators.tests.TestsPackage}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("idea/testData/android/folding")
@TestDataPath("$PROJECT_ROOT")
@RunWith(JUnit3RunnerWithInners.class)
public class AndroidResourceFoldingTestGenerated extends AbstractAndroidResourceFoldingTest {
    private void runTest(String testDataFilePath) throws Exception {
        KotlinTestUtils.runTest(this::doTest, TargetBackend.ANY, testDataFilePath);
    }

    public void testAllFilesPresentInFolding() throws Exception {
        org.jetbrains.kotlin.test.util.KtTestUtil.assertAllTestsPresentByMetadata(this.getClass(), new File("idea/testData/android/folding"), Pattern.compile("^(.+)\\.kt$"), TargetBackend.ANY, true);
    }

    @TestMetadata("dimensions.kt")
    public void testDimensions() throws Exception {
        runTest("idea/testData/android/folding/dimensions.kt");
    }

    @TestMetadata("getString.kt")
    public void testGetString() throws Exception {
        runTest("idea/testData/android/folding/getString.kt");
    }

    @TestMetadata("plurals.kt")
    public void testPlurals() throws Exception {
        runTest("idea/testData/android/folding/plurals.kt");
    }
}