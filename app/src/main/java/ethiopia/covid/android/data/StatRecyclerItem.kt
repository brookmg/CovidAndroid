package ethiopia.covid.android.data

import android.view.View

/**
 * Created by BrookMG on 3/24/2020 in ethiopia.covid.android.data
 * inside the project CoVidEt .
 */
class StatRecyclerItem {
    var type = 0 // 0 -> Table , 1 -> Pie , 2 -> Status card, 3 -> Patients , 4 -> Line , 5 -> Button card

    // Table stuff
    var tableItems: List<CovidStatItem?>? = null
    var headers: List<String>? = null
    var fixedHeaderCount = 0
    var pieCardTitle: String? = null
    var pieValues: List<Int>? = null
    var pieLabels: List<String>? = null
    var pieColors: List<Int>? = null
    var lineCardTitle: String? = null
    var lineChartItems: List<LineChartItem>? = null
    var lineLabels: List<String>? = null

    // Status Card
    var country: String? = null
    var totalInfected = 0
    var totalDeath = 0
    var totalRecovered = 0
    var totalTested = 0

    // Patient table stuff
    var patientItems: List<PatientItem>? = null

    // Button Card stuff
    var buttonCardText: String? = null
    var buttonText: String? = null
    var buttonOnClickListener: View.OnClickListener? = null

    constructor(buttonCardText: String?, buttonText: String?, buttonOnClickListener: View.OnClickListener?) {
        type = 5
        this.buttonCardText = buttonCardText
        this.buttonText = buttonText
        this.buttonOnClickListener = buttonOnClickListener
    }

    constructor(lineCardTitle: String?, lineChartItems: List<LineChartItem>?, lineLabels: List<String>?) {
        type = 4
        this.lineCardTitle = lineCardTitle
        this.lineChartItems = lineChartItems
        this.lineLabels = lineLabels
    }

    constructor(pieCardTitle: String?, pieValues: List<Int>?, pieLabels: List<String>?, pieColors: List<Int>?) {
        type = 1
        this.pieCardTitle = pieCardTitle
        this.pieValues = pieValues
        this.pieLabels = pieLabels
        this.pieColors = pieColors
    }

    constructor(country: String?, totalInfected: Int, totalDeath: Int, totalRecovered: Int, totalTested: Int) {
        type = 2
        this.country = country
        this.totalInfected = totalInfected
        this.totalDeath = totalDeath
        this.totalRecovered = totalRecovered
        this.totalTested = totalTested
    }

    constructor(headers: List<String>?, fixedHeaderCount: Int, patientItems: List<PatientItem>?) {
        type = 3
        this.headers = headers
        this.fixedHeaderCount = fixedHeaderCount
        this.patientItems = patientItems
    }

    constructor(tableItems: List<CovidStatItem?>?, headers: List<String>?) : this(0, tableItems, headers, 0)

    @JvmOverloads
    constructor(type: Int, tableItems: List<CovidStatItem?>?, headers: List<String>?, fixedHeaderCount: Int = 0) {
        this.type = type
        this.tableItems = tableItems
        this.headers = headers
        this.fixedHeaderCount = fixedHeaderCount
    }

}