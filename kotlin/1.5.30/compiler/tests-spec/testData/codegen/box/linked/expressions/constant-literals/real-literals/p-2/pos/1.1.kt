/*
 * KOTLIN CODEGEN BOX SPEC TEST (POSITIVE)
 *
 * SPEC VERSION: 0.1-435
 * MAIN LINK: expressions, constant-literals, real-literals -> paragraph 2 -> sentence 1
 * NUMBER: 1
 * DESCRIPTION: Simple real literals with an exponent mark.
 */

val value_1 = 0.0e0
val value_2 = 0.0e-00
val value_3 = 0.0E000
val value_4 = 0.0E+0000

val value_5 = 00.0e+0
val value_6 = 000.00e00
val value_7 = 0000.000E-000

val value_8 = 1.0E+1
val value_9 = 22.00e22
val value_10 = 2345678.345678e00000000001
val value_11 = 456.56e-0
val value_12 = 5.65e000000000000
val value_13 = 654.7654E+010
val value_14 = 76543.876543E1
val value_15 = 8765432.98765432e-2
val value_16 = 987654321.0987654321E-3

val value_17 = 0.1111e4
val value_18 = 1.22222E-5
val value_19 = 9.33333e+6
val value_20 = 9.444444E7
val value_21 = 8.5555555e8
val value_22 = 3.777777777E-308
val value_23 = 6.99999999999e-309

val value_24 = 0.000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000e0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
val value_25 = 0.000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000e-000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
val value_26 = 0.000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000e+000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000

fun box(): String? {
    if (value_1.compareTo(0.0e0) != 0 || value_1.compareTo(0.0) != 0) return null
    if (value_2.compareTo(0.0e-00) != 0 || value_2.compareTo(0.0) != 0) return null
    if (value_3.compareTo(0.0E000) != 0 || value_3.compareTo(0.0) != 0) return null
    if (value_4.compareTo(0.0E+0000) != 0 || value_4.compareTo(0.0) != 0) return null
    if (value_5.compareTo(00.0e+0) != 0 || value_5.compareTo(0.0) != 0) return null
    if (value_6.compareTo(000.00e00) != 0 || value_6.compareTo(0.0) != 0) return null
    if (value_7.compareTo(0000.000E-000) != 0 || value_7.compareTo(0.0) != 0) return null

    if (value_8.compareTo(1.0E+1) != 0 || value_8.compareTo(10.0) != 0) return null
    if (value_9.compareTo(22.00e22) != 0 || value_9.compareTo(2.2E23) != 0) return null
    if (value_10.compareTo(2345678.345678e00000000001) != 0 || value_10.compareTo(2.345678345678E7) != 0) return null
    if (value_11.compareTo(456.56e-0) != 0 || value_11.compareTo(456.56) != 0) return null
    if (value_12.compareTo(5.65e000000000000) != 0 || value_12.compareTo(5.65) != 0) return null
    if (value_13.compareTo(654.7654E+010) != 0 || value_13.compareTo(6.547654E12) != 0) return null
    if (value_14.compareTo(76543.876543E1) != 0 || value_14.compareTo(765438.76543) != 0) return null
    if (value_15.compareTo(8765432.98765432e-2) != 0 || value_15.compareTo(87654.3298765432) != 0) return null
    if (value_16.compareTo(987654321.0987654321E-3) != 0 || value_16.compareTo(987654.3210987655) != 0) return null

    if (value_17.compareTo(0.1111e4) != 0 || value_17.compareTo(1111.0) != 0) return null
    if (value_18.compareTo(1.22222E-5) != 0) return null
    if (value_19.compareTo(9.33333e+6) != 0 || value_19.compareTo(9333330.0) != 0) return null
    if (value_20.compareTo(9.444444E7) != 0) return null
    if (value_21.compareTo(8.5555555e8) != 0 || value_21.compareTo(8.5555555E8) != 0) return null
    if (value_22.compareTo(3.777777777E-308) != 0) return null
    if (value_23.compareTo(6.99999999999e-309) != 0 || value_23.compareTo(6.99999999999E-309) != 0) return null
    if (value_24.compareTo(0.000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000e0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000) != 0 || value_24.compareTo(0.0) != 0) return null
    if (value_25.compareTo(0.000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000e-000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000) != 0 || value_25.compareTo(0.0) != 0) return null
    if (value_26.compareTo(0.000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000e+000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000) != 0 || value_26.compareTo(0.0) != 0) return null

    return "OK"
}