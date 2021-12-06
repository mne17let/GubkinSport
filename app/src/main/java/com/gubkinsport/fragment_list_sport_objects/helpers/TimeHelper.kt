package com.gubkinsport.fragment_list_sport_objects.helpers

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class TimeHelper {

    private val TAG_SIMPLE_TIME_HELPER = "SimpleTimeHelper"

    fun getCurrentTime(): Long {
        val currentTime: Long = Calendar.getInstance().time.time
        return currentTime
    }

    fun parseTimeFromString(string: String): Long{
        val dateFormat = SimpleDateFormat("HH:mm dd-MM-yyyy", Locale.getDefault())

        Log.d(TAG_SIMPLE_TIME_HELPER, "Дата открытия  == ${string}")

        val newDate = dateFormat.parse(string)

        Log.d(TAG_SIMPLE_TIME_HELPER, "Объект даты  == ${newDate}")

        return newDate.time
    }

    fun parseStringFromMilliseconds(milliseconds: Long): String{
        // val dateFormat = SimpleDateFormat("hh:mm dd-MM-yyyy", Locale.CANADA)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliseconds
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        val stringDateMyFormat: String = "$hour:$minute $day-$month-$year"

        return stringDateMyFormat
    }

}