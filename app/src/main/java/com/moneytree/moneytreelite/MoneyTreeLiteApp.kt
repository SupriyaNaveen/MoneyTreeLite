package com.moneytree.moneytreelite

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import com.moneytree.moneytreelite.di.DaggerApplicationComponent
import com.moneytree.moneytreelite.di.module.AppModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import timber.log.Timber
import javax.inject.Inject
import android.content.Intent
import com.moneytree.moneytreelite.utility.LoadJsonToDbService

/**
 * Application called once. Initialize service to load json file to db.
 * Set logging system to Timber.
 * Set the Dependency Injections.
 * Fragment is for future modules. Not used in this demo application.
 */
class MoneyTreeLiteApp : Application(), HasActivityInjector, HasSupportFragmentInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate() {
        super.onCreate()

        Timber.uprootAll()
        Timber.plant(Timber.DebugTree())

        DaggerApplicationComponent.builder()
            .appModule(AppModule(this))
            .build().inject(this)

        val serviceIntent = Intent(applicationContext, LoadJsonToDbService::class.java)
        startService(serviceIntent)
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector
    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector
}