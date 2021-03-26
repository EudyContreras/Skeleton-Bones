package com.eudycontreras.bones.shared

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<ViewBindingType : ViewBinding> : Fragment() {
    protected abstract fun init()
    var enableLifecycleLogging = false
    private var firstInit: Boolean = false
    private var previousLoadedView: View? = null

    private var _binding: ViewBindingType? = null
    protected val binding
        get(): ViewBindingType {
            return requireNotNull(_binding)
        }
    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> ViewBindingType

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (previousLoadedView == null) {
            _binding = bindingInflater.invoke(inflater, container, false)
            previousLoadedView = binding.root
            firstInit = true
        } else {
            (previousLoadedView?.parent as? ViewGroup)?.removeAllViews()
        }
        return previousLoadedView
    }

    override fun onStart() {
        super.onStart()
        if (firstInit) {
            firstInit = false
            init()
        }
    }

    fun isFinishing(): Boolean {
        return activity == null || activity?.isFinishing == true || isRemoving || isDetached || !isAdded
    }
}
