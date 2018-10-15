package com.qmakercorp.qmaker.extensions

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder

fun BarcodeEncoder.generateQRCode(text: String): Bitmap {
    val multiFormatWriter = MultiFormatWriter()
    val bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 750, 750)
    return this.createBitmap(bitMatrix)
}