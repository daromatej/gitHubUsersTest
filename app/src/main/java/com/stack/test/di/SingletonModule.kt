package com.stack.test.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SingletonModule {

//    @Binds
//    abstract fun bindRepository(impl: StubRepository): Repository
}
