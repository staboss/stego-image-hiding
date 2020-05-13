package com.staboss.stego.method

import com.staboss.stego.Image
import com.staboss.stego.util.errorMessage
import com.staboss.stego.util.math
import com.staboss.stego.util.toBinary

class LSB(imagePath: String) : Image(imagePath), Method {

    override fun embed(message: String, stegoImage: String, keyPath: String) {
        val length = message.length
        val binaryString = "$length:$message".toBinary()

        val binLen = binaryString.length

        if (binLen >= capacity) {
            errorMessage("The message is too long to be embedded in this Image!")
        }

        var indexRGB = 0
        var indexMSG = 0
        while (true) {
            if ((indexMSG + 1) <= binLen) {
                red[indexRGB] = math.changeLSB(red[indexRGB], binaryString[indexMSG])

                if ((indexMSG + 2) <= binLen) {
                    green[indexRGB] = math.changeLSB(green[indexRGB], binaryString[indexMSG + 1])
                }

                if ((indexMSG + 3) <= binLen) {
                    blue[indexRGB] = math.changeLSB(blue[indexRGB], binaryString[indexMSG + 2])
                }
            } else {
                break
            }
            indexMSG += 3; indexRGB++
        }

        writeImage(stegoImage)
    }

    override fun extract(keyPath: String): String {
        var buffer = ""
        var counter = 0

        var messageBits = ""
        var messageSize: Int? = null

        for (i in 0 until capacity) {
            for (component in components) {
                buffer += component[i].toBinary().last()
                counter++

                if (counter == 8) {
                    messageBits += buffer.toInt(radix = 2).toChar()
                    counter = 0
                    buffer = ""

                    if (messageBits.last() == ':' && messageSize == null) {
                        try {
                            messageSize = messageBits.substring(0, messageBits.length - 1).toInt()
                        } catch (e: NumberFormatException) {
                            e.printStackTrace()
                        }
                    }
                }
            }

            if ((messageBits.length - messageSize.toString().length - 1) == messageSize) {
                return messageBits.substring(messageSize.toString().length + 1)
            }
        }

        return ""
    }
}