package com.eudycontreras.bones

sealed class DemoData {
    class One(
        val textOne: String,
        val textTwo: String,
        val imageUrl: String
    ): DemoData()

    class Two(
        val text: String,
        val imageOneUrl: String,
        val imageTwoUrl: String
    ): DemoData()

    class Three(
        val text: String,
        val imageUrl: String
    ): DemoData()
}