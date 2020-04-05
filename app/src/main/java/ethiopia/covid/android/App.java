package ethiopia.covid.android;

import android.app.Application;

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
    }

    public static App getInstance() {
        return instance;
    }

    public API getMainAPI() {
        return mainAPI;
    }
}
