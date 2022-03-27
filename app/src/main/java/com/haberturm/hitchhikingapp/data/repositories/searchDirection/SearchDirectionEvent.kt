package com.haberturm.hitchhikingapp.data.repositories.searchDirection

sealed class SearchDirectionEvent {
    data class UserLocationStatus(val isDone: Boolean) : SearchDirectionEvent()
}
