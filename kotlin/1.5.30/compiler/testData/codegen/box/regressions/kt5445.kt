// TARGET_BACKEND: JVM

// IGNORE_BACKEND_FIR: JVM_IR
//  - FIR2IR should generate call to fake override

// WITH_RUNTIME
// FILE: 1.kt

package test2

import test.A

public fun box(): String {
    return B().test(B())
}

public class B : A() {
    public fun test(other:Any): String {
        if (other is B && other.s == 2) {
            return "OK"
        }
        return "fail"
    }
}

// FILE: 2.kt

package test

open class A {
    @JvmField protected val s = 2;
}
