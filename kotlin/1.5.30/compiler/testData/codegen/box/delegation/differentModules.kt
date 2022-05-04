// TARGET_BACKEND: JVM
// WITH_RUNTIME

// MODULE: base
// FILE: base.kt

interface PsiClass {
    fun foo(): String?
}

interface UClass : PsiClass {
    override fun foo(): String?
}

abstract class BaseKotlinUClass(
    psi: PsiClass,
) : UClass, PsiClass by psi

// MODULE: main(base)
// FILE: main.kt

class A(psi: PsiClass) : BaseKotlinUClass(psi)

fun bar(uClass: UClass): String = uClass.foo()!!

fun box(): String = bar(A(object : PsiClass {
    override fun foo(): String? = "OK"
}))