@file:Suppress("unused")

package ethiopia.covid.android.util

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.text.format.DateUtils
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.annotation.Px
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ethiopia.covid.android.R
import ethiopia.covid.android.data.NewsItem
import ethiopia.covid.android.util.Constant.MATERIAL_COLORS
import timber.log.Timber
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import kotlin.experimental.and
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.math.roundToLong

object Utils {
    @JvmStatic
    fun dpToPx(context: Context, dp: Int): Int {
        val displayMetrics = context.resources.displayMetrics
        return (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
    }

    fun pxToDp(context: Context, px: Int): Int {
        val displayMetrics = context.resources.displayMetrics
        return (px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
    }

    /**
     * Function to set margin to a view
     * @param view -> view to be tempered
     * @param l -> left
     * @param t -> top
     * @param r -> right
     * @param b -> bottom
     */
    fun setMargins(view: View, @Px l: Int, @Px t: Int, @Px r: Int, @Px b: Int) {
        if (view.layoutParams is MarginLayoutParams) {
            val params = view.layoutParams as MarginLayoutParams
            params.setMargins(l, t, r, b)
            view.layoutParams = params
            view.requestLayout()
        }
    }

    /**
     * Function to get the time gap between now and the timestamp given
     * @param fromTimeMil - the milli-sec timestamp value to compute gap from
     * @return the time gap: eg "2d ago" , "just now", "43m ago"
     */
    fun getTimeGap(fromTimeMil: Long): String {
        val diff = Date().time - fromTimeMil
        return when {
            diff < DateUtils.MINUTE_IN_MILLIS -> "just now"
            diff < DateUtils.HOUR_IN_MILLIS -> (diff.toFloat() / DateUtils.MINUTE_IN_MILLIS).roundToLong().toString() + "m ago"
            diff < DateUtils.DAY_IN_MILLIS -> (diff.toFloat() / DateUtils.HOUR_IN_MILLIS).roundToLong().toString() + "h ago"
            diff < DateUtils.WEEK_IN_MILLIS -> (diff.toFloat() / DateUtils.DAY_IN_MILLIS).roundToLong().toString() + "d ago"
            diff < DateUtils.YEAR_IN_MILLIS -> (diff.toFloat() / DateUtils.WEEK_IN_MILLIS).roundToLong().toString() + "w ago"

            else -> {
                (diff.toFloat() / DateUtils.YEAR_IN_MILLIS).roundToLong().toString() + "y ago"
            }
        }
    }

    fun run(task: Runnable, times: Int) {
        for (i in 0 until times) {
            task.run()
        }
    }

    fun openPlayStore(activity: Activity?) {
        if (activity == null) return
        val appPackageName = activity.packageName // getPackageName() from Context or Activity object
        try {
            activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
        } catch (anfe: ActivityNotFoundException) {
            activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
        }
    }

    @JvmStatic
    fun callPhoneNumber(context: Context, phone: String) {
        val callIntent = Intent(Intent.ACTION_DIAL)
        callIntent.data = Uri.parse("tel:$phone")
        context.startActivity(callIntent)
    }

    @JvmStatic
    fun formatNumber(number: Long): String {
        val stringify = number.toString()
        val numberOfCommas = floor((stringify.length - 1) / 3f.toDouble()).toInt()
        val returnable = StringBuilder()
        returnable.append(stringify)
        for (i in 1..numberOfCommas) {
            returnable.insert(stringify.length - i * 3, ',')
        }
        return returnable.toString()
    }

    @JvmStatic
    fun generateColors(number: Int): List<Int> {
        val returnable: MutableList<Int> = ArrayList()
        val duplicationControl: HashSet<Int> = hashSetOf()

        var i = 0
        while (i in 0 .. number) {
            val randomIndex = (Random().nextFloat() * MATERIAL_COLORS.size).toInt()
            if (duplicationControl.contains(Color.parseColor(MATERIAL_COLORS[randomIndex]))) continue
            else {
                val color = Color.parseColor(MATERIAL_COLORS[randomIndex]);
                duplicationControl.add(color)
                returnable.add(color)
                i++
            }
        }

        return returnable
    }

    @JvmStatic
    fun readRawFile(ctx: Context, resId: Int): String? {
        val inputStream = ctx.resources.openRawResource(resId)
        val inputStreamReader = InputStreamReader(inputStream)
        val bufferedReader = BufferedReader(inputStreamReader)
        var line: String?
        val text = StringBuilder()
        try {
            while (bufferedReader.readLine().also { line = it } != null) {
                text.append(line)
                text.append('\n')
            }
        } catch (e: IOException) {
            return null
        }
        return text.toString()
    }

    @JvmStatic
    fun getCurrentTheme(context: Context?): Int {
        return getDefaultSharedPreferences(context).getInt(Constant.PREFERENCE_THEME, 0)
    }

    @JvmStatic
    fun setCurrentTheme(context: Context?, theme: Int) {
        getDefaultSharedPreferences(context).edit().putInt(Constant.PREFERENCE_THEME, theme).apply()
    }

    fun getNewsItems(json: String?): List<NewsItem> {
        val typeOfT = object : TypeToken<Collection<NewsItem?>?>() {}.type
        return Gson().fromJson(json, typeOfT)
    }

    @JvmStatic
    fun getImagesForContent(json: String?): List<String> {
        val typeOfT = object : TypeToken<Collection<String?>?>() {}.type
        return Gson().fromJson(json, typeOfT)
    }

    @JvmStatic
    fun openUrlInCustomTab(context: Context, url: String) {
        val customTabsIntent = CustomTabsIntent.Builder()
                .setToolbarColor(ContextCompat.getColor(context, R.color.black_0))
                .enableUrlBarHiding().setShowTitle(true).build()
        customTabsIntent.launchUrl(context, Uri.parse(url.replace(" ", "")))
    }

    fun bindCustomTabsService(context: Context, connection: CustomTabsServiceConnection): Boolean {
        return CustomTabsClient.bindCustomTabsService(context, "com.android.chrome", connection)
    }

    @JvmStatic
    fun md5(string: String): String {
        val hash: ByteArray
        hash = try {
            MessageDigest.getInstance("MD5").digest(string.toByteArray(StandardCharsets.UTF_8))
        } catch (e: NoSuchAlgorithmException) {
            Timber.e(e, "Huh, MD5 should be supported? China is that you?")
            return ""
        }
        val hex = StringBuilder(hash.size * 2)
        for (b in hash) {
            if (b and 0xFF.toByte() < 0x10) {
                hex.append("0")
            }
            hex.append(Integer.toHexString((b and 0xFF.toByte()).toInt()))
        }
        return hex.toString()
    }

    /**
     * Function to return a color in the gradient of two colors at a specific point
     * @param colorRGBFirst -> first color with RGB value containing array of ( Shade of red ! ) [Integer]
     * @param colorRGBsecond -> second color with RGB value containing array of ( Shade of green ! ) [Integer]
     * @param factor -> position of the pointer ... 0 is color1 and 1 is color2
     * @return an integer array containing RGB value of the color found
     */
    @JvmStatic
    fun calculateTheDelta(colorRGBFirst: IntArray, colorRGBsecond: IntArray, factor: Float): IntArray {
        val rgbBaby = intArrayOf(
                0, 0, 0
        )
        val slope = (colorRGBsecond[1] - colorRGBFirst[1]) / (1f - 0.5f)
        val slope2 = (colorRGBFirst[0] - colorRGBsecond[0]) / (1f - 0.5f)
        if (factor < 0.5f) {
            //only touch green
            rgbBaby[0] = colorRGBFirst[0]
            rgbBaby[1] = (slope * factor).toInt()
        } else {
            //only touch red
            rgbBaby[0] = (-1 * slope2 * (factor - 1f)).toInt()
            rgbBaby[1] = colorRGBsecond[1]
        }
        rgbBaby[2] = colorRGBFirst[2] + ((colorRGBsecond[2] - colorRGBFirst[2]) * factor).toInt()
        return rgbBaby
    }
}