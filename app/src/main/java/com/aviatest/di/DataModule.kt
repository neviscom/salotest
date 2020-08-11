package com.aviatest.di

import com.aviatest.data.BaseServiceFactory
import com.aviatest.data.ServiceFactory
import com.aviatest.data.search.CityRepositoryImpl
import com.aviatest.domain.CityRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun provideCityRepository(impl: CityRepositoryImpl): CityRepository

    @Binds
    @Singleton
    abstract fun provideServiceFactory(impl: BaseServiceFactory): ServiceFactory
}