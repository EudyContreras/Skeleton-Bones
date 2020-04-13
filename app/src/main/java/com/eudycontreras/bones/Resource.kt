package com.eudycontreras.bones

/**
 * Copyright (C) 2020 Project X
 *
 * @Project ProjectX
 * @author Eudy Contreras.
 * @since April 2020
 */

sealed class Resource<out T>(
    open val data: T?,
    open val loading: Boolean
) {
    class Success<T>(
        data: T
    ) : Resource<T>(
        data = data,
        loading = false
    )

    class Loading<T>(
        cache: T? = null
    ) : Resource<T>(
        data = cache,
        loading = true
    )
}