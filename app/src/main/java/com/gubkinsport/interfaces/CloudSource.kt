package com.gubkinsport.interfaces

import com.gubkinsport.data.models.people.Booking

interface CloudSource {
    fun sendBooking(newBooking: Booking)

    fun getBooking()

    fun deleteBooking(booking: Booking)
}