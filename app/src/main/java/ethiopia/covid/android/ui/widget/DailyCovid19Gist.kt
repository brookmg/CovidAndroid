package ethiopia.covid.android.ui.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import ethiopia.covid.android.App
import ethiopia.covid.android.R
import ethiopia.covid.android.data.Case
import ethiopia.covid.android.network.API
import ethiopia.covid.android.util.Utils
import timber.log.Timber

/**
 * Implementation of App Widget functionality.
 */
class DailyCovid19Gist : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }
}

internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    val countryName = context.getString(R.string.ethiopia)
    val views = RemoteViews(context.packageName,
            if (Utils.getCurrentTheme(context) == 0) R.layout.daily_covid19_gist_light
            else R.layout.daily_covid19_gist_dark
    )

    App.instance.mainAPI.getCases(object: API.OnItemReady<Case?> {
        override fun onItem(item: Case?, err: String?) {
            views.setTextViewText(R.id.country_name, countryName)
            views.setTextViewText(R.id.tested, Utils.formatNumber((item?.tested?.toLong() ?: 0L)))
            views.setTextViewText(R.id.infected, Utils.formatNumber((item?.confirmed?.toLong() ?: 0L)))
            views.setTextViewText(R.id.recovered, Utils.formatNumber((item?.recovered?.toLong() ?: 0L)))
            views.setTextViewText(R.id.death, Utils.formatNumber((item?.deceased?.toLong() ?: 0L)))

            Timber.d("Widget content is loaded: ${item.toString()}")
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    })
}