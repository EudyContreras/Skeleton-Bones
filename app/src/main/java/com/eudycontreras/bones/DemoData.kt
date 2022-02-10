package com.eudycontreras.bones

data class DemoData(
    override val id: String,
    val textOne: String,
    val textTwo: String,
    val textThree: String,
    val imageUrl: String
): DiffComparable