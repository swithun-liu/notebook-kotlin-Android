/*
 * Copyright 2010-2021 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.idea.fir.frontend.api.test.framework

import com.intellij.mock.MockProject
import org.jetbrains.kotlin.idea.fir.low.level.api.test.base.AbstractLowLevelApiTest
import org.jetbrains.kotlin.idea.frontend.api.InvalidWayOfUsingAnalysisSession
import org.jetbrains.kotlin.idea.frontend.api.KtAnalysisSessionProvider
import org.jetbrains.kotlin.idea.frontend.api.fir.KtFirAnalysisSessionProvider

abstract class AbstractHLApiTest : AbstractLowLevelApiTest() {
    @OptIn(InvalidWayOfUsingAnalysisSession::class)
    override fun registerServicesForProject(project: MockProject) {
        super.registerServicesForProject(project)
        project.registerService(KtAnalysisSessionProvider::class.java, KtFirAnalysisSessionProvider::class.java)
    }
}