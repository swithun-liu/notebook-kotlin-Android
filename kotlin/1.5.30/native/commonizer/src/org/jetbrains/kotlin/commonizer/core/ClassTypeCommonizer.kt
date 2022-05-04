/*
 * Copyright 2010-2021 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.commonizer.core

import org.jetbrains.kotlin.commonizer.cir.CirClassType
import org.jetbrains.kotlin.commonizer.cir.CirEntityId
import org.jetbrains.kotlin.commonizer.mergedtree.CirKnownClassifiers
import org.jetbrains.kotlin.commonizer.utils.isUnderKotlinNativeSyntheticPackages
import org.jetbrains.kotlin.descriptors.Visibility

internal class ClassTypeCommonizer(private val classifiers: CirKnownClassifiers) :
    AbstractStandardCommonizer<CirClassType, CirClassType>() {
    private lateinit var classId: CirEntityId
    private val outerType = OuterClassTypeCommonizer(classifiers)
    private lateinit var anyVisibility: Visibility
    private val arguments = TypeArgumentListCommonizer(classifiers)
    private var isMarkedNullable = false

    override fun commonizationResult() = CirClassType.createInterned(
        classId = classId,
        outerType = outerType.result,
        // N.B. The 'visibility' field in class types is needed ONLY for TA commonization. The class type constructed here is
        // intended to be used in "common" target. It could not participate in TA commonization. So, it does not matter which
        // exactly visibility will be recorded for commonized class type. Passing the visibility of the first class type
        // to reach better interning rate.
        visibility = anyVisibility,
        arguments = arguments.result,
        isMarkedNullable = isMarkedNullable
    )

    override fun initialize(first: CirClassType) {
        classId = first.classifierId
        anyVisibility = first.visibility
        isMarkedNullable = first.isMarkedNullable
    }

    override fun doCommonizeWith(next: CirClassType) =
        isMarkedNullable == next.isMarkedNullable
                && classId == next.classifierId
                && outerType.commonizeWith(next.outerType)
                && isClassifierAvailableInCommon(classifiers, classId)
                && arguments.commonizeWith(next.arguments)
}

private fun isClassifierAvailableInCommon(classifiers: CirKnownClassifiers, classId: CirEntityId): Boolean {
    if (classifiers.commonDependencies.hasClassifier(classId)) {
        // The class is from common fragment of dependency library (ex: stdlib). Already commonized.
        return true
    } else if (classId.packageName.isUnderKotlinNativeSyntheticPackages) {
        // C/Obj-C forward declarations are:
        // - Either resolved to real classes/interfaces from other interop libraries (which are generated by C-interop tool and
        //   are known to have modality/visibility/other attributes to successfully pass commonization).
        // - Or resolved to the same synthetic classes/interfaces.
        // ... and therefore are considered as successfully commonized.
        return true
    }

    // Looking for a a node that provides a non-null (successfully commonized) classifier declaration
    return (classifiers.commonizedNodes.classNode(classId)?.commonDeclaration?.invoke()
        ?: classifiers.commonizedNodes.typeAliasNode(classId)?.commonDeclaration?.invoke()) != null
}

private class OuterClassTypeCommonizer(classifiers: CirKnownClassifiers) :
    AbstractNullableCommonizer<CirClassType, CirClassType, CirClassType, CirClassType>(
        wrappedCommonizerFactory = { ClassTypeCommonizer(classifiers) },
        extractor = { it },
        builder = { it }
    )