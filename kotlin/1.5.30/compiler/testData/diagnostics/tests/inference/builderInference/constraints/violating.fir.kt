// !LANGUAGE: +UnrestrictedBuilderInference
// WITH_RUNTIME

interface A {
    fun foo(): MutableList<String>
}

@ExperimentalStdlibApi
fun main() {
    buildList {
        add(3)
        object : A {
            override fun foo(): MutableList<String> = this@buildList
        }
    }
    buildList {
        add(3)
        val x: String = <!INITIALIZER_TYPE_MISMATCH!>get(0)<!>
    }
    buildList {
        add("3")
        val x: MutableList<Int> = this@buildList
    }
    buildList {
        val y: CharSequence = ""
        add(y)
        val x: MutableList<String> = this@buildList
    }
    buildList {
        add("")
        val x: StringBuilder = <!INITIALIZER_TYPE_MISMATCH!>get(0)<!>
    }
}
