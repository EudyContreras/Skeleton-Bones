package com.eudycontreras.bones

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.eudycontreras.boneslibrary.extensions.notifySkeletonImageLoaded

/**
 * Copyright (C) 2020 Project X
 *
 * @Project ProjectX
 * @author Eudy Contreras.
 * @since February 2021
 */

fun Int.isEven() = this % 2 == 0

fun ImageView.loadImage(imageUrl: String?) {
    if (imageUrl == null) return

    Glide.with(context)
        .load(imageUrl)
        .addListener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                exception: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                this@loadImage.notifySkeletonImageLoaded()
                return false
            }

        })
        .apply(RequestOptions().format(DecodeFormat.PREFER_ARGB_8888))
        .into(this)
}

@Suppress("UNCHECKED_CAST")
fun <T : DemoData> RecyclerView.setItemData(
    data: List<T?>
) {
    if (this.adapter == null) {
        this.adapter = ItemAdapter(data)
    } else {
        with(adapter as ItemAdapter<T>) {
            this.updateData(data)
        }
    }
}