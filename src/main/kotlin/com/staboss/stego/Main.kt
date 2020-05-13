package com.staboss.stego

import com.staboss.stego.method.DCT
import com.staboss.stego.method.LSB
import com.staboss.stego.method.Method
import com.staboss.stego.util.errorMessage
import java.io.File

fun main(args: Array<String>) {
    if (args.contains("-h") || args.isEmpty()) {
        Parser.usage()
        return
    }

    val parser = Parser.getInstance()
    if (!parser.parseArgs(args)) return

    val method: Method
    val result: String

    with(parser) {
        if (resultFile.isNullOrEmpty()) {
            val file = File(sourceFile)
            resultFile = file.absolutePath.substring(0, file.absolutePath.lastIndexOf('/')) + "/new_${file.name}"
        }
    }

    method = if (parser.method == "DCT") DCT(parser.sourceFile) else LSB(parser.sourceFile)

    if (parser.embed) {
        val messageFile = File(parser.messageFile)
        if (!messageFile.exists()) errorMessage("Secret file does not exists!")
        val secretMessage = messageFile.readText()
        method.embed(secretMessage, parser.resultFile, parser.keyFile ?: "secretKey.txt")
        println("stego-img: ${File(parser.resultFile).absolutePath}")
        if (parser.method.toLowerCase() == "dct") {
            println("stego-key: ${File(parser.keyFile).absolutePath}")
        }
    } else {
        result = method.extract(parser.keyFile ?: "")
        println(result)
    }
}
