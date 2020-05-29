package ethiopia.covid.android.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.animation.Easing
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
import ethiopia.covid.android.databinding.*
import ethiopia.covid.android.ui.widget.Table
import ethiopia.covid.android.util.Constant
import ethiopia.covid.android.util.Utils
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
            HEADER -> return HeaderViewHolder(
                    BlankHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            STAT_TABLE -> return CovidStatisticTableViewHolder(
                    StatTableRecyclerElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            STATUS_CARD -> return StatusCardViewHolder(
                    StatusCardRecyclerElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            PIE_CHART -> return CovidStatisticPieViewHolder(
                    StatTablePieElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            LINE_CHART -> return CovidStatisticLineGraphViewHolder(
                    StatTableGraphElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            BUTTON_CARD -> return ButtonCardViewHolder(
                    CardRecyclerElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> return CovidStatisticTableViewHolder(
                    StatTableRecyclerElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when {
            getItemViewType(position) == STAT_TABLE -> (holder as? ViewHolderWithBind)?.bind(statRecyclerItemList[position-1])
            getItemViewType(position) == STATUS_CARD -> (holder as? ViewHolderWithBind)?.bind(statRecyclerItemList[position-1])
            getItemViewType(position) == PATIENT_TABLE -> (holder as? ViewHolderWithBind)?.bind(statRecyclerItemList[position-1])
            getItemViewType(position) == PIE_CHART -> (holder as? ViewHolderWithBind)?.bind(statRecyclerItemList[position-1])
            getItemViewType(position) == LINE_CHART -> (holder as? ViewHolderWithBind)?.bind(statRecyclerItemList[position-1])
            getItemViewType(position) == BUTTON_CARD -> (holder as? ViewHolderWithBind)?.bind(statRecyclerItemList[position-1])
            else -> (holder as? ViewHolderWithBind)?.bind(statRecyclerItemList[0])  // This is just the blank header
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

    private open class ViewHolderWithBind(itemView: View) : RecyclerView.ViewHolder(itemView) {
        open fun bind(bindWith: StatRecyclerItem) {}
    } 
    
    private class CovidStatisticLineGraphViewHolder(private val statTableGraphElementBinding: StatTableGraphElementBinding) 
        : ViewHolderWithBind(statTableGraphElementBinding.root) {

        override fun bind(bindWith: StatRecyclerItem) {
            super.bind(bindWith)
            
            statTableGraphElementBinding.mainCardView.setContentPadding(0, 0, 0, 0)
            
            val chart = statTableGraphElementBinding.lineChart
            statTableGraphElementBinding.lineTitle.text = bindWith.lineCardTitle
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
            chart.xAxis.textColor = if (getCurrentTheme(itemView.context) == 0) Color.BLACK else Color.WHITE
            chart.xAxis.labelRotationAngle = 90f

            run {
                chart.xAxis.valueFormatter = object : IndexAxisValueFormatter() {
                    override fun getFormattedValue(value: Float): String =
                            bindWith.lineLabels?.get(value.toInt()) ?: ""
                }
            }

            chart.axisLeft.textColor = if (getCurrentTheme(itemView.context) == 0) Color.BLACK else Color.WHITE

            // draw points over time
            chart.animateX(300)

            // get the legend (only possible after setting data)
            val l = chart.legend

            // draw legend entries as lines
            l.form = Legend.LegendForm.LINE
            l.textColor = if (getCurrentTheme(itemView.context) == 0) Color.BLACK else Color.WHITE
            val dataSets = ArrayList<ILineDataSet>()
            for ((values1, chartLabel, lineColor, circleColor) in bindWith.lineChartItems
                    ?: listOf()) {
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
                    dataSet.valueTextColor = if (getCurrentTheme(itemView.context) == 0) Color.BLACK else Color.WHITE
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
                    dataSet.highLightColor = ContextCompat.getColor(itemView.context, R.color.purple_0)
                    dataSet.enableDashedHighlightLine(10f, 5f, 0f)

                    // set the filled area
                    dataSet.setDrawFilled(false)
                    dataSets.add(dataSet) // add the data sets
                }
            }

            // create a data object with the data sets
            val data = LineData(dataSets)
            data.setDrawValues(false)
            data.setValueTextColor(if (getCurrentTheme(itemView.context) == 0) Color.BLACK else Color.WHITE)

            // set data
            chart.data = data

        }
    }

    private class CovidStatisticPieViewHolder(private val statTablePieElementBinding: StatTablePieElementBinding) 
        : ViewHolderWithBind(statTablePieElementBinding.root) {
//        var mainPieChart: PieChart = itemView.findViewById(R.id.pie_chart)
//        var mainCardView: MaterialCardView = itemView.findViewById(R.id.main_card_view)
//        var pieCardTitle: AppCompatTextView = itemView.findViewById(R.id.pie_title)

        override fun bind(bindWith: StatRecyclerItem) {
            super.bind(bindWith)
            statTablePieElementBinding.mainCardView.setContentPadding(0, 0, 0, 0)

            statTablePieElementBinding.pieTitle.text = bindWith.pieCardTitle

            val entries = ArrayList<PieEntry>()
            statTablePieElementBinding.pieChart.setUsePercentValues(true)
            statTablePieElementBinding.pieChart.description.isEnabled = false
            statTablePieElementBinding.pieChart.setExtraOffsets(5f, 10f, 5f, 5f)
            statTablePieElementBinding.pieChart.dragDecelerationFrictionCoef = 0.95f
            statTablePieElementBinding.pieChart.isDrawHoleEnabled = true
            statTablePieElementBinding.pieChart.setHoleColor(
                    if (getCurrentTheme(statTablePieElementBinding.root.context) == 0) Color.WHITE
                    else ContextCompat.getColor(statTablePieElementBinding.root.context, R.color.black_2)
            )
            statTablePieElementBinding.pieChart.setTransparentCircleAlpha(110)
            statTablePieElementBinding.pieChart.holeRadius = 40f
            statTablePieElementBinding.pieChart.transparentCircleRadius = 44f
            statTablePieElementBinding.pieChart.setDrawCenterText(true)
            statTablePieElementBinding.pieChart.rotationAngle = 0f
            // enable rotation of the chart by touch
            statTablePieElementBinding.pieChart.isRotationEnabled = true
            statTablePieElementBinding.pieChart.isHighlightPerTapEnabled = true
            statTablePieElementBinding.pieChart.animateY(400, Easing.EaseInOutQuad)

            val l = statTablePieElementBinding.pieChart.legend
            l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
            l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
            l.orientation = Legend.LegendOrientation.VERTICAL
            l.textColor = if (getCurrentTheme(statTablePieElementBinding.root.context) == 0) Color.BLACK else Color.WHITE
            l.setDrawInside(false)
            l.xEntrySpace = 7f
            l.yEntrySpace = 0f
            l.yOffset = 0f

            // entry label styling
            statTablePieElementBinding.pieChart.setEntryLabelColor(Color.WHITE)
            statTablePieElementBinding.pieChart.setDrawEntryLabels(false)
            statTablePieElementBinding.pieChart.setNoDataTextColor(
                    if (getCurrentTheme(statTablePieElementBinding.root.context) == 0) Color.BLACK else Color.WHITE
            )
            statTablePieElementBinding.pieChart.setEntryLabelTextSize(12f)
            for (i in bindWith.pieValues?.indices ?: 0..0) {
                entries.add(PieEntry(
                        bindWith.pieValues?.get(i)?.toFloat() ?: 0F,
                        bindWith.pieLabels?.get(i) ?: ""
                ))
            }

            val mainDataSet = PieDataSet(entries, "")
            mainDataSet.setDrawIcons(false)
            mainDataSet.sliceSpace = 1f
            mainDataSet.iconsOffset = MPPointF(0F, 40F)
            mainDataSet.selectionShift = 5f
            mainDataSet.colors = bindWith.pieColors

            val data = PieData(mainDataSet)
            data.setValueFormatter(PercentFormatter(statTablePieElementBinding.pieChart))
            data.setValueTextSize(11f)
            data.setValueTextColor(Color.WHITE)
            statTablePieElementBinding.pieChart.data = data
            statTablePieElementBinding.pieChart.highlightValues(null)
            statTablePieElementBinding.pieChart.invalidate()
        }
    }

    private class CovidStatisticTableViewHolder (private val statTableRecyclerElementBinding: StatTableRecyclerElementBinding)
        : ViewHolderWithBind(statTableRecyclerElementBinding.root) {

        override fun bind(bindWith: StatRecyclerItem) {
            super.bind(bindWith)
            when (bindWith.type) {
                0 -> {
                    statTableRecyclerElementBinding.mainCardView.setContentPadding(0, 0, 0, 0)
                    val rowItems: MutableList<List<String>> = ArrayList()
                    for (item in bindWith.tableItems ?: listOf()) {
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
                    statTableRecyclerElementBinding.statTable.populateTable(
                            bindWith.headers,
                            rowItems,
                            bindWith.fixedHeaderCount > 0,
                            bindWith.fixedHeaderCount,
                            8
                    )

                }
                3 -> {
                    statTableRecyclerElementBinding.mainCardView.setContentPadding(0, 0, 0, 0)
                    val rowItems: MutableList<MutableList<String>> = mutableListOf()
                    for ((id, patient_nationality, location, recent_travel_to, name, age, gender, status) in bindWith.patientItems
                            ?: listOf()) {
                        rowItems.add(mutableListOf(
                                id.toString(),
                                name, location,
                                age.toString(), gender,
                                patient_nationality,
                                recent_travel_to,
                                Constant.statusIdentifierMap[status] ?: "Unknown"))
                    }
                    statTableRecyclerElementBinding.statTable.populateTable(
                            bindWith.headers,
                            rowItems,
                            bindWith.fixedHeaderCount > 0,
                            bindWith.fixedHeaderCount, 8
                    )
                }
            }
        }
    }

    private class HeaderViewHolder ( blankHeaderBinding: BlankHeaderBinding) : ViewHolderWithBind(blankHeaderBinding.root)

    private class StatusCardViewHolder (private val statusCardRecyclerElementBinding: StatusCardRecyclerElementBinding)
        : ViewHolderWithBind(statusCardRecyclerElementBinding.root) {

        override fun bind(bindWith: StatRecyclerItem) {
            super.bind(bindWith)
            statusCardRecyclerElementBinding.countryName.text = bindWith.country
            statusCardRecyclerElementBinding.tested.text = String.format(Locale.US, "%s",
                    formatNumber(bindWith.totalTested.toLong()))
            statusCardRecyclerElementBinding.infected.text = String.format(Locale.US, "%s",
                    formatNumber(bindWith.totalInfected.toLong()))
            statusCardRecyclerElementBinding.recovered.text = String.format(Locale.US, "%s",
                    formatNumber(bindWith.totalRecovered.toLong()))
            statusCardRecyclerElementBinding.death.text = String.format(Locale.US, "%s",
                    formatNumber(bindWith.totalDeath.toLong()))
        }
    }

    private class ButtonCardViewHolder (private val cardRecyclerElementBinding: CardRecyclerElementBinding)
        : ViewHolderWithBind(cardRecyclerElementBinding.root) {

        override fun bind(bindWith: StatRecyclerItem) {
            super.bind(bindWith)
            cardRecyclerElementBinding.mainText.text = bindWith.buttonCardText
            cardRecyclerElementBinding.mainButton.setOnClickListener(bindWith.buttonOnClickListener)
            cardRecyclerElementBinding.mainButton.text = bindWith.buttonText
        }

    }

}