package com.haberturm.hitchhikingapp.ui.di

import com.haberturm.hitchhikingapp.ui.nav.MyRouteNavigator
import com.haberturm.hitchhikingapp.ui.nav.RouteNavigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
class ViewModelModule {
    @Provides
    @ViewModelScoped
    fun bindRouteNavigator(): RouteNavigator = MyRouteNavigator()
}

