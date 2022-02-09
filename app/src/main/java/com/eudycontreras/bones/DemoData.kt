package com.eudycontreras.bones

data class DemoData(
    val id: String,
    val textOne: String,
    val textTwo: String,
    val textThree: String,
    val imageUrl: String
): DiffComparable {
    override fun getIdentifier(): String = id
}
