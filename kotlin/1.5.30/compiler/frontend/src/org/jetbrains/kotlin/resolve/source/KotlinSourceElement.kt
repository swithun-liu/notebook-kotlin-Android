/*
 * Copyright 2010-2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.resolve.source

import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.descriptors.SourceElement
import org.jetbrains.kotlin.psi.KtElement
import org.jetbrains.kotlin.psi.KtPureElement

class KotlinSourceElement(override val psi: KtElement) : PsiSourceElement

fun KtPureElement?.toSourceElement(): SourceElement = if (this == null) SourceElement.NO_SOURCE else KotlinSourceElement(psiOrParent)

@Deprecated(
    "provided for BWC",
    replaceWith = ReplaceWith("PsiSourceElementKt.getPsi"),
    level = DeprecationLevel.ERROR
)
@JvmName(name = "getPsi")
fun SourceElement._getPsi(): PsiElement? = (this as? PsiSourceElement)?.psi