package com.eudycontreras.bones.category

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import com.eudycontreras.bones.R
import kotlin.random.Random


class ImageService {
    fun getImage(context: Context, completion: (image: Bitmap?) -> Unit) {
        val delay = Random(3123).nextLong(200, 5000)

        Handler(Looper.getMainLooper()).postDelayed({
            val icon = BitmapFactory.decodeResource(
                context.resources,
                R.drawable.img_female_avatar
            )
            completion(icon)
        }, delay)
    }
}