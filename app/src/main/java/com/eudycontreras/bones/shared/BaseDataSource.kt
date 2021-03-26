package com.eudycontreras.bones.shared

@Suppress("UnnecessaryAbstractClass")
abstract class BaseDataSource {
    abstract fun buildViewModels(): List<BaseViewModel>
}
