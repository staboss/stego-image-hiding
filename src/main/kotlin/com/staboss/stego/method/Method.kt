package com.staboss.stego.method

interface Method {

    fun embed(message: String, stegoImage: String, keyPath: String)

    fun extract(keyPath: String): String
}
