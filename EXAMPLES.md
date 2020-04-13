[![Dev Site Banner](media/images/android_dev_site_banner.png)](https://medium.com/android-dev-site)

<div align="center">
  
![Banner Demo](./media/gifs/wide_banner_white.gif)

## Table of Content
</div>

  
- **[Using Skeleton Bones with Glide](#using-skeleton-bones-with-glide)**
- **[Using Skeleton Bones with RecyclerViews](#using-skeleton-bones-with-recyclerviews)**
<br/>

## Using Skeleton Bones with Glide

Bones can be easily combined with glide or any other image loading framework for that matter. When it comes to glide, all we have to do is allow the glide request callback to notify the skeleton drawable that the image load process is completed. This can be done through the **`image.notifySkeletonImageLoaded()`** extension which is available for ImageViews.

```kotlin
val imageUrl: String
val imageView: ImageView

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
     .into(imageView)

```

Assuming that the skeleton or shimmer effect is being shown for the image being loaded, calling the **`image.notifySkeletonImageLoaded()`** extension function will switch the loading state to false and it will remove any actve shimmer effects or skeletons.

The extension function allows this to work with any image loader that allows listening to request results. If the target ImageView is within a SkeletonParent, you must allow the image view to be its own state owner by setting the **`    skeletonBoneStateOwener`** attribute to true. This only needed if you allowing the image view to listen to state changes with the **`skeletonBoneEnabled`** flag.

## Using Skeleton Bones with RecyclerViews

Using skeleton bones with RecyclerViews is easy and straightforward. There are multiple ways of representing skeleton content on RecyclerViews. In this solution what we'll do is pre-render a set number of dummy items in our RecyclerView. These items will be shown as skeletons. Once the data is available we can do one of two things. We can either update the dummy items with data or we can clear the list from all dummy data and add the real data to the list. In the example code below I will try to stay as close to a production application as possible. 

**Lets examine the example code!** 

Imagine our RecyclerView adapter data looks somewhat like this:

```kotlin
sealed class DemoData: DiffComparable {
    abstract val id: String

    override fun getIdentifier() = id

    data class A(
        override val id: String,
        val text: String,
        val imageUrl: String
    ) : DemoData()

    data class B(
        override val id: String,
        val textOne: String,
        val textTwo: String,
        val textThree: String,
        val imageUrl: String
    ) : DemoData()
}
```

We also have a simple resource wrapper for the data we get from our repository/service. The wrapper allows us to know the state of our data. When making an API call we create an empty **"loading"** resource and post/emit it to the ViewModel. This will let the view know that the data is currently being loaded we can determine that the data is loading if our ViewModel is null. An example of this is shown further below, once the data has been retrieved we post a resource with the data to the view. Our resource wrapper looks like this:

```kotlin
sealed class Resource<out T>(
    open val data: T?,
    open val loading: Boolean
) {
    class Success<T>(
        data: T
    ) : Resource<T>(
        data = data,
        loading = false
    )

    class Loading<T>(
        cache: T? = null
    ) : Resource<T>(
        data = cache,
        loading = true
    )
}
```

The above wrapper handles the loading state of our data. Error and empty states have been omitted for simplicity! As shown in the examples below using the resource is quite straight forward. Imagine we have our demo live data:

```kotlin
private val demoData: MutableLiveData<Resource<List<DemoData?>?>> = MutableLiveData()
```

Based on the state of our request our data is usually updated in more or less the following way:

```kotlin
demoData.postValue(Resource.Loading())

...

demoData.postValue(Resource.Success(dataCollection))
```

We can let the viewModel handle our data and determine when to show the dummy skeleton data. This can be done in the following way:

```kotlin
private val dummyData = arrayOfNulls<DemoData>(DUMMY_ENTRY_COUNT)

val items: LiveData<Resource<List<DemoData?>?>> = repository.getDemoData().map {
    if (it.loading) {
        Resource.Loading(dummyData.toList())
    } else it
}
```

In the code above, we initialize a collection with null elements and then we map the data based on its state. When the data is loading we can map it to a Loading resource containing null data. All of this can be done directly on the repository. But it is best if the repository does not know about visual dummy data. 

In this example; each item is an individual skeleton. The item is in the loading state when the viewModel is null. Our item  xml could look somewhat like this. The following attributes can be added to any upper level ViewGroup.

```xml
bones:skeletonEnabled="@{viewModel == null}"
bones:skeletonAllowSavedState="@{true}"
bones:skeletonBoneColor="@{@color/bone_color}"
bones:skeletonBoneCornerRadius="@{Utils.getDp(10)}"
bones:skeletonBoneMaxThickness="@{Utils.getDp(10)}"
bones:skeletonBoneMinThickness="@{Utils.getDp(10)}"
bones:skeletonDissectLargeBones="@{true}"
bones:skeletonShimmerRayColor="@{@color/bone_ray_color_alt}"
bones:skeletonShimmerRayCount="@{3}"
bones:skeletonShimmerRayInterpolator="@{@android:interpolator/fast_out_slow_in}"
bones:skeletonShimmerRaySharedInterpolator="@{true}"
bones:skeletonShimmerRaySpeedMultiplier="@{1.1f}"
bones:skeletonShimmerRayThicknessRatio="@{0.7f}"
bones:skeletonShimmerRayTilt="@{-0.1f}"
bones:skeletonTransitionDuration="@{200L}"
bones:skeletonUseStateTransition="@{true}"
```
The above xml yields the following result: [Demo Recycler](https://github.com/EudyContreras/Skeleton-Bones/blob/development/media/gifs/recycler-demo.gif)

For a full working demo try out the **Demo-1** branch

### More examples coming soon!

You are welcome to try out the demo app and inspect the xml for a full
live example of Bones.
