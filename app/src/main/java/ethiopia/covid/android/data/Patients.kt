package ethiopia.covid.android.data

/**
 * Created by BrookMG on 3/25/2020 in ethiopia.covid.android.data
 * inside the project CoVidEt .
 */
data class Patients(// 12
        var count: Int, // "https://api.pmo.gov.et/v1/patients/?limit=10&offset=10",
        var next: String, // null
        var previous: String, var results: List<PatientItem>)