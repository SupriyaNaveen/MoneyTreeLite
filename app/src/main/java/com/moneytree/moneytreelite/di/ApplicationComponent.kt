package com.moneytree.moneytreelite.di

import com.moneytree.moneytreelite.MoneyTreeLiteApp
import com.moneytree.moneytreelite.di.module.AppModule
import com.moneytree.moneytreelite.di.module.BuildersViewModule
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Component, specifies who is going to be able to manually inject it:
 */
@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class,
        BuildersViewModule::class, AppModule::class]
)

interface ApplicationComponent {
    fun inject(app: MoneyTreeLiteApp)
}