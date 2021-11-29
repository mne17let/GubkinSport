package com.gubkinsport.interfaces

import com.gubkinsport.data.models.people.Booking
import com.gubkinsport.data.models.people.StudentModelRoom

interface Repository {
    suspend fun getStudent(id: String): StudentModelRoom

    suspend fun saveStudent(newStudent: StudentModelRoom)

    suspend fun deleteAllCache()

    suspend fun sendBooking(booking: Booking)

    suspend fun deleteBooking(bookingToDelete: Booking)
}