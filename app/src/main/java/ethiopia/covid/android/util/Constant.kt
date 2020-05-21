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