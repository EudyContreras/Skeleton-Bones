[![Dev Site Banner](media/images/android_dev_site_banner.png)](https://medium.com/android-dev-site)

<div align="center">
  
![Banner Demo](./media/gifs/wide_banner_white.gif)

</div>

## Using Skeleton Bones with Glide

Bones can be easily combined with glide by allowing the glide request callback to notify the skeleton drawable that the image load process is completed. This can be done through the **`image.notifySkeletonImageLoaded()`** extension which is available for ImageViews.

```kotlin
Glide.with(context)
     .load(imageUrl)
     .addListener(object : RequestListener<Drawable> {
         override fun onLoadFailed(
             exception: GlideException?,
             model: Any?,
             target: Target<Drawable>?,
             isFirstResource: Boolean
         ): Boolean {
             return this@loadImage.notifySkeletonImageLoaded()
         }

         override fun onResourceReady(
             resource: Drawable?,
             model: Any?,
             target: Target<Drawable>?,
             dataSource: DataSource?,
             isFirstResource: Boolean
         ): Boolean {
             return this@loadImage.notifySkeletonImageLoaded()
         }

     })
     .apply(RequestOptions().format(DecodeFormat.PREFER_ARGB_8888))
     .into(this)

```

Assuming that the skeleton or shimmer effect is being shown for the image being loaded, calling the **`image.notifySkeletonImageLoaded()`** extension function will switch the loading state to false and it will remove any actve shimmer effects or skeletons.

The extension function allows this to work with any image loader that allows listening to request results. If the target ImageView is within a SkeletonParent, you must allow the image view to be its own state owner by setting the **`    skeletonBoneStateOwener`** attribute to true. This only needed if you allowing the image view to listen to state changes with the **`skeletonBoneEnabled`** flag.

### More examples coming soon!

You are welcome to try out the demo app and inspect the xml for a full
live example of Bones.