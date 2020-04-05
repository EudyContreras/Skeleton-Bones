
<div align="center">
  
![Banner Demo](../../media/gifs/wide_banner_white.gif)

# Bone Shimmer Rays
</div>

<br/>

A Shimmer Ray is the effect that overlays a skeleton or bone in order to communicate to the user that the View or ViewGroup is currently loading. Shimmer rays can be applied to both Skeletons and individual bones. Most aspects of the shimmer rays can be customized. For a full list of all the attributes that can be customized take a look below.
  

The properties covered in this section can only be applied to a Bone or Bone Drawable. These properties will create shimmer effects that overlay the whole View.

### Glossary

* **Dimension** A dimension which should be given in dps  
* **Fraction** A fraction where the offset can be either 0f or 1f.  
* **FOSI** Refers to the FastOutSlowIn interpolator
<br/>

### Shimmer Ray Attributes
<br/>

| Property| Type | Availability | Default  |
|:----------|:-----|:-------------|:---------:|
|**skeletonBoneShimmerRayColor** | ViewGroup  |  Integer, ColorRes | **`White`** |
|**skeletonBoneShimmerRayTilt** | ViewGroup   | Float, Fraction  | **`-0.3f`** |
|**skeletonBoneShimmerRayCount** | ViewGroup    | Integer | **`0`** |
|**skeletonBoneShimmerRayThickness** | ViewGroup     | Float, Dimension | **`120.dp`** |
|**skeletonBoneShimmerRayThicknessRatio** | ViewGroup | Float, Fraction | **`0.45f`** |
|**skeletonBoneShimmerRayInterpolator** | ViewGroup| Interpolator | **`FOSI`** |
|**skeletonBoneShimmerRaySharedInterpolator** &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; | ViewGroup | Boolean | **`true`** |
|**skeletonBoneShimmerRaySpeedMultiplier** | ViewGroup &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;| Float, Fraction | **`1.0f`** |
<br/>

### Shimmer Ray Attribute Descriptions  

<br/>

**skeletonBoneShimmerRayColor** Sets the base color of the shimmer rays. The set color is then used to generate a gradient.

**skeletonBoneShimmerRayTilt** Sets the tilt/skew used for the shimmer rays. Negative values will skew the ray to right while positive values will skew it to the left. The recommended range is between -0.5 and 0.5. Values that are lower or higher will make the shimmer ray look odd.

**skeletonBoneShimmerRayCount** Sets the amount of shimmer rays that should be rendered and animated on the Skeleton.

**skeletonBoneShimmerRayThickness** Sets the shimmer ray thickness. If both `skeletonShimmerRayThicknessRatio` and the `skeletonShimmerRayThickness` are defined, the skeletonShimmerRayThickness will take priority. Default: 120dp

**skeletonBoneShimmerRayThicknessRatio** Sets the shimmer ray thickness ratio. The ratio lets the ray be created at the specified percentage of its parent skeleton width. A ratio of 0.2f means that the shimmer ray's thickness will be 20 percent of the skeletons's width. If both skeletonShimmerRayThicknessRatio and skeletonShimmerRayThickness are defined, the skeletonShimmerRayThickness will take priority.

**skeletonBoneShimmerRaySpeedMultiplier** Sets the shimmer ray speed multiplier. Values greater than 1.0f will result in faster animation speeds, while values lower than 1.0 will result in slower animation speeds.

**skeletonBoneShimmerRaySharedInterpolator**  Determines whether or nt the Interpolator set for animating the shimmer rays should be shared or divided among all the shimmer rays.

**skeletonBoneShimmerRayInterpolator** Sets the interpolator to be use when animating the shimmer rays. The interpolator will either be shared among all the rays or divided among them based on the value of: `skeletonShimmerRaySharedInterpolator`.
The default interpolator is `FastOutSlowInInterpolator()` The interpolators available through the android framework as of now are the following:
* accelerate_cubic
* accelerate_decelerate
* accelerate_quad
* accelerate_quint
* anticipate
* anticipate_overshoot
* bounce
* cycle
* decelerate_cubic
* decelerate_quad
* decelerate_quint
* fast_out_extra_slow_in
* fast_out_linear_in
* fast_out_slow_in
* linear
* linear_out_slow_in
* overshoot
