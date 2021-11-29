package com.gubkinsport.data

import android.content.Context
import com.gubkinsport.data.cloud.CloudSourceImplementation
import com.gubkinsport.data.database.CacheSourceImplementation
import com.gubkinsport.data.database.CacheStudentDataBase
import com.gubkinsport.data.models.people.Booking
import com.gubkinsport.data.models.people.StudentModelRoom
import com.gubkinsport.interfaces.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositoryImplementation(private val context: Context): Repository {

    val cloud = CloudSourceImplementation()

    val dataBaseInstance = CacheStudentDataBase.getStudentDatabase(context)
    val dao = dataBaseInstance.studentDao()
    val cache = CacheSourceImplementation(dao)

    override suspend fun sendBooking(booking: Booking){
        withContext(Dispatchers.IO){
            cloud.sendBooking(booking)
        }
    }

    override suspend fun deleteBooking(bookingToDelete: Booking){
        withContext(Dispatchers.IO){
            cloud.deleteBooking(bookingToDelete)
        }
    }

    override suspend fun getStudent(id: String): StudentModelRoom {
        return cache.getStudentData(id)
    }

    override suspend fun saveStudent(newStudent: StudentModelRoom) {
        cache.saveNewStudentData(newStudent)
    }

    override suspend fun deleteAllCache() {
        cache.deleteAll()
    }
}