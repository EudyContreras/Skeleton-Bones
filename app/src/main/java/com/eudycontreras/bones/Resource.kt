package com.eudycontreras.bones

/**
 * Copyright (C) 2020 Project X
 *
 * @Project ProjectX
 * @author Eudy Contreras.
 * @since April 2020
 */

sealed class Resource<out T>(
    open val loading: Boolean
) {
    class Success<T>(
        val data: T
    ) : Resource<T>(
        loading = false
    )

    class Loading<T>(
        val cache: T? = null
    ) : Resource<T>(
        loading = true
    )
}