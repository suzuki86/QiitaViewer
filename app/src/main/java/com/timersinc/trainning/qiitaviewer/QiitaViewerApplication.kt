package com.timersinc.trainning.qiitaviewer

import android.app.Application
import timber.log.Timber

class QiitaViewerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}
