package com.stack.test.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModule {

//    @Binds
//    abstract fun bindRepository(impl: StubRepository): Repository
}
