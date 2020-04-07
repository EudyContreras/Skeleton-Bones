package com.eudycontreras.bones

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions

/**
 * Copyright (C) 2020 Project X
 *
 * @Project ProjectX
 * @author Eudy Contreras.
 * @since April 2020
 */

@BindingAdapter("imageUrl")
fun ImageView.loadImage(imageUrl: String?) {
    if (imageUrl == null) return

    Glide.with(context)
        .load(imageUrl)
        .apply(RequestOptions().format(DecodeFormat.PREFER_ARGB_8888))
        .into(this)
}