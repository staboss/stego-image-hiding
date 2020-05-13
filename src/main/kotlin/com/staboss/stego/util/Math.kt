package com.staboss.stego.util

import java.awt.image.BufferedImage
import java.awt.image.Raster
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.sqrt

object Math {

    /**
     * Рассчитывает значение PSNR двух изображений
     *
     * @param image1 изображение 1
     * @param image2 изображение 2
     * @return значение PSNR
     */
    fun psnr(image1: BufferedImage, image2: BufferedImage): String {
        val mse = mse(image1, image2)
        val psnr = 10.0 * log10(255.0.pow(2.0) / mse)
        return String.format("%.4f", psnr).replace(",", ".")
    }

    /**
     * Рассчитывает значение MSE двух изображений
     *
     * @param image1 изображение 1
     * @param image2 изображение 2
     * @return значение RMSE
     */
    fun mse(image1: BufferedImage, image2: BufferedImage): Double {
        assert(
            image1.type == image2.type && image1.height == image2.height && image1.width == image2.width
        )

        val w = image1.width
        val h = image1.height
        val r1: Raster = image1.raster
        val r2: Raster = image2.raster

        var mse = 0.0
        for (j in 0 until h)
            for (i in 0 until w)
                mse += (r1.getSample(i, j, 0) - r2.getSample(i, j, 0).toDouble()).pow(2.0)

        mse /= (w * h).toDouble()
        return mse
    }

    /**
     * Рассчитывает значение RMSE двух изображений
     *
     * @param image1 изображение 1
     * @param image2 изображение 2
     * @return значение RMSE
     */
    fun rmse(image1: BufferedImage, image2: BufferedImage): String {
        val mse = mse(image1, image2)
        val rmse = sqrt(mse)
        return String.format("%.6f", rmse).replace(",", ".")
    }

    /**
     * Логарифм по основанию 10
     *
     * @param x значение логарифма
     * @return log10(x)
     */
    fun log10(x: Double): Double {
        return ln(x) / ln(10.0)
    }

    fun changeLSB(component: Int, bit: Char): Int {
        return changeLSB(component, if (bit == '1') 1 else 0)
    }

    fun changeLSB(component: Int, bit: Int): Int {
        return component and 1.inv() or bit
    }
}
