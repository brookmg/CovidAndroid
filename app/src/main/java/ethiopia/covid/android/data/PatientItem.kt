package ethiopia.covid.android.data

import java.util.*

/**
 * Created by BrookMG on 3/25/2020 in ethiopia.covid.android.data
 * inside the project CoVidEt .
 */
data class PatientItem(var id: Int, // "Japan",
                  var patient_nationality: String, // "Addis Ababa",
                  var location: String, // "Burkina Faso",
                  var recent_travel_to: String, // "Not Available",
                  var name: String, // 48,
                  var age: Int, // "M"
                  var gender: String, // "ST",
                  var status: String, // "2020-03-13T12:00:00Z"
                  var date_time_announced: Date)