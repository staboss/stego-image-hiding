package com.staboss.stego.method

import com.staboss.stego.data.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class DCTTest {

    lateinit var dct: DCT

    var extracted = ""

    @Before
    fun init() {
        dct = DCT(srcIMG)
    }

    @Test
    fun `small text test`() {
        dct.embed(text256, resIMG, keyBin)
        extracted = dct.extract(keyBin)
        Assert.assertEquals(text256, extracted)
    }

    @Test
    fun `large text test`() {
        dct.embed(text4096, resIMG, keyBin)
        extracted = dct.extract(keyBin)
        Assert.assertEquals(text4096, extracted)
    }
}
