package ethiopia.covid.android.util

import java.util.*

/**
 * Created by BrookMG on 3/23/2020 in ethiopia.covid.android.util
 * inside the project CoVidEt .
 */
@Suppress("unused")
object Constant {
    const val TAG_HOME = "tag_home"
    const val TAG_QUESTIONNAIRE = "tag_questionnaire"
    const val TAG_HEAT_MAP = "tag_heat_map"
    const val PREFERENCE_THEME = "pref_theme"
    const val PREFERENCE_LATEST_NEWS = "pref_latest_news"
    const val PREFERENCE_QTIME = "pref_q_time"
    const val TIME_STAMP_FORMAT = "yyyy-MM-dd HH:mm:ss"
    const val FRC_QUESTIONNAIRE_EN = "frc_questionnaire_en"
    const val FRC_QUESTIONNAIRE_AM = "frc_questionnaire_am"
    const val FRC_QUESTIONNAIRE_OR = "frc_questionnaire_or"
    const val FRC_QUESTIONNAIRE_TI = "frc_questionnaire_ti"

    val MATERIAL_COLORS: List<String> = listOf( "#F44336","#333333","#FFCDD2","#EF9A9A","#E57373",
                                                "#EF5350","#E53935","#D32F2F","#C62828","#B71C1C",
                                                "#FF8A80","#FF5252","#FF1744","#D50000","#333333",
                                                "#F8BBD0","#F48FB1","#F06292","#EC407A","#E91E63",
                                                "#D81B60","#C2185B","#AD1457","#880E4F","#FF80AB",
                                                "#FF4081","#F50057","#C51162","#333333","#E1BEE7",
                                                "#CE93D8","#BA68C8","#AB47BC","#9C27B0","#8E24AA",
                                                "#7B1FA2","#6A1B9A","#4A148C","#EA80FC","#E040FB",
                                                "#D500F9","#AA00FF","#333333","#D1C4E9","#B39DDB",
                                                "#9575CD","#7E57C2","#673AB7","#5E35B1","#512DA8",
                                                "#4527A0","#311B92","#B388FF","#7C4DFF","#651FFF",
                                                "#6200EA","#333333","#C5CAE9","#9FA8DA","#7986CB",
                                                "#5C6BC0","#3F51B5","#3949AB","#303F9F","#283593",
                                                "#1A237E","#8C9EFF","#536DFE","#3D5AFE","#304FFE",
                                                "#333333","#BBDEFB","#90CAF9","#64B5F6","#42A5F5",
                                                "#2196F3","#1E88E5","#1976D2","#1565C0","#0D47A1",
                                                "#82B1FF","#448AFF","#2979FF","#2962FF","#333333",
                                                "#B3E5FC","#81D4FA","#4FC3F7","#29B6F6","#03A9F4",
                                                "#039BE5","#0288D1","#0277BD","#01579B","#80D8FF",
                                                "#40C4FF","#00B0FF","#0091EA","#333333","#B2EBF2",
                                                "#80DEEA","#4DD0E1","#26C6DA","#00BCD4","#00ACC1",
                                                "#0097A7","#00838F","#006064","#84FFFF","#18FFFF",
                                                "#00E5FF","#00B8D4","#333333","#B2DFDB","#80CBC4",
                                                "#4DB6AC","#26A69A","#009688","#00897B","#00796B",
                                                "#00695C","#004D40","#A7FFEB","#64FFDA","#1DE9B6",
                                                "#00BFA5","#333333","#C8E6C9","#A5D6A7","#81C784",
                                                "#66BB6A","#4CAF50","#43A047","#388E3C","#2E7D32",
                                                "#1B5E20","#B9F6CA","#69F0AE","#00E676","#00C853",
                                                "#333333","#DCEDC8","#C5E1A5","#AED581","#9CCC65",
                                                "#8BC34A","#7CB342","#689F38","#558B2F","#33691E",
                                                "#CCFF90","#B2FF59","#76FF03","#64DD17","#333333",
                                                "#F0F4C3","#E6EE9C","#DCE775","#D4E157","#CDDC39",
                                                "#C0CA33","#AFB42B","#9E9D24","#827717","#F4FF81",
                                                "#EEFF41","#C6FF00","#AEEA00","#333333","#FFF9C4",
                                                "#FFF59D","#FFF176","#FFEE58","#FFEB3B","#FDD835",
                                                "#FBC02D","#F9A825","#F57F17","#FFFF8D","#FFFF00",
                                                "#FFEA00","#FFD600","#333333","#FFECB3","#FFE082",
                                                "#FFD54F","#FFCA28","#FFC107","#FFB300","#FFA000",
                                                "#FF8F00","#FF6F00","#FFE57F","#FFD740","#FFC400",
                                                "#FFAB00","#333333","#FFE0B2","#FFCC80","#FFB74D",
                                                "#FFA726","#FF9800","#FB8C00","#F57C00","#EF6C00",
                                                "#E65100","#FFD180","#FFAB40","#FF9100","#FF6D00",
                                                "#333333","#FFCCBC","#FFAB91","#FF8A65","#FF7043",
                                                "#FF5722","#F4511E","#E64A19","#D84315","#BF360C",
                                                "#FF9E80","#FF6E40","#FF3D00","#DD2C00","#333333",
                                                "#D7CCC8","#BCAAA4","#A1887F","#8D6E63","#795548",
                                                "#6D4C41","#5D4037","#4E342E","#3E2723","#333333",
                                                "#333333","#333333","#333333","#BDBDBD","#9E9E9E",
                                                "#757575","#616161","#424242","#212121","#333333",
                                                "#CFD8DC","#B0BEC5","#90A4AE","#78909C","#607D8B",
                                                "#546E7A","#455A64","#37474F","#263238","#000000")

    @JvmStatic
    fun getQuestionnaireConstant(localeLanguage: String): String {
        return "frc_questionnaire_$localeLanguage"
    }

    @JvmField
    val regionNameWithCodeMap = object : HashMap<String?, String?>() {
        init {
            put("Addis Ababa", "aa")
            put("Afar Region", "af")
            put("Amhara Region", "am")
            put("Benishangul-Gumuz Region", "be")
            put("Dire Dawa", "dd")
            put("Gambela Region", "ga")
            put("Harari Region", "ha")
            put("Oromia Region", "or")
            put("Somali Region", "so")
            put("Southern Nations, Nationalities, and Peoples' Region", "snnp")
            put("Tigray Region", "tg")
        }
    }
    @JvmField
    val statusIdentifierMap = object : HashMap<String?, String?>() {
        init {
            put("ST", "Stable")
            put("CR", "Critical")
            put("RE", "Recovered")
            put("NA", "Not Available")
            put("DC", "Deceased")
        }
    }
}