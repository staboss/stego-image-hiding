package com.staboss.stego.util

import kotlin.system.exitProcess

val math = Math

/**
 * Дополняет двоичную последовательность нулями
 *
 * @param bits кодировка
 * @return двоичное представление кратное [bits]
 */
fun String.toCorrectBinaryLength(bits: Int = 8) = "0".repeat(bits - length % bits) + this

/**
 * Преобразует число в его двоичный формат
 *
 * @param bits кодировка
 * @return двоичное представление числа
 */
fun Int.toBinary(bits: Int = 8): String = toString(radix = 2).toCorrectBinaryLength(bits)

/**
 * Преобразует символ в его двоичный формат
 *
 * @param bits кодировка
 * @return двоичное представление символа
 */
fun Char.toBinary(bits: Int = 8): String = toInt().toString(radix = 2).toCorrectBinaryLength(bits)

/**
 * Преобразует строку в двоичное представление
 *
 * @param bits кодировка
 * @return массив битов
 */
fun String.toBinary(bits: Int = 8): String =
    map { char -> char.toBinary(bits) }
        .flatMap { binaryString -> binaryString.toList() }
        .map { bit -> bit.toString().toInt() }
        .joinToString("")

/**
 * Преобразует строку битов в текст
 *
 * @param bits кодировка
 * @return текст
 */
fun String.toText(bits: Int = 8): String =
    chunked(bits)
        .map { binaryString -> binaryString.toInt(radix = 2).toChar() }
        .joinToString("")

/**
 * Вывод ошибки
 *
 * @param message сообщение об ошибке
 */
fun errorMessage(message: String): Nothing {
    System.err.println(message)
    exitProcess(1)
}

/**
 * Время выполнения дейстия [action]
 *
 * @param action действие
 * @return время в мс
 */
inline fun measureTime(action: () -> Unit): Long {
    val startTime = System.currentTimeMillis()
    action.invoke()
    val endTime = System.currentTimeMillis()
    return endTime - startTime
}

/**
 * Двойной цикл for
 *
 * @param firstRange количество итераций в главном цикле
 * @param secondRange количество итераций во вложенном цикле
 * @param action действие, которое принимает индексы двойного цикла
 */
inline fun doubleLoop(firstRange: Int, secondRange: Int, action: (Int, Int) -> Unit) {
    for (i in 0 until firstRange) {
        for (j in 0 until secondRange) {
            action(i, j)
        }
    }
}
