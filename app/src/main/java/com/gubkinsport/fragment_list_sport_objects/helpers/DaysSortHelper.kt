package com.gubkinsport.fragment_list_sport_objects.helpers

import android.util.Log
import com.gubkinsport.data.models.people.Booking
import com.gubkinsport.data.models.sport_objects.Period
import com.gubkinsport.data.models.sport_objects.ui_format.UiDay
import com.gubkinsport.data.models.sport_objects.ui_format.UiPeriod
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

// Класс для сортировки объектов List, Map и конвертации строк во время и дату и обратно

class DaysSortHelper(private val sourceHashMap: HashMap<String, HashMap<String, Period>>) {

    //Тег для логов
    private val TAG_TIME_HELPER = "TimeHelperTag"

    fun sortDaysAndPeriods(): List<UiDay> {
        val result = sortMap()
        return result
    }

    fun sortOnlyPeriods(source: HashMap<String, Period>): List<UiPeriod> {
        val result = getSortedListOfPeriods(source)
        return result
    }

    fun sortBookings(source: List<Booking>){

    }

    private fun sortMap(): List<UiDay> {
        val longStringSortedMap: SortedMap<Long, String> = getStringLongMap()

        Log.d(TAG_TIME_HELPER, "Получена мапа: ${sourceHashMap.keys}")

        val longSortedListOfKeys = longStringSortedMap.keys

        val sortedListOfStringDate = mutableListOf<String>()

        for (key in longSortedListOfKeys) {
            val newValue = longStringSortedMap[key]
            if (newValue != null) {
                // Log.d(TAG_TIME_HELPER, "Дата: ${newValue}")

                sortedListOfStringDate.add(newValue)
            }
        }

        val sortedListOfDays = mutableListOf<UiDay>()

        for (longDate in longStringSortedMap.keys) {
            val newValueStringDate = longStringSortedMap[longDate]
            val newValueMapOfPeriodsCurrentStringDate = sourceHashMap[newValueStringDate]

            if (newValueStringDate != null && newValueMapOfPeriodsCurrentStringDate != null) {
                val sortedPeriods = getSortedListOfPeriods(newValueMapOfPeriodsCurrentStringDate)


                /*Log.d(TAG_TIME_HELPER, "Сорт: $newValueStringDate")
                Log.d(TAG_TIME_HELPER, "Несорт: $newValueMapOfPeriodsCurrentStringDate")*/

                Log.d(TAG_TIME_HELPER, "$sortedPeriods")

                sortedListOfDays.add(UiDay(longDate, newValueStringDate, sortedPeriods))
            }
        }

        for (day in sortedListOfDays) {
            // Log.d(TAG_TIME_HELPER, "День: ${day.date}, дата в мс: ${day.dateLong}")

            if (day.listOfPeriods != null) {
                for (period in day.listOfPeriods) {
                    // Log.d(TAG_TIME_HELPER, "${period}")
                }
            }
        }

        return sortedListOfDays
    }

    private fun getStringLongMap(): SortedMap<Long, String> {
        val newMap = sortedMapOf<Long, String>()

        // Log.d(TAG_TIME_HELPER, "Получена мапа: ${sourceHashMap.keys}")

        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        for (key in sourceHashMap.keys) {
            val dateFromString = dateFormat.parse(key)

            if (dateFromString != null) {
                newMap[dateFromString.time] = key
            }
        }

        // Log.d(TAG_TIME_HELPER, "Итоговая мапа: ${newMap}")

        return newMap
    }

