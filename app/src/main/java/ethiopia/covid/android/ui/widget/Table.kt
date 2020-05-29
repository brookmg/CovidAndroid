package ethiopia.covid.android.ui.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.google.android.material.button.MaterialButton
import ethiopia.covid.android.R
import ethiopia.covid.android.util.Utils.dpToPx
import ethiopia.covid.android.util.Utils.getCurrentTheme

@Suppress("unused")
class Table : LinearLayout {
    private lateinit var fixedColumnTableLayout: TableLayout

    private var scrollableTableLayout: TableLayout? = null
    private var tableLayoutContainer: LinearLayout? = null
    private var nextPageButton: MaterialButton? = null
    private var scrollableTableContainer: HorizontalScrollView? = null
    private val currentTheme = getCurrentTheme(context)
    private var rowItems: List<List<String>>? = null
    private var currentPageLimit = 10
    private var currentPage = 0

    private interface ApplyProperty {
        fun apply(item: View)
    }

    interface OnTableRowClicked {
        fun onClick(identifier: String)
    }

    private var onTableRowClickedCallback: OnTableRowClicked? = null

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    private fun init() {
        orientation = VERTICAL
        fixedColumnTableLayout = TableLayout(context)
        scrollableTableLayout = TableLayout(context)
        nextPageButton = MaterialButton(ContextThemeWrapper(context, R.style.Widget_MaterialComponents_Button_OutlinedButton), null, 0)
        tableLayoutContainer = LinearLayout(context)
        scrollableTableContainer = object : HorizontalScrollView(context) {
            override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
                super.onScrollChanged(l, t, oldl, oldt)
                fixedColumnTableLayout.setBackgroundColor(ContextCompat.getColor(context,
                        if (currentTheme == 0) R.color.white_0 else R.color.black_2))
                ViewCompat.setElevation(fixedColumnTableLayout , (l * 0.4f).coerceAtMost(10f))
            }
        }
        scrollableTableLayout?.isStretchAllColumns = true
        fixedColumnTableLayout.isStretchAllColumns = false
        tableLayoutContainer?.orientation = HORIZONTAL
        nextPageButton?.text = context.getString(R.string.next_page)
        nextPageButton?.cornerRadius = 10
        nextPageButton?.setTextColor(ContextCompat.getColor(context, R.color.purple_1))
        nextPageButton?.backgroundTintList = ColorStateList.valueOf(
                Color.TRANSPARENT
        )
    }

    @JvmOverloads
    fun populateTable(headers: List<String>, rowItems: List<List<String>>,
                      fixedColumn: Boolean, fixedColumnCount: Int, headerTextLengthLimit: Int,
                      enablePagination: Boolean =
                              true, onNextButtonClicked: OnClickListener? = null, pageLimit: Int = 10,
                      page: Int = 0
    ) {
        // if there is already content present in the table and the table was not cleared before calling this method
        // then do not re-populate the table.
        if (fixedColumnTableLayout.parent != null
                && scrollableTableLayout?.parent != null) return
        this.rowItems = rowItems
        currentPageLimit = pageLimit
        currentPage = page

        nextPageButton?.setOnClickListener(onNextButtonClicked
                ?: if (enablePagination) OnClickListener {
                    clearTable()
                    populateTable(
                            headers, rowItems, fixedColumn, fixedColumnCount, headerTextLengthLimit,
                            true, null, pageLimit,
                            if ((page + 1) * pageLimit >= rowItems.size) 0 else page + 1
                    )
                } else null
        )

        if (fixedColumn && fixedColumnCount > 0) addRows(fixedColumnTableLayout, null,
                generateTableRow(0, headers.subList(0, fixedColumnCount),
                null, false, headerTextLengthLimit))
        addRows(scrollableTableLayout, null,
                generateTableRow(0, headers.subList(fixedColumnCount, headers.size),
                        null))

        var i = currentPage * currentPageLimit
        while (i < currentPage * currentPageLimit + currentPageLimit && i < rowItems.size) {
            val stat = rowItems[i]
            if (fixedColumn && fixedColumnCount > 0) addRows(fixedColumnTableLayout, stat[0],
                    generateTableRow(1, headers.subList(0, fixedColumnCount),
                    stat.subList(0, fixedColumnCount), false, headerTextLengthLimit))
            addRows(scrollableTableLayout, stat[0], generateTableRow(1, headers.subList(fixedColumnCount, headers.size),
                    if (fixedColumnCount > 0) stat.subList(fixedColumnCount, stat.size) else stat))
            i++
        }
        if (fixedColumn && fixedColumnCount > 0) {
            tableLayoutContainer?.addView(fixedColumnTableLayout)
            val params = fixedColumnTableLayout.layoutParams as LayoutParams
            params.width = LayoutParams.WRAP_CONTENT
            params.height = LayoutParams.MATCH_PARENT
            fixedColumnTableLayout.layoutParams = params
        }
        scrollableTableContainer?.addView(scrollableTableLayout)
        tableLayoutContainer?.addView(scrollableTableContainer)
        
        addView(tableLayoutContainer)
        addView(nextPageButton)

        val params = (nextPageButton?.layoutParams as LayoutParams).apply { 
            width = LayoutParams.MATCH_PARENT
            height = LayoutParams.MATCH_PARENT
            leftMargin = dpToPx(context, 10)
            rightMargin = dpToPx(context, 10)
            topMargin = dpToPx(context, 10)
            bottomMargin = dpToPx(context, 10)
        }
        
        nextPageButton?.layoutParams = params
        nextPageButton?.visibility = 
                if (!enablePagination && onNextButtonClicked == null) 
                    View.GONE else View.VISIBLE
    }

    fun setOnTableRowClickedCallback(onTableRowClicked: OnTableRowClicked?) {
        onTableRowClickedCallback = onTableRowClicked
    }

    private fun addRows(to: TableLayout?, identifier: String?, vararg rows: TableRow) {
        for (row in rows) {
            row.setOnClickListener {
                if (onTableRowClickedCallback != null && identifier != null) onTableRowClickedCallback?.onClick(identifier)
            }
            to?.addView(row)
        }
    }

    private fun addMultipleViews(viewGroup: ViewGroup, vararg items: View) {
        for (item in items) viewGroup.addView(item)
    }

    private fun applyToAllViews(property: ApplyProperty, vararg onto: AppCompatTextView) {
        for (textview in onto) {
            property.apply(textview)
        }
    }

    fun clearTable() {
        tableLayoutContainer?.removeAllViews()
        fixedColumnTableLayout.removeAllViews()
        scrollableTableLayout?.removeAllViews()
        scrollableTableContainer?.removeAllViews()
        removeAllViews()
    }

    private fun generateTableRow(position: Int, centeredHeaders: List<String>, centeredItems: List<String>?): TableRow {
        return generateTableRow(position, centeredHeaders, centeredItems, true, -1)
    }

    private fun generateTableRow(position: Int, headers: List<String>,
                                 items: List<String>?, centered: Boolean,
                                 maxHeaderLength: Int): TableRow {

        return if (position == 0) {
            val headerRow = TableRow(context)

            for (header in headers) {
                val textView = AppCompatTextView(context)
                applyToAllViews(object : ApplyProperty {
                    override fun apply(item: View) {
                        if (centered) (item as AppCompatTextView).gravity = Gravity.CENTER
                        (item as AppCompatTextView).setTextColor(ContextCompat.getColor(context,
                                if (currentTheme == 0) R.color.black_0 else R.color.white_0))
                        item.setTypeface(null, Typeface.BOLD)
                        item.setPadding(dpToPx(context, 16), dpToPx(context, 12),
                                dpToPx(context, 16), dpToPx(context, 12))
                    }
                }, textView)

                textView.text = String.format("%s%s", header.substring(0,
                        if (maxHeaderLength > 0 && maxHeaderLength < header.length) maxHeaderLength else header.length
                ), if (maxHeaderLength < 0 || maxHeaderLength > header.length) "" else "...")

                headerRow.addView(textView)
            }
            headerRow.setBackgroundColor(ContextCompat.getColor(context, R.color.grey_1x))
            headerRow
        } else {
            val itemRow = TableRow(context)

            for (content in items ?: listOf()) {
                val contentTextView = AppCompatTextView(context)
                contentTextView.text = String.format("%s%s", content.substring(0,
                    if (maxHeaderLength > 0 && maxHeaderLength < content.length) maxHeaderLength else content.length
                ), if (maxHeaderLength < 0 || maxHeaderLength > content.length) "" else "...")

                applyToAllViews(
                    object : ApplyProperty {
                        override fun apply(item: View) {
                            if (centered) (item as AppCompatTextView).gravity = Gravity.CENTER
                            (item as AppCompatTextView).setTextColor(
                                    if (currentTheme == 0) ContextCompat.getColor(context, R.color.black_0)
                                    else ContextCompat.getColor(context, R.color.white_0)
                            )
                            item.setPadding(dpToPx(context, 16), dpToPx(context, 16),
                                    dpToPx(context, 16), dpToPx(context, 16))
                        }
                    },
                    contentTextView
                )
                addMultipleViews(itemRow, contentTextView)
            }
            itemRow
        }
    }
}