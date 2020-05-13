package com.staboss.stego

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

open class Image(path: String) {

    var image: BufferedImage = ImageIO.read(File(path))

    lateinit var components: List<IntArray>

    lateinit var red: IntArray
    lateinit var green: IntArray
    lateinit var blue: IntArray

    var width: Int = 0
    var height: Int = 0
    var capacity: Int = 0

    init {
        init()
    }

    protected fun init() {
        width = image.width
        height = image.height
        capacity = width * height

        red = IntArray(height * width)
        green = IntArray(height * width)
        blue = IntArray(height * width)

        var color: Color
        for (i in 0 until height) {
            for (j in 0 until width) {
                color = Color(image.getRGB(j, i), true)
                red[j + i * width] = color.red
                green[j + i * width] = color.green
                blue[j + i * width] = color.blue
            }
        }

        components = listOf(red, green, blue)
    }

    protected fun writeImage(dstPath: String): Boolean {
        val resImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val pixels = constructPixels()
        resImage.setRGB(0, 0, width, height, pixels, 0, width)
        ImageIO.write(resImage, "bmp", File(dstPath))
        return true
    }

    private fun constructPixels(): IntArray = (0 until capacity)
        .map { i -> Color(red[i], green[i], blue[i]).rgb }
        .toIntArray()
}
