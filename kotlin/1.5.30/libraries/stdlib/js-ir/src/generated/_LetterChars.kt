/*
 * Copyright 2010-2021 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package kotlin.text

//
// NOTE: THIS FILE IS AUTO-GENERATED by the GenerateUnicodeData.kt
// See: https://github.com/JetBrains/kotlin/tree/master/libraries/stdlib
//

// 222 ranges totally
private object Letter {
    val decodedRangeStart: IntArray
    val decodedRangeLength: IntArray
    val decodedRangeCategory: IntArray
    
    init {
        val toBase64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
        val fromBase64 = IntArray(128)
        for (i in toBase64.indices) {
            fromBase64[toBase64[i].code] = i
        }
        
        // rangeStartDiff.length = 356
        val rangeStartDiff = "hCgBpCQGYHZH5BRpBPPPPPPRMP5BPPlCPP6BkEPPPPcPXPzBvBrB3BOiDoBHwD+E3DauCnFmBmB2D6E1BlBTiBmBlBP5BhBiBrBvBjBqBnBPRtBiCmCtBlB0BmB5BiB7BmBgEmChBZgCoEoGVpBSfRhBPqKQ2BwBYoFgB4CJuTiEvBuCuDrF5DgEgFlJ1DgFmBQtBsBRGsB+BPiBlD1EIjDPRPPPQPPPPPGQSQS/DxENVNU+B9zCwBwBPPCkDPNnBPqDYY1R8B7FkFgTgwGgwUwmBgKwBuBScmEP/BPPPPPPrBP8B7F1B/ErBqC6B7BiBmBfQsBUwCw/KwqIwLwETPcPjQgJxFgBlBsD"
        val diff = decodeVarLenBase64(rangeStartDiff, fromBase64, 222)
        val start = IntArray(diff.size)
        for (i in diff.indices) {
            if (i == 0) start[i] = diff[i]
            else start[i] = start[i - 1] + diff[i]
        }
        decodedRangeStart = start
        
        // rangeLength.length = 328
        val rangeLength = "aaMBXHYH5BRpBPPPPPPRMP5BPPlCPPzBDOOPPcPXPzBvBjB3BOhDmBBpB7DoDYxB+EiBP1DoExBkBQhBekBPmBgBhBctBiBMWOOXhCsBpBkBUV3Ba4BkB0DlCgBXgBtD4FSdBfPhBPpKP0BvBXjEQ2CGsT8DhBtCqDpFvD1D3E0IrD2EkBJrBDOBsB+BPiBlB1EIjDPPPPPPPPPPPGPPMNLsBNPNPKCvBvBPPCkDPBmBPhDXXgD4B6FzEgDguG9vUtkB9JcuBSckEP/BPPPPPPBPf4FrBjEhBpC3B5BKaWPrBOwCk/KsCuLqDHPbPxPsFtEaaqDL"
        decodedRangeLength = decodeVarLenBase64(rangeLength, fromBase64, 222)
        
        // rangeCategory.length = 959
        val rangeCategory = "GFjgggUHGGFFZZZmzpz5qB6s6020B60ptltB6smt2sB60mz22B1+vv+8BZZ5s2850BW5q1ymtB506smzBF3q1q1qB1q1q1+Bgii4wDTm74g3KiggxqM60q1q1Bq1o1q1BF1qlrqrBZ2q5wprBGFZWWZGHFsjiooLowgmOowjkwCkgoiIk7ligGogiioBkwkiYkzj2oNoi+sbkwj04DghhkQ8wgiYkgoioDsgnkwC4gikQ//v+85BkwvoIsgoyI4yguI0whiwEowri4CoghsJowgqYowgm4DkwgsY/nwnzPowhmYkg6wI8yggZswikwHgxgmIoxgqYkwgk4DkxgmIkgoioBsgssoBgzgyI8g9gL8g9kI0wgwJoxgkoC0wgioFkw/wI0w53iF4gioYowjmgBHGq1qkgwBF1q1q8qBHwghuIwghyKk0goQkwgoQk3goQHGFHkyg0pBgxj6IoinkxDswno7Ikwhz9Bo0gioB8z48Rwli0xN0mpjoX8w78pDwltoqKHFGGwwgsIHFH3q1q16BFHWFZ1q10q1B2qlwq1B1q10q1B2q1yq1B6q1gq1Biq1qhxBir1qp1Bqt1q1qB1g1q1+B//3q16B///q1qBH/qlqq9Bholqq9B1i00a1q10qD1op1HkwmigEigiy6Cptogq1Bixo1kDq7/j00B2qgoBWGFm1lz50B6s5q1+BGWhggzhwBFFhgk4//Bo2jigE8wguI8wguI8wgugUog1qoB4qjmIwwi2KgkYHHH4lBgiFWkgIWoghssMmz5smrBZ3q1y50B5sm7gzBtz1smzB5smz50BqzqtmzB5sgzqzBF2/9//5BowgoIwmnkzPkwgk4C8ys65BkgoqI0wgy6FghquZo2giY0ghiIsgh24B4ghsQ8QF/v1q1OFs0O8iCHHF1qggz/B8wg6Iznv+//B08QgohsjK0QGFk7hsQ4gB"
        decodedRangeCategory = decodeVarLenBase64(rangeCategory, fromBase64, 222)
    }
}

/**
 * Returns `true` if this character is a letter.
 */
internal fun Char.isLetterImpl(): Boolean {
    return getLetterType() != 0
}

/**
 * Returns `true` if this character is a lower case letter, or it has contributory property Other_Lowercase.
 */
internal fun Char.isLowerCaseImpl(): Boolean {
    return getLetterType() == 1 || code.isOtherLowercase()
}

/**
 * Returns `true` if this character is an upper case letter, or it has contributory property Other_Uppercase.
 */
internal fun Char.isUpperCaseImpl(): Boolean {
    return getLetterType() == 2 || code.isOtherUppercase()
}

/**
 * Returns
 *   - `1` if the character is a lower case letter,
 *   - `2` if the character is an upper case letter,
 *   - `3` if the character is a letter but not a lower or upper case letter,
 *   - `0` otherwise.
 */
private fun Char.getLetterType(): Int {
    val ch = this.code
    val index = binarySearchRange(Letter.decodedRangeStart, ch)

    val rangeStart = Letter.decodedRangeStart[index]
    val rangeEnd = rangeStart + Letter.decodedRangeLength[index] - 1
    val code = Letter.decodedRangeCategory[index]

    if (ch > rangeEnd) {
        return 0
    }

    val lastTwoBits = code and 0x3

    if (lastTwoBits == 0) { // gap pattern
        var shift = 2
        var threshold = rangeStart
        for (i in 0..1) {
            threshold += (code shr shift) and 0x7f
            if (threshold > ch) {
                return 3
            }
            shift += 7
            threshold += (code shr shift) and 0x7f
            if (threshold > ch) {
                return 0
            }
            shift += 7
        }
        return 3
    }

    if (code <= 0x7) {
        return lastTwoBits
    }

    val distance = (ch - rangeStart)
    val shift = if (code <= 0x1F) distance % 2 else distance
    return (code shr (2 * shift)) and 0x3
}
