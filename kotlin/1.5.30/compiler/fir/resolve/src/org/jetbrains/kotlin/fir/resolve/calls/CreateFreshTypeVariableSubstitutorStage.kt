/*
 * Copyright 2010-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.fir.resolve.calls

import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.declarations.FirTypeParameterRef
import org.jetbrains.kotlin.fir.declarations.FirTypeParameterRefsOwner
import org.jetbrains.kotlin.fir.renderWithType
import org.jetbrains.kotlin.fir.resolve.fullyExpandedType
import org.jetbrains.kotlin.fir.resolve.inference.ConeTypeParameterBasedTypeVariable
import org.jetbrains.kotlin.fir.resolve.inference.inferenceComponents
import org.jetbrains.kotlin.fir.resolve.inference.model.ConeDeclaredUpperBoundConstraintPosition
import org.jetbrains.kotlin.fir.resolve.inference.model.ConeExplicitTypeParameterConstraintPosition
import org.jetbrains.kotlin.fir.resolve.substitution.ConeSubstitutor
import org.jetbrains.kotlin.fir.resolve.substitution.substitutorByMap
import org.jetbrains.kotlin.fir.symbols.ConeTypeParameterLookupTag
import org.jetbrains.kotlin.fir.typeContext
import org.jetbrains.kotlin.fir.types.*
import org.jetbrains.kotlin.fir.types.impl.FirTypePlaceholderProjection
import org.jetbrains.kotlin.name.StandardClassIds
import org.jetbrains.kotlin.resolve.calls.inference.ConstraintSystemOperation
import org.jetbrains.kotlin.resolve.calls.inference.model.SimpleConstraintSystemConstraintPosition

internal object CreateFreshTypeVariableSubstitutorStage : ResolutionStage() {
    override suspend fun check(candidate: Candidate, callInfo: CallInfo, sink: CheckerSink, context: ResolutionContext) {
        val declaration = candidate.symbol.fir
        if (declaration !is FirTypeParameterRefsOwner || declaration.typeParameters.isEmpty()) {
            candidate.substitutor = ConeSubstitutor.Empty
            candidate.freshVariables = emptyList()
            return
        }
        val csBuilder = candidate.system.getBuilder()
        val (substitutor, freshVariables) =
            createToFreshVariableSubstitutorAndAddInitialConstraints(declaration, csBuilder, context.session)
        candidate.substitutor = substitutor
        candidate.freshVariables = freshVariables

        // bad function -- error on declaration side
        if (csBuilder.hasContradiction) {
            sink.yieldDiagnostic(InapplicableCandidate) //TODO: auto report it
            return
        }

        // optimization
        if (candidate.typeArgumentMapping == TypeArgumentMapping.NoExplicitArguments /*&& knownTypeParametersResultingSubstitutor == null*/) {
            return
        }

        val typeParameters = declaration.typeParameters
        for (index in typeParameters.indices) {
            val typeParameter = typeParameters[index]
            val freshVariable = freshVariables[index]

//            val knownTypeArgument = knownTypeParametersResultingSubstitutor?.substitute(typeParameter.defaultType)
//            if (knownTypeArgument != null) {
//                csBuilder.addEqualityConstraint(
//                    freshVariable.defaultType,
//                    knownTypeArgument.unwrap(),
//                    KnownTypeParameterConstraintPosition(knownTypeArgument)
//                )
//                continue
//            }

            when (val typeArgument = candidate.typeArgumentMapping[index]) {
                is FirTypeProjectionWithVariance -> csBuilder.addEqualityConstraint(
                    freshVariable.defaultType,
                    getTypePreservingFlexibilityWrtTypeVariable(
                        typeArgument.typeRef.coneType,
                        typeParameter,
                        context.session
                    ).fullyExpandedType(context.session),
                    ConeExplicitTypeParameterConstraintPosition(typeArgument)
                )
                is FirStarProjection -> csBuilder.addEqualityConstraint(
                    freshVariable.defaultType,
                    typeParameter.symbol.fir.bounds.firstOrNull()?.coneType
                        ?: context.session.builtinTypes.nullableAnyType.type,
                    SimpleConstraintSystemConstraintPosition
                )
                else -> assert(typeArgument == FirTypePlaceholderProjection) {
                    "Unexpected typeArgument: ${typeArgument.renderWithType()}"
                }
            }
        }
    }

    private fun getTypePreservingFlexibilityWrtTypeVariable(
        type: ConeKotlinType,
        typeParameter: FirTypeParameterRef,
        session: FirSession,
    ): ConeKotlinType {
        return if (typeParameter.shouldBeFlexible(session.typeContext)) {
            val notNullType = type.withNullability(ConeNullability.NOT_NULL, session.typeContext)
            ConeFlexibleType(notNullType, notNullType.withNullability(ConeNullability.NULLABLE, session.typeContext))
        } else {
            type
        }
    }

    private fun FirTypeParameterRef.shouldBeFlexible(context: ConeTypeContext): Boolean {
        return symbol.fir.bounds.any {
            val type = it.coneType
            type is ConeFlexibleType || with(context) {
                (type.typeConstructor() as? ConeTypeParameterLookupTag)?.symbol?.fir?.shouldBeFlexible(context) ?: false
            }
        }
    }

}

private fun createToFreshVariableSubstitutorAndAddInitialConstraints(
    declaration: FirTypeParameterRefsOwner,
    csBuilder: ConstraintSystemOperation,
    session: FirSession
): Pair<ConeSubstitutor, List<ConeTypeVariable>> {

    val typeParameters = declaration.typeParameters

    val freshTypeVariables = typeParameters.map { ConeTypeParameterBasedTypeVariable(it.symbol) }

    val toFreshVariables = substitutorByMap(freshTypeVariables.associate { it.typeParameterSymbol to it.defaultType }, session)

    for (freshVariable in freshTypeVariables) {
        csBuilder.registerVariable(freshVariable)
    }

    fun ConeTypeParameterBasedTypeVariable.addSubtypeConstraint(
        upperBound: ConeKotlinType//,
        //position: DeclaredUpperBoundConstraintPosition
    ) {
        if ((upperBound.lowerBoundIfFlexible() as? ConeClassLikeType)?.lookupTag?.classId == StandardClassIds.Any &&
            upperBound.upperBoundIfFlexible().isMarkedNullable
        ) {
            return
        }

        csBuilder.addSubtypeConstraint(
            defaultType,
            toFreshVariables.substituteOrSelf(upperBound),
            ConeDeclaredUpperBoundConstraintPosition()
        )
    }

    for (index in typeParameters.indices) {
        val typeParameter = typeParameters[index]
        val freshVariable = freshTypeVariables[index]
        //val position = DeclaredUpperBoundConstraintPosition(typeParameter)

        for (upperBound in typeParameter.symbol.fir.bounds) {
            freshVariable.addSubtypeConstraint(upperBound.coneType/*, position*/)
        }
    }

    return toFreshVariables to freshTypeVariables
}