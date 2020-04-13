package com.eudycontreras.bones

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

