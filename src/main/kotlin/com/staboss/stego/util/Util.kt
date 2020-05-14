package com.staboss.stego.util

import kotlin.system.exitProcess

val math = Math

/**
 * Supplements the binary sequence with zeros
 *
 * @param bits coding (bits per symbol)
 * @return binary representation multiple of [bits]
 */
fun String.toCorrectBinaryLength(bits: Int = 8) = "0".repeat(bits - length % bits) + this

/**
 * Converts a number to its binary format
 *
 * @param bits coding (bits per symbol)
 * @return binary representation of a number
 */
fun Int.toBinary(bits: Int = 8): String = toString(radix = 2).toCorrectBinaryLength(bits)

/**
 * Converts a character to its binary format
 *
 * @param bits coding (bits per symbol)
 * @return binary representation of a character
 */
fun Char.toBinary(bits: Int = 8): String = toInt().toString(radix = 2).toCorrectBinaryLength(bits)

/**
 * Converts a string to binary representation
 *
 * @param bits coding (bits per symbol)
 * @return binary string
 */
fun String.toBinary(bits: Int = 8): String =
    map { char -> char.toBinary(bits) }
        .flatMap { binaryString -> binaryString.toList() }
        .map { bit -> bit.toString().toInt() }
        .joinToString("")

/**
 * Converts a string of bits to text
 *
 * @param bits coding (bits per symbol)
 * @return text
 */
fun String.toText(bits: Int = 8): String =
    chunked(bits)
        .map { binaryString -> binaryString.toInt(radix = 2).toChar() }
        .joinToString("")

/**
 * Error output
 *
 * @param message error message
 */
fun errorMessage(message: String): Nothing {
    System.err.println(message)
    exitProcess(1)
}

/**
 * Measures [action] execution time
 *
 * @param action action
 * @return time (ms)
 */
inline fun measureTime(action: () -> Unit): Long {
    val startTime = System.currentTimeMillis()
    action.invoke()
    val endTime = System.currentTimeMillis()
    return endTime - startTime
}

/**
 * Double FOR loop
 *
 * @param firstRange number of iterations in the main loop
 * @param secondRange number of iterations in the nested loop
 * @param action action that takes double loop indices
 */
inline fun doubleLoop(firstRange: Int, secondRange: Int, action: (Int, Int) -> Unit) {
    for (i in 0 until firstRange) {
        for (j in 0 until secondRange) {
            action(i, j)
        }
    }
}
