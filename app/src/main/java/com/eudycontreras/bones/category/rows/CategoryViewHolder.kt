package com.eudycontreras.bones.category.rows

import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.eudycontreras.bones.R
import com.eudycontreras.bones.category.Category
import com.eudycontreras.bones.category.ImageService
import com.eudycontreras.bones.category.Skeleton
import com.eudycontreras.bones.category.Skeleton.forImageView
import com.eudycontreras.bones.category.Skeleton.forTextView
import com.eudycontreras.bones.databinding.RowCategoryBinding
import com.eudycontreras.bones.shared.BaseViewHolder
import com.eudycontreras.boneslibrary.extensions.disableSkeletonLoading
import com.eudycontreras.boneslibrary.extensions.enableSkeletonLoading

class CategoryViewHolder(private val binding: RowCategoryBinding) : BaseViewHolder(binding.root) {

    fun bind(viewModel: CategoryViewModel, delegate: CategoryCellDelegate) {
        Skeleton.createIfNeeded(binding.root)
            .forImageView(binding.imageView, binding.root)
            .forTextView(binding.nameTextView, binding.root)
        binding.imageView.isVisible = true

        setupLevelBasedLayout(viewModel.category)

        if (viewModel.category.isEmpty()) {
            binding.nameTextView.enableSkeletonLoading()
            binding.expandCollapseImageView.isGone = true
            return
        }
        binding.nameTextView.post {
            binding.nameTextView.disableSkeletonLoading()
        }

        if (viewModel.category.isShowAll) {
            binding.imageView.setImageResource(R.drawable.forward_arrow)
            binding.imageView.post {
                binding.imageView.disableSkeletonLoading()
            }
        } else {
            ImageService().getImage(binding.root.context, viewModel.category.level) { image ->
                if (viewModel.category.name == viewModel.category.name && image != null) {
                    binding.imageView.setImageBitmap(image)
                    binding.imageView.disableSkeletonLoading()
                }
            }
        }

        binding.nameTextView.text = viewModel.category.displayName

        binding.expandCollapseImageView.setImageResource(if (viewModel.category.isExpanded) R.drawable.icon_up_arrow else R.drawable.icon_down_arrow)
        binding.expandCollapseImageView.isGone = viewModel.category.isLastLevel

        binding.root.setOnClickListener {
            delegate.didSelectCategory(viewModel.category)
        }
    }

    override fun onViewRecycled() {
//        binding.imageView.isInvisible = true
//        binding.imageView.setImageDrawable(null)
        binding.imageView.post {
            binding.imageView.enableSkeletonLoading()
            binding.nameTextView.enableSkeletonLoading()
        }

        binding.root.setOnClickListener(null)
    }

    private fun setupLevelBasedLayout(category: Category) {
        when (category.level) {
            1 -> {
                binding.root.setBackgroundResource(R.color.white)
                binding.bottomBorderView.setBackgroundResource(R.color.breeze)
            }
            2 -> {
                binding.root.setBackgroundResource(R.color.navy10)
                binding.bottomBorderView.setBackgroundResource(R.color.navy25)
            }
            3 -> {
                binding.root.setBackgroundResource(R.color.navy20)
                binding.bottomBorderView.setBackgroundResource(R.color.navy25)
            }
            4 -> {
                binding.root.setBackgroundResource(R.color.navy25)
                binding.bottomBorderView.setBackgroundResource(R.color.navy30)
            }
            else -> {
                binding.root.setBackgroundResource(R.color.navy25)
                binding.bottomBorderView.setBackgroundResource(R.color.navy30)
            }
        }
    }
}
