package com.staboss.stego.data

import java.io.File

const val srcIMG = "src/test/resources/src.bmp"
const val resIMG = "src/test/resources/res.bmp"
const val keyBin = "src/test/resources/key.txt"

const val file256 = "src/test/resources/text256.txt"
const val file4096 = "src/test/resources/text4096.txt"
const val file65536 = "src/test/resources/text65536.txt"

val text256 = File(file256).readText()
val text4096 = File(file4096).readText()
val text65536 = File(file65536).readText()
