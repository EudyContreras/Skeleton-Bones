package com.eudycontreras.bones.category

import com.eudycontreras.bones.category.rows.CategoryViewModel
import com.eudycontreras.bones.shared.BaseDataSource
import com.eudycontreras.bones.shared.BaseViewModel

class CategoryDataSource : BaseDataSource() {
    var rootCategory: Category? = null

    override fun buildViewModels(): List<BaseViewModel> {
        val list = mutableListOf<BaseViewModel>()

        val rootCategory = rootCategory
        if (rootCategory == null) {
            val categories = addCategorySkeletons()
            val categoryViewModels = categories.map { category ->
                CategoryViewModel(category)
            }

            list.addAll(categoryViewModels)

            return list
        }

        list.addAll(buildCategoryTree(rootCategory))
        return list
    }

    fun addCategorySkeletons(parent: Category? = null): List<Category> {
        val categories = mutableListOf<Category>()

        for (i in 0..10) {
            categories.add(Category.empty(parent))
        }

        return categories
    }

    private fun buildCategoryTree(category: Category): List<CategoryViewModel> {
        val expandedCategory = category.childCategories.firstOrNull { it.isExpanded }

        if (expandedCategory != null) {
            val categories = mutableListOf<CategoryViewModel>()

            categories.add(CategoryViewModel(expandedCategory))
            categories.addAll(buildCategoryTree(expandedCategory))
            return categories
        }

        return category.childCategories.map { CategoryViewModel(it) }
    }
}
