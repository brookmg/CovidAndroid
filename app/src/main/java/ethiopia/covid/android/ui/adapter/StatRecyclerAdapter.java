package ethiopia.covid.android.ui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import ethiopia.covid.android.R;
import ethiopia.covid.android.data.CovidStatItem;
import ethiopia.covid.android.data.LineChartItem;
import ethiopia.covid.android.data.PatientItem;
import ethiopia.covid.android.data.StatRecyclerItem;
import ethiopia.covid.android.ui.widget.Table;
import ethiopia.covid.android.util.Constant;
import ethiopia.covid.android.util.Utils;

import static ethiopia.covid.android.util.Utils.formatNumber;

/**
 * Created by BrookMG on 3/24/2020 in ethiopia.covid.android.ui.adapter
 * inside the project CoVidEt .
 */
public class StatRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int HEADER = 0;
    private final int STAT_TABLE = 1;
    private final int PIE_CHART = 2;
    private final int STATUS_CARD = 3;
    private final int PATIENT_TABLE = 4;
    private final int LINE_CHART = 5;
    private final int BUTTON_CARD = 6;

    private List<StatRecyclerItem> statRecyclerItemList;

    public StatRecyclerAdapter(List<StatRecyclerItem> statRecyclerItemList) {
        this.statRecyclerItemList = statRecyclerItemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case HEADER:
                return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.blank_header, parent, false));
            case STAT_TABLE:
                return new CovidStatisticTableViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.stat_table_recycler_element, parent, false)
                );
            case STATUS_CARD:
                return new StatusCardViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.status_card_recycler_element, parent, false)
                );
            case PIE_CHART:
                return new CovidStatisticPieViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.stat_table_pie_element, parent, false)
                );
            case LINE_CHART:
                return new CovidStatisticLineGraphViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.stat_table_graph_element, parent, false)
                );
            case BUTTON_CARD:
                return new ButtonCardViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.card_recycler_element, parent, false)
                );
        }

        return new CovidStatisticTableViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.stat_table_recycler_element, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == STAT_TABLE) {
            position -= 1;
            ((CovidStatisticTableViewHolder) holder).mainCardView.setContentPadding(0,0,0,0);

            List<List<String>> rowItems = new ArrayList<>();
            for (CovidStatItem item : statRecyclerItemList.get(position).getTableItems()) {
                rowItems.add(Arrays.asList(
                        String.valueOf(item.getIdentifier()),
                        formatNumber(item.getInfected()),
                        formatNumber(item.getActive()),
                        formatNumber(item.getDeath()),
                        formatNumber(item.getRecovered()),
                        formatNumber(item.getCritical()),
                        String.valueOf(item.getCasePMillion()),
                        String.valueOf(item.getDeathsPMillion())
                ));
            }

            ((CovidStatisticTableViewHolder) holder).mainTable.populateTable(
                    statRecyclerItemList.get(position).getHeaders(),
                    rowItems,
                    statRecyclerItemList.get(position).getFixedHeaderCount() > 0,
                    statRecyclerItemList.get(position).getFixedHeaderCount(),
                    8
            );

        }
        else if (getItemViewType(position) == STATUS_CARD) {
            position -= 1;
            ((StatusCardViewHolder) holder).country.setText(statRecyclerItemList.get(position).getCountry());

            ((StatusCardViewHolder) holder).tested.setText(String.format(Locale.US, "%d",
                    statRecyclerItemList.get(position).getTotalTested()));

            ((StatusCardViewHolder) holder).infected.setText(String.format(Locale.US, "%d",
                    statRecyclerItemList.get(position).getTotalInfected()));

            ((StatusCardViewHolder) holder).recovered.setText(String.format(Locale.US, "%d",
                    statRecyclerItemList.get(position).getTotalRecovered()));

            ((StatusCardViewHolder) holder).death.setText(String.format(Locale.US, "%d",
                    statRecyclerItemList.get(position).getTotalDeath()));

        }
        else if (getItemViewType(position) == PATIENT_TABLE) {
            position -= 1;
            ((CovidStatisticTableViewHolder) holder).mainCardView.setContentPadding(0,0,0,0);

            List<List<String>> rowItems = new ArrayList<>();
            for (PatientItem item : statRecyclerItemList.get(position).getPatientItems()) {
                rowItems.add(Arrays.asList(
                        String.valueOf(item.getId()),
                        String.valueOf(item.getName()),
                        String.valueOf(item.getLocation()),
                        String.valueOf(item.getAge()),
                        String.valueOf(item.getGender()),
                        String.valueOf(item.getPatient_nationality()),
                        String.valueOf(item.getRecent_travel_to()),
                        Constant.statusIdentifierMap.get(item.getStatus())
                ));
            }

            ((CovidStatisticTableViewHolder) holder).mainTable.populateTable(
                    statRecyclerItemList.get(position).getHeaders(),
                    rowItems,
                    statRecyclerItemList.get(position).getFixedHeaderCount() > 0,
                    statRecyclerItemList.get(position).getFixedHeaderCount(), 8
            );

        }
        else if (getItemViewType(position) == PIE_CHART) {
            position -= 1;

            ((CovidStatisticPieViewHolder) holder).mainCardView.setContentPadding(0,0,0,0);
            ((CovidStatisticPieViewHolder) holder).pieCardTitle.setText(statRecyclerItemList.get(position).getPieCardTitle());
            ArrayList<PieEntry> entries = new ArrayList<>();

            ((CovidStatisticPieViewHolder) holder).mainPieChart.setUsePercentValues(true);
            ((CovidStatisticPieViewHolder) holder).mainPieChart.getDescription().setEnabled(false);
            ((CovidStatisticPieViewHolder) holder).mainPieChart.setExtraOffsets(5, 10, 5, 5);

            ((CovidStatisticPieViewHolder) holder).mainPieChart.setDragDecelerationFrictionCoef(0.95f);
            ((CovidStatisticPieViewHolder) holder).mainPieChart.setDrawHoleEnabled(true);

            ((CovidStatisticPieViewHolder) holder).mainPieChart.setHoleColor(
                    Utils.getCurrentTheme(holder.itemView.getContext()) == 0 ? Color.WHITE :
                            ContextCompat.getColor(holder.itemView.getContext() , R.color.black_2)
            );

            ((CovidStatisticPieViewHolder) holder).mainPieChart.setTransparentCircleAlpha(110);

            ((CovidStatisticPieViewHolder) holder).mainPieChart.setHoleRadius(40f);
            ((CovidStatisticPieViewHolder) holder).mainPieChart.setTransparentCircleRadius(44f);

            ((CovidStatisticPieViewHolder) holder).mainPieChart.setDrawCenterText(true);

            ((CovidStatisticPieViewHolder) holder).mainPieChart.setRotationAngle(0);
            // enable rotation of the chart by touch
            ((CovidStatisticPieViewHolder) holder).mainPieChart.setRotationEnabled(true);
            ((CovidStatisticPieViewHolder) holder).mainPieChart.setHighlightPerTapEnabled(true);
            ((CovidStatisticPieViewHolder) holder).mainPieChart.animateY(400, Easing.EaseInOutQuad);

            Legend l = ((CovidStatisticPieViewHolder) holder).mainPieChart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            l.setOrientation(Legend.LegendOrientation.VERTICAL);
            l.setTextColor(Utils.getCurrentTheme(holder.itemView.getContext()) == 0 ? Color.BLACK : Color.WHITE);
            l.setDrawInside(false);
            l.setXEntrySpace(7f);
            l.setYEntrySpace(0f);
            l.setYOffset(0f);

            // entry label styling
            ((CovidStatisticPieViewHolder) holder).mainPieChart.setEntryLabelColor(Color.WHITE);
            ((CovidStatisticPieViewHolder) holder).mainPieChart.setDrawEntryLabels(false);

            ((CovidStatisticPieViewHolder) holder).mainPieChart.setNoDataTextColor(
                    Utils.getCurrentTheme(holder.itemView.getContext()) == 0 ?
                            Color.BLACK : Color.WHITE
            );
            ((CovidStatisticPieViewHolder) holder).mainPieChart.setEntryLabelTextSize(12f);

            for (int i = 0; i < statRecyclerItemList.get(position).getPieValues().size(); i++) {
                entries.add(new PieEntry(
                        statRecyclerItemList.get(position).getPieValues().get(i),
                        statRecyclerItemList.get(position).getPieLabels().get(i)
                ));
            }

            PieDataSet mainDataSet = new PieDataSet(entries, "");
            mainDataSet.setDrawIcons(false);
            mainDataSet.setSliceSpace(1f);
            mainDataSet.setIconsOffset(new MPPointF(0, 40));
            mainDataSet.setSelectionShift(5f);

            mainDataSet.setColors(statRecyclerItemList.get(position).getPieColors());

            PieData data = new PieData(mainDataSet);
            data.setValueFormatter(new PercentFormatter(
                    ((CovidStatisticPieViewHolder) holder).mainPieChart
            ));

            data.setValueTextSize(11f);
            data.setValueTextColor(Color.WHITE);

            ((CovidStatisticPieViewHolder) holder).mainPieChart.setData(data);
            ((CovidStatisticPieViewHolder) holder).mainPieChart.highlightValues(null);

            ((CovidStatisticPieViewHolder) holder).mainPieChart.invalidate();
        }
        else if (getItemViewType(position) == LINE_CHART) {
            position -= 1;

            ((CovidStatisticLineGraphViewHolder) holder).mainCardView.setContentPadding(0,0,0,0);
            LineChart chart = ((CovidStatisticLineGraphViewHolder) holder).mainLineChart;
            ((CovidStatisticLineGraphViewHolder) holder).lineCartCardTitle.setText(statRecyclerItemList.get(position).getLineCardTitle());
            //chart.setBackgroundColor(Color.WHITE);

            chart.getDescription().setEnabled(false);

            chart.setTouchEnabled(true);
            chart.setDrawGridBackground(false);

            chart.setDragEnabled(true);
            chart.setScaleEnabled(true);

            // force pinch zoom along both axis
            chart.setPinchZoom(true);

            chart.getAxisRight().setEnabled(false);
            chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            chart.getXAxis().setTextColor(Utils.getCurrentTheme(holder.itemView.getContext()) == 0 ? Color.BLACK : Color.WHITE);
            chart.getXAxis().setLabelRotationAngle(90f);

            {
                int finalPosition = position;
                chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        return statRecyclerItemList.get(finalPosition).getLineLabels().get((int) value);
                    }
                });
            }

            chart.getAxisLeft().setTextColor(Utils.getCurrentTheme(holder.itemView.getContext()) == 0 ? Color.BLACK : Color.WHITE);

            // draw points over time
            chart.animateX(300);

            // get the legend (only possible after setting data)
            Legend l = chart.getLegend();

            // draw legend entries as lines
            l.setForm(Legend.LegendForm.LINE);
            l.setTextColor(Utils.getCurrentTheme(holder.itemView.getContext()) == 0 ? Color.BLACK : Color.WHITE);

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();

            for (LineChartItem lineChartItem : statRecyclerItemList.get(position).getLineChartItems()) {
                LineDataSet dataSet;

                ArrayList<Entry> values = new ArrayList<>();

                int i = 0;
                for (Integer item : lineChartItem.getValues()) {
                    values.add(new Entry(i, item));
                    i++;
                }

                if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
                    dataSet = (LineDataSet) chart.getData().getDataSetByIndex(0);
                    dataSet.setValues(values);
                    dataSet.notifyDataSetChanged();
                    chart.getData().notifyDataChanged();
                    chart.notifyDataSetChanged();
                } else {

                    // create a dataset, which should belong to a country
                    dataSet = new LineDataSet(values, lineChartItem.getChartLabel());
                    dataSet.setDrawIcons(false);

                    dataSet.setColor(lineChartItem.getLineColor());
                    dataSet.setValueTextColor(Utils.getCurrentTheme(holder.itemView.getContext()) == 0 ? Color.BLACK : Color.WHITE);
                    dataSet.setCircleColor(lineChartItem.getCircleColor());

                    // line thickness and point size
                    dataSet.setLineWidth(1f);
                    dataSet.setCircleRadius(3f);

                    // draw points as solid circles
                    dataSet.setDrawCircleHole(false);

                    // customize legend entry
                    dataSet.setFormLineWidth(1f);
                    dataSet.setFormSize(15.f);

                    // text size of values
                    dataSet.setValueTextSize(9f);

                    // draw selection line as dashed
                    dataSet.setHighLightColor(ContextCompat.getColor(holder.itemView.getContext() , R.color.purple_0));
                    dataSet.enableDashedHighlightLine(10f, 5f, 0f);

                    // set the filled area
                    dataSet.setDrawFilled(false);

                    dataSets.add(dataSet); // add the data sets
                }
            }

            // create a data object with the data sets
            LineData data = new LineData(dataSets);
            data.setDrawValues(false);
            data.setValueTextColor(Utils.getCurrentTheme(holder.itemView.getContext()) == 0 ? Color.BLACK : Color.WHITE);

            // set data
            chart.setData(data);
        }
        else if (getItemViewType(position) == BUTTON_CARD) {
            position -= 1;
            ((ButtonCardViewHolder) holder).mainText.setText(statRecyclerItemList.get(position).getButtonCardText());
            ((ButtonCardViewHolder) holder).mainButton.setOnClickListener(statRecyclerItemList.get(position).getButtonOnClickListener());
            ((ButtonCardViewHolder) holder).mainButton.setText(statRecyclerItemList.get(position).getButtonText());
        }

        else {
//            ViewGroup.LayoutParams params = ((HeaderViewHolder) holder).blankView.getLayoutParams();
//            params.height = dpToPx(holder.itemView.getContext(), 116);
//            ((HeaderViewHolder) holder).blankView.setLayoutParams(params);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return HEADER;
        else {
            switch (statRecyclerItemList.get(position -1).getType()) {
                case 0: return STAT_TABLE;
                case 1: return PIE_CHART;
                case 2: return STATUS_CARD;
                case 3: return PATIENT_TABLE;
                case 4: return LINE_CHART;
                case 5: return BUTTON_CARD;
                default: return HEADER;
            }
        }
    }

    @Override
    public int getItemCount() {
        return statRecyclerItemList.size()+1;
    }

    private static class CovidStatisticLineGraphViewHolder extends RecyclerView.ViewHolder {
        LineChart mainLineChart;
        MaterialCardView mainCardView;
        AppCompatTextView lineCartCardTitle;

        CovidStatisticLineGraphViewHolder(@NonNull View itemView) {
            super(itemView);
            mainCardView = itemView.findViewById(R.id.main_card_view);
            mainLineChart = itemView.findViewById(R.id.line_chart);
            lineCartCardTitle = itemView.findViewById(R.id.line_title);
        }
    }

    private static class CovidStatisticPieViewHolder extends RecyclerView.ViewHolder {
        PieChart mainPieChart;
        MaterialCardView mainCardView;
        AppCompatTextView pieCardTitle;

        CovidStatisticPieViewHolder(@NonNull View itemView) {
            super(itemView);
            mainCardView = itemView.findViewById(R.id.main_card_view);
            mainPieChart = itemView.findViewById(R.id.pie_chart);
            pieCardTitle = itemView.findViewById(R.id.pie_title);
        }
    }

    private static class CovidStatisticTableViewHolder extends RecyclerView.ViewHolder {
        Table mainTable;
        MaterialCardView mainCardView;

        CovidStatisticTableViewHolder(@NonNull View itemView) {
            super(itemView);
            mainCardView = itemView.findViewById(R.id.main_card_view);
            mainTable = itemView.findViewById(R.id.stat_table);
        }
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        View blankHeader;

        HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            blankHeader = itemView.findViewById(R.id.blank_view);
        }
    }

    private static class StatusCardViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView infected, recovered, death, country, tested;

        StatusCardViewHolder(@NonNull View itemView) {
            super(itemView);
            country = itemView.findViewById(R.id.country_name);
            tested = itemView.findViewById(R.id.tested);
            infected = itemView.findViewById(R.id.infected);
            recovered = itemView.findViewById(R.id.recovered);
            death = itemView.findViewById(R.id.death);
        }
    }

    private static class ButtonCardViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView mainCard;
        AppCompatTextView mainText;
        AppCompatButton mainButton;

        ButtonCardViewHolder(@NonNull View itemView) {
            super(itemView);
            mainCard = itemView.findViewById(R.id.root);
            mainText = itemView.findViewById(R.id.main_text);
            mainButton = itemView.findViewById(R.id.main_button);
        }
    }

}
