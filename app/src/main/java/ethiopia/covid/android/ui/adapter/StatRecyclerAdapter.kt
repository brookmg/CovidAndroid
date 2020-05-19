package ethiopia.covid.android.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.MPPointF
import com.google.android.material.card.MaterialCardView
import ethiopia.covid.android.R
import ethiopia.covid.android.data.StatRecyclerItem
import ethiopia.covid.android.ui.widget.Table
import ethiopia.covid.android.util.Constant
import ethiopia.covid.android.util.Utils.formatNumber
import ethiopia.covid.android.util.Utils.getCurrentTheme
import java.util.*

/**
 * Created by BrookMG on 3/24/2020 in ethiopia.covid.android.ui.adapter
 * inside the project CoVidEt .
 */
class StatRecyclerAdapter(
        private val statRecyclerItemList: List<StatRecyclerItem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val HEADER = 0
    private val STAT_TABLE = 1
    private val PIE_CHART = 2
    private val STATUS_CARD = 3
    private val PATIENT_TABLE = 4
    private val LINE_CHART = 5
    private val BUTTON_CARD = 6

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            HEADER -> return HeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.blank_header, parent, false))
            STAT_TABLE -> return CovidStatisticTableViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.stat_table_recycler_element, parent, false)
            )
            STATUS_CARD -> return StatusCardViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.status_card_recycler_element, parent, false)
            )
            PIE_CHART -> return CovidStatisticPieViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.stat_table_pie_element, parent, false)
            )
            LINE_CHART -> return CovidStatisticLineGraphViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.stat_table_graph_element, parent, false)
            )
            BUTTON_CARD -> return ButtonCardViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.card_recycler_element, parent, false)
            )
            else -> return CovidStatisticTableViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.stat_table_recycler_element, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        @Suppress("NAME_SHADOWING") var position = position

        when {
            getItemViewType(position) == STAT_TABLE -> {
                position -= 1
                (holder as CovidStatisticTableViewHolder).mainCardView.setContentPadding(0, 0, 0, 0)
                val rowItems: MutableList<List<String>> = ArrayList()
                for (item in statRecyclerItemList[position].tableItems ?: listOf()) {
                    rowItems.add(listOf(
                            item?.identifier ?: "",
                            formatNumber(item?.infected?.toLong() ?: 0),
                            formatNumber(item?.active?.toLong()?: 0),
                            formatNumber(item?.death?.toLong()?: 0),
                            formatNumber(item?.recovered?.toLong()?: 0),
                            formatNumber(item?.critical?.toLong()?: 0),
                            item?.casePMillion.toString(),
                            item?.deathsPMillion.toString())
                    )
                }
                holder.mainTable.populateTable(
                        statRecyclerItemList[position].headers,
                        rowItems,
                        statRecyclerItemList[position].fixedHeaderCount > 0,
                        statRecyclerItemList[position].fixedHeaderCount,
                        8
                )
            }
            getItemViewType(position) == STATUS_CARD -> {
                position -= 1
                (holder as StatusCardViewHolder).country.text = statRecyclerItemList[position].country
                holder.tested.text = String.format(Locale.US, "%d",
                        statRecyclerItemList[position].totalTested)
                holder.infected.text = String.format(Locale.US, "%d",
                        statRecyclerItemList[position].totalInfected)
                holder.recovered.text = String.format(Locale.US, "%d",
                        statRecyclerItemList[position].totalRecovered)
                holder.death.text = String.format(Locale.US, "%d",
                        statRecyclerItemList[position].totalDeath)
            }
            getItemViewType(position) == PATIENT_TABLE -> {
                position -= 1
                (holder as CovidStatisticTableViewHolder).mainCardView.setContentPadding(0, 0, 0, 0)
                val rowItems: MutableList<MutableList<String>> = mutableListOf()
                for ((id, patient_nationality, location, recent_travel_to, name, age, gender, status) in statRecyclerItemList[position].patientItems
                        ?: listOf()) {
                    rowItems.add(mutableListOf(
                            id.toString(),
                            name, location,
                            age.toString(), gender,
                            patient_nationality,
                            recent_travel_to,
                            Constant.statusIdentifierMap[status] ?: "Unknown"))
                }
                holder.mainTable.populateTable(
                        statRecyclerItemList[position].headers,
                        rowItems,
                        statRecyclerItemList[position].fixedHeaderCount > 0,
                        statRecyclerItemList[position].fixedHeaderCount, 8
                )
            }
            getItemViewType(position) == PIE_CHART -> {
                position -= 1
                (holder as CovidStatisticPieViewHolder).mainCardView.setContentPadding(0, 0, 0, 0)

                holder.pieCardTitle.text = statRecyclerItemList[position].pieCardTitle

                val entries = ArrayList<PieEntry>()
                holder.mainPieChart.setUsePercentValues(true)
                holder.mainPieChart.description.isEnabled = false
                holder.mainPieChart.setExtraOffsets(5f, 10f, 5f, 5f)
                holder.mainPieChart.dragDecelerationFrictionCoef = 0.95f
                holder.mainPieChart.isDrawHoleEnabled = true
                holder.mainPieChart.setHoleColor(
                        if (getCurrentTheme(holder.itemView.context) == 0) Color.WHITE
                        else ContextCompat.getColor(holder.itemView.context, R.color.black_2)
                )
                holder.mainPieChart.setTransparentCircleAlpha(110)
                holder.mainPieChart.holeRadius = 40f
                holder.mainPieChart.transparentCircleRadius = 44f
                holder.mainPieChart.setDrawCenterText(true)
                holder.mainPieChart.rotationAngle = 0f
                // enable rotation of the chart by touch
                holder.mainPieChart.isRotationEnabled = true
                holder.mainPieChart.isHighlightPerTapEnabled = true
                holder.mainPieChart.animateY(400, Easing.EaseInOutQuad)

                val l = holder.mainPieChart.legend
                l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
                l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                l.orientation = Legend.LegendOrientation.VERTICAL
                l.textColor = if (getCurrentTheme(holder.itemView.context) == 0) Color.BLACK else Color.WHITE
                l.setDrawInside(false)
                l.xEntrySpace = 7f
                l.yEntrySpace = 0f
                l.yOffset = 0f

                // entry label styling
                holder.mainPieChart.setEntryLabelColor(Color.WHITE)
                holder.mainPieChart.setDrawEntryLabels(false)
                holder.mainPieChart.setNoDataTextColor(
                        if (getCurrentTheme(holder.itemView.context) == 0) Color.BLACK else Color.WHITE
                )
                holder.mainPieChart.setEntryLabelTextSize(12f)
                for (i in statRecyclerItemList[position].pieValues?.indices ?: listOf<Int>()) {
                    entries.add(PieEntry(
                        statRecyclerItemList[position].pieValues?.get(i)?.toFloat() ?: 0F,
                        statRecyclerItemList[position].pieLabels?.get(i) ?: ""
                    ))
                }

                val mainDataSet = PieDataSet(entries, "")
                mainDataSet.setDrawIcons(false)
                mainDataSet.sliceSpace = 1f
                mainDataSet.iconsOffset = MPPointF(0F, 40F)
                mainDataSet.selectionShift = 5f
                mainDataSet.colors = statRecyclerItemList[position].pieColors

                val data = PieData(mainDataSet)
                data.setValueFormatter(PercentFormatter(holder.mainPieChart))
                data.setValueTextSize(11f)
                data.setValueTextColor(Color.WHITE)
                holder.mainPieChart.data = data
                holder.mainPieChart.highlightValues(null)
                holder.mainPieChart.invalidate()
            }
            getItemViewType(position) == LINE_CHART -> {
                position -= 1
                (holder as CovidStatisticLineGraphViewHolder).mainCardView.setContentPadding(0, 0, 0, 0)
                val chart = holder.mainLineChart
                holder.lineCartCardTitle.text = statRecyclerItemList[position].lineCardTitle
                //chart.setBackgroundColor(Color.WHITE);
                chart.description.isEnabled = false
                chart.setTouchEnabled(true)
                chart.setDrawGridBackground(false)
                chart.isDragEnabled = true
                chart.setScaleEnabled(true)

                // force pinch zoom along both axis
                chart.setPinchZoom(true)
                chart.axisRight.isEnabled = false
                chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
                chart.xAxis.textColor = if (getCurrentTheme(holder.itemView.context) == 0) Color.BLACK else Color.WHITE
                chart.xAxis.labelRotationAngle = 90f

                run {
                    val finalPosition = position
                    chart.xAxis.valueFormatter = object : IndexAxisValueFormatter() {
                        override fun getFormattedValue(value: Float): String =
                            statRecyclerItemList[finalPosition].lineLabels?.get(value.toInt()) ?: ""
                    }
                }

                chart.axisLeft.textColor = if (getCurrentTheme(holder.itemView.context) == 0) Color.BLACK else Color.WHITE

                // draw points over time
                chart.animateX(300)

                // get the legend (only possible after setting data)
                val l = chart.legend

                // draw legend entries as lines
                l.form = Legend.LegendForm.LINE
                l.textColor = if (getCurrentTheme(holder.itemView.context) == 0) Color.BLACK else Color.WHITE
                val dataSets = ArrayList<ILineDataSet>()
                for ((values1, chartLabel, lineColor, circleColor) in statRecyclerItemList[position].lineChartItems ?: listOf()) {
                    var dataSet: LineDataSet
                    val values = ArrayList<Entry>()

                    for ((i, item) in values1.withIndex()) values.add(Entry(i.toFloat(), item.toFloat()))

                    if (chart.data != null && chart.data.dataSetCount > 0) {
                        dataSet = chart.data.getDataSetByIndex(0) as LineDataSet
                        dataSet.values = values
                        dataSet.notifyDataSetChanged()
                        chart.data.notifyDataChanged()
                        chart.notifyDataSetChanged()
                    } else {

                        // create a dataset, which should belong to a country
                        dataSet = LineDataSet(values, chartLabel)
                        dataSet.setDrawIcons(false)
                        dataSet.color = lineColor
                        dataSet.valueTextColor = if (getCurrentTheme(holder.itemView.context) == 0) Color.BLACK else Color.WHITE
                        dataSet.setCircleColor(circleColor)

                        // line thickness and point size
                        dataSet.lineWidth = 1f
                        dataSet.circleRadius = 3f

                        // draw points as solid circles
                        dataSet.setDrawCircleHole(false)

                        // customize legend entry
                        dataSet.formLineWidth = 1f
                        dataSet.formSize = 15f

                        // text size of values
                        dataSet.valueTextSize = 9f

                        // draw selection line as dashed
                        dataSet.highLightColor = ContextCompat.getColor(holder.itemView.context, R.color.purple_0)
                        dataSet.enableDashedHighlightLine(10f, 5f, 0f)

                        // set the filled area
                        dataSet.setDrawFilled(false)
                        dataSets.add(dataSet) // add the data sets
                    }
                }

                // create a data object with the data sets
                val data = LineData(dataSets)
                data.setDrawValues(false)
                data.setValueTextColor(if (getCurrentTheme(holder.itemView.context) == 0) Color.BLACK else Color.WHITE)

                // set data
                chart.data = data
            }
            getItemViewType(position) == BUTTON_CARD -> {
                position -= 1
                (holder as ButtonCardViewHolder).mainText.text = statRecyclerItemList[position].buttonCardText
                holder.mainButton.setOnClickListener(statRecyclerItemList[position].buttonOnClickListener)
                holder.mainButton.text = statRecyclerItemList[position].buttonText
            }
            else -> {
//            ViewGroup.LayoutParams params = ((HeaderViewHolder) holder).blankView.getLayoutParams();
//            params.height = dpToPx(holder.itemView.getContext(), 116);
//            ((HeaderViewHolder) holder).blankView.setLayoutParams(params);
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) HEADER else {
            when (statRecyclerItemList[position - 1].type) {
                0 -> STAT_TABLE
                1 -> PIE_CHART
                2 -> STATUS_CARD
                3 -> PATIENT_TABLE
                4 -> LINE_CHART
                5 -> BUTTON_CARD
                else -> HEADER
            }
        }
    }

    override fun getItemCount(): Int = statRecyclerItemList.size + 1

    private class CovidStatisticLineGraphViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mainLineChart: LineChart = itemView.findViewById(R.id.line_chart)
        var mainCardView: MaterialCardView = itemView.findViewById(R.id.main_card_view)
        var lineCartCardTitle: AppCompatTextView = itemView.findViewById(R.id.line_title)
    }

    private class CovidStatisticPieViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mainPieChart: PieChart = itemView.findViewById(R.id.pie_chart)
        var mainCardView: MaterialCardView = itemView.findViewById(R.id.main_card_view)
        var pieCardTitle: AppCompatTextView = itemView.findViewById(R.id.pie_title)
    }

    private class CovidStatisticTableViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mainTable: Table = itemView.findViewById(R.id.stat_table)
        var mainCardView: MaterialCardView = itemView.findViewById(R.id.main_card_view)
    }

    private class HeaderViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var blankHeader: View = itemView.findViewById(R.id.blank_view)
    }

    private class StatusCardViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var infected: AppCompatTextView = itemView.findViewById(R.id.infected)
        var recovered: AppCompatTextView = itemView.findViewById(R.id.recovered)
        var death: AppCompatTextView = itemView.findViewById(R.id.death)
        var country: AppCompatTextView = itemView.findViewById(R.id.country_name)
        var tested: AppCompatTextView = itemView.findViewById(R.id.tested)
    }

    private class ButtonCardViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mainCard: MaterialCardView = itemView.findViewById(R.id.root)
        var mainText: AppCompatTextView = itemView.findViewById(R.id.main_text)
        var mainButton: AppCompatButton = itemView.findViewById(R.id.main_button)
    }

}