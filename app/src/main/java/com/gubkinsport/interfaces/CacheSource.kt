package com.gubkinsport.interfaces

import com.gubkinsport.data.models.people.StudentModelRoom

interface CacheSource {
    suspend fun saveNewStudentData(studentModelRoom: StudentModelRoom)

    suspend fun getStudentData(id: String): StudentModelRoom

    suspend fun deleteAll()
}