package ethiopia.covid.android.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Px;
import androidx.browser.customtabs.CustomTabsClient;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.customtabs.CustomTabsServiceConnection;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;

import ethiopia.covid.android.R;
import ethiopia.covid.android.data.NewsItem;
import timber.log.Timber;

public class Utils {

    @SuppressWarnings("SameParameterValue")
    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @SuppressWarnings("unused")
    public static int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    /**
     * Function to set margin to a view
     * @param view -> view to be tempered
     * @param l -> left
     * @param t -> top
     * @param r -> right
     * @param b -> bottom
     */
    public static void setMargins(View view, @Px int l, @Px int t, @Px int r, @Px int b) {
        if (view.getLayoutParams() instanceof LinearLayout.MarginLayoutParams) {
            LinearLayout.MarginLayoutParams params = (LinearLayout.MarginLayoutParams) view.getLayoutParams();
            params.setMargins(l, t, r, b);
            view.setLayoutParams(params);
            view.requestLayout();
        }
    }

    /**
     * Function to get the time gap between now and the timestamp given
     * @param fromTimeMil - the milli-sec timestamp value to compute gap from
     * @return the time gap: eg "2d ago" , "just now", "43m ago"
     */
    public static String getTimeGap(long fromTimeMil) {

        long diff = (new Date().getTime()) - fromTimeMil;

        if (diff < DateUtils.MINUTE_IN_MILLIS)
        { return "just now"; }
        else if (diff < DateUtils.HOUR_IN_MILLIS)
        { return Math.round((float) diff/DateUtils.MINUTE_IN_MILLIS) + "m ago"; }
        else if (diff < DateUtils.DAY_IN_MILLIS)
        { return Math.round((float) diff/DateUtils.HOUR_IN_MILLIS) + "h ago"; }
        else if (diff < DateUtils.WEEK_IN_MILLIS)
        { return Math.round((float) diff/DateUtils.DAY_IN_MILLIS) + "d ago"; }
        else if (diff < DateUtils.YEAR_IN_MILLIS)
        { return Math.round((float) diff/DateUtils.WEEK_IN_MILLIS) + "w ago"; }
        else
        { return Math.round((float) diff/DateUtils.YEAR_IN_MILLIS) + "y ago"; }

    }

    public static void run(Runnable task, int times) {
        for (int i = 0; i < times; i++) {
            task.run();
        }
    }

    public static void openPlayStore(Activity activity) {
        if (activity == null) return;
        final String appPackageName = activity.getPackageName(); // getPackageName() from Context or Activity object
        try {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public static void callPhoneNumber(Context context, String phone) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + phone));
        context.startActivity(callIntent);
    }

    public static String formatNumber(long number) {
        String stringify = String.valueOf(number);
        int numberOfCommas = ((int) Math.floor((stringify.length() - 1) / 3f));

        StringBuilder returnable = new StringBuilder();
        returnable.append(stringify);

        for (int i = 1; i <= numberOfCommas; i++) {
            returnable.insert( stringify.length() - ( i * 3 ), ',');
        }

        return returnable.toString();
    }

    public static List<Integer> generateColors(int number) {
        List<Integer> returnable = new ArrayList<>();
        float[] hsl = { 0 , 0.7f , 0.5f };

        for (int i = 0; i < number; i++) {
            hsl[0] = new Random().nextFloat() * 360f;
            if (hsl[1] < 0.2) hsl[1] -= 0.1f; else hsl[1] = 0.7f;
            returnable.add(ColorUtils.HSLToColor(hsl));
        }

        return returnable;
    }

    public static String readRawFile(Context ctx, int resId) {
        InputStream inputStream = ctx.getResources().openRawResource(resId);

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line;
        StringBuilder text = new StringBuilder();

        try {
            while (( line = bufferedReader.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        } catch (IOException e) {
            return null;
        }

        return text.toString();
    }

    public static int getCurrentTheme(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(Constant.PREFERENCE_THEME , 0);
    }

    public static void setCurrentTheme(Context context, int theme) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(Constant.PREFERENCE_THEME , theme).apply();
    }

    public static List<NewsItem> getNewsItems(String json) {
        Type typeOfT = new TypeToken<Collection<NewsItem>>(){}.getType();
        return new Gson().fromJson(json , typeOfT);
    }

    public static List<String> getImagesForContent(String json) {
        Type typeOfT = new TypeToken<Collection<String>>(){}.getType();
        return new Gson().fromJson(json , typeOfT);
    }

    public static void openUrlInCustomTab(Context context, String url) {
        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                .setToolbarColor(ContextCompat.getColor(context, R.color.black_0))
                .enableUrlBarHiding().setShowTitle(true).build();
        customTabsIntent.launchUrl(context, Uri.parse(url.replace(" " , "")));
    }

    public static boolean bindCustomTabsService(Context context, CustomTabsServiceConnection connection) {
        return CustomTabsClient.bindCustomTabsService(context, "com.android.chrome", connection);
    }

    public static String md5(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            Timber.e(e, "Huh, MD5 should be supported? China is that you?");
            return "";
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

}
