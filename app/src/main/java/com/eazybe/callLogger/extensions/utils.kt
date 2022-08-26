package com.eazybe.callLogger.extensions

import Config
import android.app.Activity
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.Toast
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.eazybe.callLogger.BaseActivity
import com.eazybe.callLogger.R
import com.eazybe.callLogger.helper.AppConstants.SHORT_ANIMATION_DURATION
import com.eazybe.callLogger.helper.AppConstants.isOnMainThread
import com.google.android.material.textfield.TextInputEditText
import com.simplemobiletools.commons.extensions.getProperBackgroundColor
import com.simplemobiletools.commons.extensions.isUsingSystemDarkTheme
import com.simplemobiletools.commons.extensions.lightenColor
import glimpse.glide.GlimpseTransformation


fun Context.toast(msg: String, length: Int = Toast.LENGTH_SHORT) {
        try {
            if (isOnMainThread()) {
                doToast(this, msg, length)
            } else {
                Handler(Looper.getMainLooper()).post {
                    doToast(this, msg, length)
                }
            }
        } catch (e: Exception) {
        }
    }

    private fun doToast(context: Context, message: String, length: Int) {
        if (context is Activity) {
            if (!context.isFinishing && !context.isDestroyed) {
                Toast.makeText(context, message, length).show()
            }
        } else {
            Toast.makeText(context, message, length).show()
        }
    }
fun View.beInvisibleIf(beInvisible: Boolean) = if (beInvisible) beInvisible() else beVisible()

fun View.beVisibleIf(beVisible: Boolean) = if (beVisible) beVisible() else beGone()

fun View.beGoneIf(beGone: Boolean) = beVisibleIf(!beGone)

fun View.beInvisible() {
    visibility = View.INVISIBLE
}

fun View.beVisible() {
    visibility = View.VISIBLE
}

fun View.beGone() {
    visibility = View.GONE
}

fun View.onGlobalLayout(callback: () -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            viewTreeObserver.removeOnGlobalLayoutListener(this)
            callback()
        }
    })
}

fun View.isVisible() = visibility == View.VISIBLE

fun View.isInvisible() = visibility == View.INVISIBLE

fun View.isGone() = visibility == View.GONE

fun View.performHapticFeedback() = performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)

fun View.fadeIn() {
    animate().alpha(1f).setDuration(SHORT_ANIMATION_DURATION).withStartAction { beVisible() }.start()
}

fun View.fadeOut() {
    animate().alpha(0f).setDuration(SHORT_ANIMATION_DURATION).withEndAction { beGone() }.start()
}

val Context.config: Config get() = Config.newInstance(applicationContext)


fun Context.getCurrentClip(): String? {
    val clipboardManager = (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
    return clipboardManager.primaryClip?.getItemAt(0)?.text?.trim()?.toString()
}

fun Context.getStrokeColor(): Int {
    return if (config.isUsingSystemTheme) {
        if (isUsingSystemDarkTheme()) {
            resources.getColor(R.color.black, theme)
        } else {
            resources.getColor(R.color.black, theme)
        }
    } else {
        val lighterColor = getProperBackgroundColor().lightenColor()
        if (lighterColor == Color.WHITE || lighterColor == Color.BLACK) {
            resources.getColor(R.color.black, theme)
        } else {
            lighterColor
        }
    }



}
fun ImageView.loadImage(uri: String?, progressDrawable: CircularProgressDrawable) {

    val options = RequestOptions()
        .placeholder(progressDrawable)
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .transform(GlimpseTransformation())
        .error(R.drawable.person_image)

    Glide.with(context)
        .setDefaultRequestOptions(options)
        .asBitmap()
        .load(uri)
        .dontAnimate()
        .listener(object : RequestListener<Bitmap> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Bitmap>?,
                isFirstResource: Boolean
            ): Boolean {
                Log.d("glideError","${e!!.logRootCauses("GLIDE")}")
                return false
            }

            override fun onResourceReady(
                resource: Bitmap?,
                model: Any?,
                target: Target<Bitmap>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

        })
        .into(this)

    this.setBackgroundColor(Color.TRANSPARENT)

}
fun getProgressDrawable(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }
}





