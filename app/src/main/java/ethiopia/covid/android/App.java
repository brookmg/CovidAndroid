package ethiopia.covid.android;

import android.app.Application;
import android.content.res.Configuration;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.franmontiel.localechanger.LocaleChanger;

import java.util.Arrays;
import java.util.Locale;

import ethiopia.covid.android.network.API;
import timber.log.Timber;

/**
 * Created by BrookMG on 3/25/2020 in ethiopia.covid.android
 * inside the project CoVidEt .
 */
public class App extends MultiDexApplication {

    private API mainAPI;
    private static App instance;

    public class DebugLogTree extends Timber.DebugTree {

        @Override
        protected void log(int priority, String tag, @NonNull String message, Throwable t) {
            // Workaround for devices that doesn't show lower priority logs
            if (Build.MANUFACTURER.equals("HUAWEI") || Build.MANUFACTURER.equals("samsung")) {
                if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO)
                    priority = Log.ERROR;
            }
            super.log(priority, tag, message, t);
        }

        @Override
        protected String createStackElementTag(@NonNull StackTraceElement element) {
            // Add log statements line number to the log
            return super.createStackElementTag(element) + " - " + element.getLineNumber();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);

        instance = this;
        mainAPI = new API();

        if(BuildConfig.DEBUG){
            Timber.plant(new DebugLogTree());
        }

        LocaleChanger.initialize(
                getApplicationContext(),
                Arrays.asList(
                        new Locale("en" , "us"),
                        new Locale("am", "et"),
                        new Locale("om", "et"),
                        new Locale("ti", "et")
                )
        );
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        try { LocaleChanger.onConfigurationChanged(); }
        catch (Exception e) { e.printStackTrace(); }
    }

    public static App getInstance() {
        return instance;
    }

    public API getMainAPI() {
        return mainAPI;
    }
}
