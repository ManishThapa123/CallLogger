package com.eazybe.callLogger.ScreenShot

import android.content.Intent
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.eazybe.callLogger.extensions.toast

class ScreenShotActivity : AppCompatActivity() {
    private var mgr: MediaProjectionManager? = null
    private var mediaProjection : MediaProjection? = null
    private val requestReadMediaProjectionPermission =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
//                mediaProjection = mgr!!.getMediaProjection(result.resultCode, result.data!!)
                val i: Intent = Intent(this, ScreenshotService::class.java)
                    .putExtra(ScreenshotService.EXTRA_RESULT_CODE, result.resultCode)
                    .putExtra(ScreenshotService.EXTRA_RESULT_INTENT, result.data)

                startService(i)

                toast("Accepted permission")
                finish()
            } else {
                toast("Denied permission")
                finish()
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mgr = getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        askForMediaProjectionPermission()
    }

    private fun askForMediaProjectionPermission(){
        requestReadMediaProjectionPermission.launch(mgr!!.createScreenCaptureIntent())
    }
}