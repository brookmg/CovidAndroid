package ethiopia.covid.android.ui.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import com.google.android.material.button.MaterialButton;

import java.util.List;
import java.util.Locale;

import ethiopia.covid.android.R;
import ethiopia.covid.android.util.Utils;

import static ethiopia.covid.android.util.Utils.dpToPx;
import static ethiopia.covid.android.util.Utils.getCurrentTheme;

public class Table extends LinearLayout {

    private TableLayout fixedColumnTableLayout;
    private TableLayout scrollableTableLayout;
    private LinearLayout tableLayoutContainer;
    private MaterialButton nextPageButton;
    private HorizontalScrollView scrollableTableContainer;
    private int currentTheme = getCurrentTheme(getContext());

    private List<List<String>> rowItems;
    private int currentPageLimit = 10;
    private int currentPage = 0;

    private interface ApplyProperty {
        void apply(View item);
    }

    public interface OnTableRowClicked {
        void onClick(String identifier);
    }

    OnTableRowClicked onTableRowClickedCallback;

    public Table(Context context) {
        super(context);
        init();
    }

    public Table(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        fixedColumnTableLayout = new TableLayout(getContext());
        scrollableTableLayout = new TableLayout(getContext());
        nextPageButton = new MaterialButton(new ContextThemeWrapper(getContext() , R.style.Widget_MaterialComponents_Button_OutlinedButton), null, 0);
        tableLayoutContainer = new LinearLayout(getContext());

        scrollableTableContainer = new HorizontalScrollView(getContext()) {
            @Override
            protected void onScrollChanged(int l, int t, int oldl, int oldt) {
                super.onScrollChanged(l, t, oldl, oldt);
                fixedColumnTableLayout.setBackgroundColor(ContextCompat.getColor(getContext(),
                        currentTheme == 0 ? R.color.white_0 : R.color.black_2));
                ViewCompat.setElevation(fixedColumnTableLayout , Math.min(l * 0.4f , 10));
            }
        };

        scrollableTableLayout.setStretchAllColumns(true);
        fixedColumnTableLayout.setStretchAllColumns(false);
        tableLayoutContainer.setOrientation(HORIZONTAL);

        nextPageButton.setText("Next Page");
        nextPageButton.setCornerRadius(10);
        nextPageButton.setTextColor(ContextCompat.getColor(getContext() , R.color.purple_1));
        nextPageButton.setBackgroundTintList(ColorStateList.valueOf(
                Color.TRANSPARENT
        ));
    }

    public void populateTable(List<String> headers, List<List<String>> rowItems,
                              boolean fixedColumn, int fixedColumnCount, int headerTextLengthLimit) {
        populateTable(headers, rowItems, fixedColumn, fixedColumnCount, headerTextLengthLimit,
                true, null, 10, 0);
    }

    public void populateTable(List<String> headers, List<List<String>> rowItems,
                              boolean fixedColumn, int fixedColumnCount, int headerTextLengthLimit,
                              boolean enablePagination, OnClickListener onNextButtonClicked, int pageLimit,
                              int page
    ) {
        clearTable();
        this.rowItems = rowItems;
        this.currentPageLimit = pageLimit;
        this.currentPage = page;

        nextPageButton.setOnClickListener( onNextButtonClicked != null ?
                onNextButtonClicked :
                (enablePagination ?
                        v -> {
                            populateTable(
                                    headers, rowItems, fixedColumn, fixedColumnCount, headerTextLengthLimit,
                                    true, null, pageLimit,
                                    ((page+1) * pageLimit) >= rowItems.size() ? 0 : page+1
                            );
                        } : null)
        );

        if (fixedColumn && fixedColumnCount > 0)
            addRows(fixedColumnTableLayout, null, generateTableRow(0, headers.subList(0 , fixedColumnCount),
                    null, false, headerTextLengthLimit));

        addRows(scrollableTableLayout, null, generateTableRow(0, headers.subList(fixedColumnCount , headers.size()),
                null));

        for (int i = currentPage * currentPageLimit;
             i < (currentPage * currentPageLimit) + currentPageLimit && i < rowItems.size();
             i++) {

            List<String> stat = rowItems.get(i);
            if (fixedColumn && fixedColumnCount > 0)
                addRows(fixedColumnTableLayout, stat.get(0), generateTableRow(1,headers.subList(0 , fixedColumnCount),
                        stat.subList(0 , fixedColumnCount), false, headerTextLengthLimit));

            addRows(scrollableTableLayout, stat.get(0), generateTableRow(1,headers.subList(fixedColumnCount , headers.size()),
                    fixedColumnCount > 0 ? stat.subList(fixedColumnCount , stat.size()) : stat));

        }

        if (fixedColumn && fixedColumnCount > 0) {
            tableLayoutContainer.addView(fixedColumnTableLayout);
            LinearLayout.LayoutParams params = (LayoutParams) fixedColumnTableLayout.getLayoutParams();
            params.width = LayoutParams.WRAP_CONTENT;
            params.height = LayoutParams.MATCH_PARENT;
            fixedColumnTableLayout.setLayoutParams(params);
        }

        scrollableTableContainer.addView(scrollableTableLayout);
        tableLayoutContainer.addView(scrollableTableContainer);

        addView(tableLayoutContainer);
        addView(nextPageButton);

        LinearLayout.LayoutParams params = (LayoutParams) nextPageButton.getLayoutParams();
        params.width = LayoutParams.MATCH_PARENT;
        params.height = LayoutParams.MATCH_PARENT;

        params.leftMargin = dpToPx(getContext() , 10);
        params.rightMargin = dpToPx(getContext() , 10);
        params.topMargin = dpToPx(getContext() , 10);
        params.bottomMargin = dpToPx(getContext() , 10);

        nextPageButton.setLayoutParams(params);
        nextPageButton.setVisibility(
                (!enablePagination && onNextButtonClicked == null) ? GONE : VISIBLE
        );

    }

