package com.haberturm.hitchhikingapp.data.repositories.home

sealed class HomeRepositoryEvent {
    data class UserLocationStatus(val isDone: Boolean) : HomeRepositoryEvent()
}
