package ethiopia.covid.android.data

/**
 * Created by BrookMG on 3/25/2020 in ethiopia.covid.android.data
 * inside the project CoVidEt .
 */
data class Case(
        var total: Int,
        var stable: Int,
        var critical: Int,
        var deceased: Int,
        var tested: Int,
        var confirmed: Int,
        var recovered: Int,
        var no_longer_in_ethiopia: Int,
        var false_positives: Int
)