package com.haberturm.hitchhikingapp.data.repositories

sealed class HomeRepositoryEvent {
    data class UserLocationStatus(val isDone: Boolean) : HomeRepositoryEvent()
}