    public void setOnTableRowClickedCallback(OnTableRowClicked onTableRowClicked) {
        this.onTableRowClickedCallback = onTableRowClicked;
    }

    private void addRows(TableLayout to, String identifier, TableRow ...rows) {
        for (TableRow row : rows) {
            row.setOnClickListener(v -> {
                if (onTableRowClickedCallback != null && identifier != null)
                    onTableRowClickedCallback.onClick(identifier);
            });
            to.addView(row);
        }
    }

    private void addMultipleViews(ViewGroup viewGroup, View... items){
        for (View item : items) viewGroup.addView(item);
    }

    private void applyToAllViews(ApplyProperty property, AppCompatTextView ...onto) {
        for(AppCompatTextView textview: onto) {
            property.apply(textview);
        }
    }

    public void clearTable() {
        tableLayoutContainer.removeAllViews();
        fixedColumnTableLayout.removeAllViews();
        scrollableTableLayout.removeAllViews();
        scrollableTableContainer.removeAllViews();

        removeAllViews();
    }

    private TableRow generateTableRow (int position, List<String> centeredHeaders, List<String> centeredItems){
        return generateTableRow(position, centeredHeaders, centeredItems, true, -1);
    }

    private TableRow generateTableRow (int position, List<String> headers, List<String> items, boolean centered, int maxHeaderLength) {

        if (position == 0) {
            TableRow headerRow = new TableRow(getContext());
            for (String header : headers) {
                AppCompatTextView textView = new AppCompatTextView(getContext());
                applyToAllViews((textview) -> {
                    if (centered) ((AppCompatTextView) textview).setGravity(Gravity.CENTER);
                    ((AppCompatTextView) textview).setTextColor(ContextCompat.getColor(getContext(),
                            currentTheme == 0 ? R.color.black_0 : R.color.white_0));
                    ((AppCompatTextView) textview).setTypeface(null, Typeface.BOLD);
                    textview.setPadding(dpToPx(getContext(), 16) , dpToPx(getContext(), 12),
                            dpToPx(getContext(), 16), dpToPx(getContext(), 12));
                }, textView);

                textView.setText(String.format("%s%s", header.substring(0,
                        maxHeaderLength > 0 && maxHeaderLength < header.length() ?
                                maxHeaderLength : header.length()
                ), maxHeaderLength < 0 || maxHeaderLength > header.length()? "" : "..."));
                headerRow.addView(textView);
            }
            headerRow.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.grey_1x));
            return headerRow;
        } else {
            TableRow itemRow = new TableRow(getContext());

            for (String content : items) {
                AppCompatTextView contentTextView = new AppCompatTextView(getContext());
                contentTextView.setText(String.format("%s%s", content.substring(0,
                        maxHeaderLength > 0 && maxHeaderLength < content.length() ?
                                maxHeaderLength : content.length()
                ), maxHeaderLength < 0 || maxHeaderLength > content.length()? "" : "..."));

                applyToAllViews(
                        textview -> {
                            if (centered) ((AppCompatTextView) textview).setGravity(Gravity.CENTER);
                            ((AppCompatTextView) textview).setTextColor(
                                    currentTheme == 0 ?
                                            ContextCompat.getColor(getContext(), R.color.black_0) :
                                            ContextCompat.getColor(getContext(), R.color.white_0)
                            );
                            textview.setPadding(dpToPx(getContext(), 16), dpToPx(getContext(), 16),
                                    dpToPx(getContext(), 16), dpToPx(getContext(), 16));
                        },
                        contentTextView);

                addMultipleViews(itemRow, contentTextView);
            }
            return itemRow;
        }
    }
}
