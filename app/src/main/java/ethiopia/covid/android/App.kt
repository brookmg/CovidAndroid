package ethiopia.covid.android

import android.content.res.Configuration
import android.os.Build
import android.util.Log
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.franmontiel.localechanger.LocaleChanger
import ethiopia.covid.android.network.API
import timber.log.Timber
import timber.log.Timber.DebugTree
import java.util.*

/**
 * Created by BrookMG on 3/25/2020 in ethiopia.covid.android
 * inside the project CoVidEt .
 */
class App : MultiDexApplication() {
    lateinit var mainAPI: API

    inner class DebugLogTree : DebugTree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            // Workaround for devices that doesn't show lower priority logs
            var mutablePriority = priority
            if (Build.MANUFACTURER == "HUAWEI" || Build.MANUFACTURER == "samsung") {
                if (mutablePriority == Log.VERBOSE || mutablePriority == Log.DEBUG || mutablePriority == Log.INFO)
                    mutablePriority = Log.ERROR
            }
            super.log(mutablePriority, tag, message, t)
        }

        override fun createStackElementTag(element: StackTraceElement): String? {
            // Add log statements line number to the log
            return super.createStackElementTag(element) + " - " + element.lineNumber
        }
    }

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        instance = this
        mainAPI = API()

        if (BuildConfig.DEBUG) Timber.plant(DebugLogTree())

        LocaleChanger.initialize(
                applicationContext,
                listOf(
                        Locale("en", "us"),
                        Locale("am", "et"),
                        Locale("om", "et"),
                        Locale("ti", "et")
                )
        )
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        try {
            LocaleChanger.onConfigurationChanged()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        lateinit var instance: App
    }
}