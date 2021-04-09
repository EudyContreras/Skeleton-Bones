package com.eudycontreras.bones.category

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.eudycontreras.bones.category.rows.CategoryCellDelegate
import com.eudycontreras.bones.databinding.FragmentCategoryBinding
import com.eudycontreras.bones.shared.BaseFragment
import java.util.*

class CategoryFragment : BaseFragment<FragmentCategoryBinding>(), CategoryCellDelegate {
    private var dataSource = CategoryDataSource()
    private var adapter = CategoryAdapter(this)

    override fun init() {
        val layoutManager = LinearLayoutManager(context)
        binding.categoryList.layoutManager = layoutManager
        adapter.setItems(dataSource.buildViewModels())
        binding.categoryList.itemAnimator = null
        binding.categoryList.adapter = adapter

        loadCategory { category ->
            dataSource.rootCategory = category
            adapter.refreshList(binding.categoryList, dataSource)
        }
    }

    override fun didSelectCategory(selectedCategory: Category) {
        if (selectedCategory.isLastLevel) {
            return
        }

        selectedCategory.setChildCategories(dataSource.addCategorySkeletons(selectedCategory))
        selectedCategory.toggleExpanded()
        adapter.refreshList(binding.categoryList, dataSource)
        binding.categoryList.scrollToPosition(0)

        loadCategory(selectedCategory) { category ->
            selectedCategory.setChildCategories(category.childCategories)
            adapter.refreshList(binding.categoryList, dataSource)
        }
    }

    private fun loadCategory(category: Category? = null, completion: (category: Category) -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed({
            val level = (category?.level ?: 1) + 1
            val childCategories = mutableListOf<Category>()
            for (i in 0..12) {
                childCategories.add(
                    Category(
                        UUID.randomUUID().toString(),
                        "Category $i, level ${level - 1}",
                        level == 5,
                        level,
                        level == 5 && i == 0
                    )
                )
            }

            val categoryToReturn = category ?: Category("Root", "Root", false, 0, false)
            categoryToReturn.setChildCategories(childCategories)
            completion(categoryToReturn)
        }, 4000)
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCategoryBinding
        get() = FragmentCategoryBinding::inflate
}
