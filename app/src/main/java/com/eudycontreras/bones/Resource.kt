package com.eudycontreras.bones

import java.lang.Exception

/**
 * Copyright (C) 2020 Project X
 *
 * @Project ProjectX
 * @author Eudy Contreras.
 * @since April 2020
 */

sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()
    class Loading<T>(val cache: T? = null) : Resource<T>()
    class Failure<T>(val ex: Exception): Resource<T>()
}