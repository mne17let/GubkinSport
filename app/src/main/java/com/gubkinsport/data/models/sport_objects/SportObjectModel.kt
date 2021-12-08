package com.gubkinsport.data.models.sport_objects

import android.util.Log
import com.gubkinsport.data.models.sport_objects.ui_format.UiSportObject
import com.gubkinsport.fragment_list_sport_objects.helpers.DaysSortHelper
import kotlin.collections.HashMap

// Модель спортивного объекта

data class SportObjectModel(
    val id: String? = null,
    val name: String? = null,
    val image_url: String? = null,
    val days: HashMap<String, HashMap<String, Period>>? = null
) {

    private val TAG_SOM = "MySportObjectModel"

    fun mapForUi(): UiSportObject?{
        var result: UiSportObject? = null

        if (days != null && id != null && name != null && image_url != null){
            Log.d(TAG_SOM, "Вызван DaysSortHelper")
            val timeHelper = DaysSortHelper(days)
            val sortedDaysAndPeriods = timeHelper.sortDaysAndPeriods()

            result = UiSportObject(id, name, image_url, sortedDaysAndPeriods)
        }

        return result
    }

}