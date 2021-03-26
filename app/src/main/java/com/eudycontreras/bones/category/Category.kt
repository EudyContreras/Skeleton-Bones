package com.eudycontreras.bones.category

import java.io.Serializable
import java.util.*

data class Category(
    val name: String, // ID
    val displayName: String,
    val isLastLevel: Boolean,
    val level: Int,
    val isShowAll: Boolean = false
) : Serializable {
    var isExpanded = false
        private set
    var childCategories = listOf<Category>()
        private set

    fun isEmpty(): Boolean = displayName.isEmpty()

    fun toggleExpanded() {
        isExpanded = !isExpanded
    }

    fun setChildCategories(categories: List<Category>) {
        childCategories = categories
    }

    companion object {
        const val serialVersionUID = 1L
        fun empty(parent: Category?): Category =
            Category(name = UUID.randomUUID().toString(),
                     displayName = "",
                     isLastLevel = false,
                     level = 1 + (parent?.level ?: 0))
    }
}
