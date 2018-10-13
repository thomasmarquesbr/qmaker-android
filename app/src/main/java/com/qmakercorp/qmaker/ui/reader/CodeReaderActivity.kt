package com.qmakercorp.qmaker.ui.reader

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.qmakercorp.qmaker.R
import com.qmakercorp.qmaker.components.Alert
import com.wajahatkarim3.easyvalidation.core.view_ktx.nonEmpty
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import kotlinx.android.synthetic.main.activity_code_reader.*


class CodeReaderActivity : Activity() {

    private val MY_CAMERA_REQUEST_CODE = 100

    private val qrCodeDetector by lazy {
        BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build()
    }

    private val cameraSource by lazy {
        CameraSource.Builder(this, qrCodeDetector)
                .setRequestedPreviewSize(640, 480)
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code_reader)
        configCodeReader()
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                configCodeReader()
                startCodeReader()
                cameraSource.start(camera_view.holder)
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    /** PRIVATE **/

    private fun checkPermission(): Boolean {
        val permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
        return permission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA),
                MY_CAMERA_REQUEST_CODE)
    }

    private fun startCodeReader() {
        qrCodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {}
            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if (barcodes.size() != 0) {
                    val code = barcodes.valueAt(0).displayValue
                    validateCode(code)
                }
            }
        })
    }

    private fun validateCode(code: String?) {

    }

    private fun configCodeReader() {
        camera_view.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                if (checkPermission()) {
                    cameraSource.start(camera_view.holder)
                    startCodeReader()
                } else
                    requestPermission()
            }
            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) { }
            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }
        })
    }

    /** PUBLIC **/

    fun didTapSend(view: View) {
        et_code.validator()
                .nonEmpty()
                .addSuccessCallback {
                    validateCode(et_code.text.toString())
                }.addErrorCallback {
                    Alert(this, getString(R.string.caution), getString(R.string.code_empty_message)).show()
                }.check()
    }

}
