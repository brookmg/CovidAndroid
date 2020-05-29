package ethiopia.covid.android.data

/**
 * Created by BrookMG on 3/24/2020 in ethiopia.covid.android.data
 * inside the project CoVidEt .
 */
data class CovidStatItem(var identifier: String, var infected: Int, var active: Int, var death: Int, var recovered: Int, var critical: Int, var casePMillion: Double, var deathsPMillion: Double)