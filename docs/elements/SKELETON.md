
<div align="center">
  
![Banner Demo](../../media/gifs/wide_banner_white.gif)

# Skeleton 
</div>

<br/>

A drawable loader representation of a ViewGroup. The skeleton is the wrapper around the generated bones where each bone represents a view within the ViewGroup. The skeleton is in charge of managing and rendering each of the bones generated for each non-ignored view within the owner layout. Skeleton drawables are recommended over single Bone Drawables when possible since the a single drawable is used for representing a complex layout. The skeleton drawable is created and overlaid over the layout in order to render the bones and the visible shimmers. See [Shimmer Rays](./SHIMMER_RAY.md)

### Glossary

* **Dimension** A dimension which should be given in dps  
* **Fraction** A fraction where the offset can be either 0f or 1f. 
<br/>

### Skeleton Attributes
<br/>

The properties covered in this section only apply to SkeletonsDrawables

| Property | Type | Availability | Default  |
|:----------|:-----|:-------------|:---------:|
|**skeletonEnabled** | ViewGroup |  Boolean | **`true`** |
|**skeletonBackgroundColor** | ViewGroup     | Integer, ColorRes   &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;| **`null`** |
|**skeletonCornerRadius** | ViewGroup    | Float, Dimension | **`0f`** |
|**skeletonLayoutIgnored** | ViewGroup    | Boolean | **`false`** |
|**skeletonGenerateBones** | ViewGroup | Boolean | **`true`** |
|**skeletonAllowSavedState** | ViewGroup | Boolean | **`false`** |
|**skeletonAllowWeakSavedState** | ViewGroup | Boolean | **`false`** |
|**skeletonDissectLargeBones** | ViewGroup | Boolean | **`false`** |
|**skeletonUseStateTransition** | ViewGroup | Boolean | **`true`** |
|**skeletonTransitionDuration** | ViewGroup | Long | **`250L`** |
|**skeletonAnimateRestoredBounds** &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;| ViewGroup &nbsp;  &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;| Boolean | **`false`** |
<br/>

### Skeleton Attributes Descriptions

<br/>

**skeletonEnabled:**  Determines whether or not this ViewGroup should be represented as a skeleton. The initial state should ideally be true when set. This property should always be present since it is the property that determines when the skeleton should be removed. If the state can go from true to false it is recommended that the `skeletonAllowSavedState`is used.

**skeletonBackgroundColor:** Sets a background color to the skeleton. Setting a color on the skeleton will override the background is not recommended as it will override the ViewGroups background since the skeleton is an overlay. If this property is used.

**skeletonGenerateBones:** When true, bones are generated for each View in the ViewGroup. When false no bone is generated. Can be used in order to apply only a shimmer ray effect to a ViewGroup. This is equivalent to ignoring all the views within the ViewGroup

**skeletonAllowSavedState:** Determines whether or not the internal state of the skeleton representation of the Skeleton should be saved in memory. Useful when the state could change from true to false (Content to Loading). If the state of the drawable is never expected to change from false to true it is best to keep this set to false.

**skeletonAllowWeakSavedState:** Determines whether or not the internal state of the skeleton representation of the Skeleton should be saved in memory as a weak reference. Useful when the state could change from true to false (Content to Loading). If the state of the drawable is never expected to change from false to true it is best to keep this set to false.

**skeletonDissectLargeBones:** When true, bones which exceed the max set thickness will be dissected into a set of smaller bones. The smaller bones are evenly distributed through the space.

**skeletonTransitionDuration:** Sets the duration of the drawable's transition between enabled and disabled. Only takes effect if the `skeletonUseStateTransition` property is set to true.

**skeletonUseStateTransition:** Determines if a transition should be used for removing the skeleton drawable from the ViewGroup. When false the drawable is removed immediately without any transition.

**skeletonGenerateBones:** When true, bones are generated for each View in the ViewGroup. When false no bone is generated. Can be used in order to apply only a shimmer ray effect to a ViewGroup. This is equivalent to ignoring all the views within the ViewGroup

**skeletonAnimateRestoredBounds:** When true, view bounds that have been resize in order to accommodate minimum dimensions set in order to build valid bones will be restored to their default values using a simple layout animation. This is done to provide a smoother using experience in such cases.

**skeletonCornerRadius:** Sets the corner radius for this skeleton. This property would only be noticeable if the skeleton has an opaque color. This value will also affect clipping.

**skeletonLayoutIgnored:** Determines if the layout bound to this property should be ignored while generating the skeleton. If true all the view within this layout will not be generated. If this attribute is set directly on a SkeletonDrawable owner or on a View, it will take no effect.

