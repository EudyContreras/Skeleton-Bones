package com.eudycontreras.bones.category.rows

import com.eudycontreras.bones.category.Category

interface CategoryCellDelegate {
    fun didSelectCategory(selectedCategory: Category)
}
