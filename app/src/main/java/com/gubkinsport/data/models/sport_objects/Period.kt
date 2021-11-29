package com.gubkinsport.data.models.sport_objects

// Модель временного промежутка, когда спортивный объект доступен для посещения

data class Period(
    val open: String? = null,
    val close: String? = null
)