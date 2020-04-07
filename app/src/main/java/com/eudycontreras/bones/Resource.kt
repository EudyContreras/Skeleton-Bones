package com.eudycontreras.bones

/**
 * Copyright (C) 2020 Project X
 *
 * @Project ProjectX
 * @author Eudy Contreras.
 * @since April 2020
 */

class Resource<T: DemoData>(
    val data: T? = null,
    val loading: Boolean = true
)