    private fun getSortedListOfPeriods(mapOfPeriods: HashMap<String, Period>): List<UiPeriod> {
        val sortedPeriods = mutableListOf<UiPeriod>()

        // Log.d(TAG_TIME_HELPER, "Получена мапа: $mapOfPeriods")

        val sortedMapOfOpensLongToString = sortedMapOf<Long, String>()

        for (stringKey in mapOfPeriods.keys) {
            val currentPeriod = mapOfPeriods[stringKey]
            val currentOpen = currentPeriod?.open

            val dateFormat = SimpleDateFormat("HH:mm dd-MM-yyyy", Locale.getDefault())

            var dateFromString: Date? = null
            if (currentOpen != null) {
                dateFromString = dateFormat.parse(currentOpen)
            }

            if (dateFromString != null) {
                sortedMapOfOpensLongToString[dateFromString.time] = mapOfPeriods[stringKey]?.open
            }
        }

        /*for (longKey in sortedMapOfOpensLongToString.keys){
            Log.d(TAG_TIME_HELPER, "$sortedPeriods")
        }*/

        //Log.d(TAG_TIME_HELPER, "Отсортированы по лонгам даты: $sortedMapOfOpensLongToString")

        for (key in sortedMapOfOpensLongToString.keys) {

            //Log.d(TAG_TIME_HELPER, "Взят лонг: $key")

            for (stringPeriodKey in mapOfPeriods.keys) {

                //Log.d(TAG_TIME_HELPER, "Взято название периода: $stringPeriodKey")

                val currentPeriod = mapOfPeriods[stringPeriodKey]

                //Log.d(TAG_TIME_HELPER, "Взят период: $currentPeriod")

                val currentValueInSortedMap = sortedMapOfOpensLongToString[key]
                val currentOpen = currentPeriod?.open

                //Log.d(TAG_TIME_HELPER, "По лонгу получено значение открытия: $currentValueInSortedMap")
                //Log.d(TAG_TIME_HELPER, "Время открытия текущего периода: $currentOpen")

                if (currentPeriod != null && currentPeriod.open != null && currentPeriod.close != null &&
                    sortedMapOfOpensLongToString[key] == currentPeriod.open
                ) {

                    // Log.d(TAG_TIME_HELPER, "Дата открытия  == ${currentPeriod.open}")

                    val dateFormat = SimpleDateFormat("HH:mm dd-MM-yyyy", Locale.getDefault())
                    val dateFromStringOpen = dateFormat.parse(currentPeriod.open)
                    val dateFromStringClose = dateFormat.parse(currentPeriod.close)

                    // Log.d(TAG_TIME_HELPER, "После парсинга строк открытие == ${dateFromStringOpen}")

                    if (dateFromStringClose != null && dateFromStringOpen != null) {
                        Log.d(TAG_TIME_HELPER, "Получена строка: ${dateFromStringOpen}")
                        Log.d(TAG_TIME_HELPER, "Получено время: ${dateFromStringOpen.time}")
                        val longTimeOpen = dateFromStringOpen.time
                        val longTimeClose = dateFromStringClose.time

                        val openCalendar = Calendar.getInstance()
                        openCalendar.timeInMillis = longTimeOpen

                        val closeCalendar = Calendar.getInstance()
                        closeCalendar.timeInMillis = longTimeClose



                        // Log.d(TAG_TIME_HELPER, "Итоговый месяц: ${openCalendar.get(Calendar.MONTH)}")
                        Log.d(TAG_TIME_HELPER, "Итоговый месяц: ${closeCalendar.get(Calendar.MONTH)}")

                        val newPeriod = UiPeriod(
                            longTimeOpen,
                            longTimeClose,
                            currentPeriod.open,
                            currentPeriod.close,
                            stringPeriodKey,
                            openCalendar.get(Calendar.MINUTE),
                            openCalendar.get(Calendar.HOUR_OF_DAY),
                            openCalendar.get(Calendar.DAY_OF_MONTH),
                            openCalendar.get(Calendar.MONTH) + 1,
                            openCalendar.get(Calendar.YEAR),

                            closeCalendar.get(Calendar.MINUTE),
                            closeCalendar.get(Calendar.HOUR_OF_DAY),
                            closeCalendar.get(Calendar.DAY_OF_MONTH) + 1,
                            closeCalendar.get(Calendar.MONTH),
                            closeCalendar.get(Calendar.YEAR)
                        )

                        sortedPeriods.add(newPeriod)

                        // Log.d(TAG_TIME_HELPER, "Объект == $newPeriod \n" +
                           //     "Открытие  = $dateFromStringOpen, закрытие == $dateFromStringClose")
                    }

                    /*Log.d(TAG_TIME_HELPER, "Хочу удалить: ${sortedMapOfOpensLongToString[key]}")

                    Log.d(TAG_TIME_HELPER, "Мапа до удаления: ${sortedMapOfOpensLongToString}")

                    sortedMapOfOpensLongToString.remove(key)

                    Log.d(TAG_TIME_HELPER, "Мапа после удаления: ${sortedMapOfOpensLongToString}")*/

                }
            }
        }

        // Log.d(TAG_TIME_HELPER, "Итоговый результат: $sortedPeriods")

        return sortedPeriods
    }
}