package com.staboss.stego.method

import com.staboss.stego.Image
import com.staboss.stego.util.math
import com.staboss.stego.util.doubleLoop
import com.staboss.stego.util.errorMessage
import com.staboss.stego.util.toBinary
import com.staboss.stego.util.toText
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.*
import kotlin.math.cos
import kotlin.math.sqrt

class DCT(imagePath: String) : Image(imagePath), Method {

    private val m = 8
    private val n = 8

    private var color: Array<IntArray> = Array(height) { IntArray(width) }
    private val dctCoefficients = Array(m) { DoubleArray(n) }

    override fun embed(message: String, stegoImage: String, keyPath: String) {
        val oos = ObjectOutputStream(FileOutputStream(keyPath))

        val binaryString = message.toBinary()
        val colorBuffer = Array(m) { IntArray(n) }

        paddingImage()
        twoDColor()

        if (binaryString.length >= capacity) {
            errorMessage("The message is too long to be embedded in this Image!")
        }

        var pos = 0

        loop@
        for (i in 0 until (height - 8) step 8) {
            for (j in 0 until (width - 8) step 8) {
                for (k in 0 until m) {
                    for (l in 0 until n) {
                        colorBuffer[k][l] = color[i + k][j + l]
                    }
                    computeDCT(colorBuffer)
                }
                for (k in 0 until m) {
                    for (l in 0 until n) {
                        if (dctCoefficients[k][l] < 0) {
                            if (pos >= binaryString.length) break@loop
                            color[i + k][j + l] = math.changeLSB(color[i + k][j + l], binaryString[pos++])
                            oos.writeBoolean(true)
                        } else {
                            oos.writeBoolean(false)
                        }
                    }
                }
            }
        }

        oneDColor()
        oos.close()

        writeImage(stegoImage)
    }

    override fun extract(keyPath: String): String {
        val ois = ObjectInputStream(FileInputStream(keyPath))
        twoDColor()

        var result = ""

        loop@
        for (i in 0 until (height - 8) step 8) {
            for (j in 0 until (width - 8) step 8) {
                for (k in 0 until m) {
                    for (l in 0 until n) {
                        try {
                            if (ois.readBoolean()) {
                                result += color[i + k][j + l].toBinary().last()
                            }
                        } catch (exp: IOException) {
                            break@loop
                        }
                    }
                }
            }
        }

        ois.close()
        return result.toText()
    }

    private fun computeDCT(component: Array<IntArray>) {
        var ci: Double
        var cj: Double

        var dct: Double
        var sum: Double

        for (i in 0 until m) {
            for (j in 0 until n) {
                // ci and cj depend on the frequency, as well as on the number of rows and columns of the matrix
                ci = if (i == 0) 1 / sqrt(m.toDouble()) else sqrt(2.0) / sqrt(m.toDouble())
                cj = if (j == 0) 1 / sqrt(n.toDouble()) else sqrt(2.0) / sqrt(n.toDouble())

                // 'sum' temporarily stores the sum of cosine signals
                sum = 0.0
                for (k in 0 until m) {
                    for (l in 0 until n) {
                        dct = component[k][l] *
                                cos(((2 * k + 1) * i * Math.PI) / (2 * m)) *
                                cos(((2 * l + 1) * j * Math.PI) / (2 * n))
                        sum += dct
                    }
                }

                dctCoefficients[i][j] = ci * cj * sum
            }
        }
    }

    private fun paddingImage() {
        val wBuffer = 8 - if (width % 8 == 0) 8 else width % 8
        val hBuffer = 8 - if (height % 8 == 0) 8 else height % 8

        val newImg = BufferedImage(width + wBuffer, height + hBuffer, BufferedImage.TYPE_INT_RGB)

        with(newImg) {
            doubleLoop(newImg.height, newImg.width) { i, j ->
                when {
                    i < super.height && j < super.width -> setRGB(j, i, image.getRGB(j, i))
                    i < super.height && j >= super.width -> setRGB(j, i, image.getRGB(j - wBuffer, i))
                    i >= super.height -> setRGB(j, i, getRGB(j, i - hBuffer))
                }
            }
        }

        super.image = newImg
        super.init()
    }

    private fun twoDColor() = doubleLoop(height, width) { i, j ->
        color[i][j] = Color(image.getRGB(j, i)).red
    }

    private fun oneDColor() = (0 until height).forEach { i ->
        System.arraycopy(color[i], 0, red, i * width, width)
    }
}
