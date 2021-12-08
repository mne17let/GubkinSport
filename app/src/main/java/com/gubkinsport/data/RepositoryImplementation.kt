package com.gubkinsport.data

import android.content.Context
import android.util.Log
import com.gubkinsport.data.cloud.CloudSourceImplementation
import com.gubkinsport.data.database.CacheSourceImplementation
import com.gubkinsport.data.database.CacheStudentDataBase
import com.gubkinsport.data.models.people.Booking
import com.gubkinsport.data.models.people.StudentModelRoom
import com.gubkinsport.interfaces.CacheSource
import com.gubkinsport.interfaces.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositoryImplementation(private val context: Context, private val cache: CacheSource) : Repository {

    val cloud = CloudSourceImplementation()

    override suspend fun sendBooking(booking: Booking) {
        val a = cloud.sendBooking(booking)
    }

    override suspend fun deleteBooking(bookingToDelete: Booking) {
        val a = cloud.deleteBooking(bookingToDelete)
    }

    override suspend fun getStudent(id: String): StudentModelRoom {
        return cache.getStudentData(id)
    }

    override suspend fun saveStudent(newStudent: StudentModelRoom) {
        val a = cache.saveNewStudentData(newStudent)
        Log.d("TAG_LOGIN_VIEWMODEL", "Вызван saveStudent в репозитории")
    }

    override suspend fun deleteAllCache() {
        val a = cache.deleteAll()
        Log.d("TAG_LOGIN_VIEWMODEL", "Вызван deleteAllCache в репозитории")
    }

    sealed class RepositoryAnswer {
        class Success : RepositoryAnswer()
        class Error : RepositoryAnswer()
    }
}