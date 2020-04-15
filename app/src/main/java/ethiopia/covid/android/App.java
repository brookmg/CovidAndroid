package ethiopia.covid.android;

import android.app.Application;
import android.content.res.Configuration;

import androidx.annotation.NonNull;

import com.franmontiel.localechanger.LocaleChanger;

import java.util.Arrays;
import java.util.Locale;

import ethiopia.covid.android.network.API;

/**
 * Created by BrookMG on 3/25/2020 in ethiopia.covid.android
 * inside the project CoVidEt .
 */
public class App extends Application {

    private API mainAPI;
    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mainAPI = new API();

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
