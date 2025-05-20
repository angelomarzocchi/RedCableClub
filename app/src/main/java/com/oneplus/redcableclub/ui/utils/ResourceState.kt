package com.oneplus.redcableclub.ui.utils

sealed interface ResourceState<out T> {
    data object Loading : ResourceState<Nothing>
    data class Success<T>(val data: T) : ResourceState<T>
    data class Error(val message: String? = null, val throwable: Throwable? = null) : ResourceState<Nothing>
   // data object Idle : ResourceState<Nothing> // If data isn't loaded immediately
}



