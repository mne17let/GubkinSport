package com.gubkinsport.data.models.sport_objects.ui_format

data class UiPeriod(
    val longTimeOpen: Long,
    val longTimeClose: Long,
    val open: String,
    val close: String,
    val name: String,

    val openMinute: Int,
    val openHour: Int,
    val openDayOfMonth: Int,
    val openMonth: Int,
    val openYear: Int,


    val closeMinute: Int,
    val closeHour: Int,
    val closeDayOfMonth: Int,
    val closeMonth: Int,
    val closeYear: Int
)