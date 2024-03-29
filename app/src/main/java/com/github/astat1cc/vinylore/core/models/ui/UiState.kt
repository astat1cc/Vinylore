package com.github.astat1cc.vinylore.core.models.ui

sealed class UiState<T> {

    class Success<T>(val data: T) : UiState<T>()

    class Fail<T>(val message: String) : UiState<T>()

    class Loading<T> : UiState<T>()
}