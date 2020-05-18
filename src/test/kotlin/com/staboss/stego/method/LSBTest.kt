package com.staboss.stego.method

import com.staboss.stego.data.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class LSBTest {

    lateinit var lsb: LSB

    var extracted = ""

    @Before
    fun init() {
        lsb = LSB(srcIMG)
    }

    @Test
    fun `small text test`() {
        lsb.embed(text256, resIMG, "")
        extracted = lsb.extract(keyBin)
        Assert.assertEquals(text256, extracted)
    }

    @Test
    fun `large text test`() {
        lsb.embed(text4096, resIMG, "")
        extracted = lsb.extract(keyBin)
        Assert.assertEquals(text4096, extracted)
    }
}