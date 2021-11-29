package com.gubkinsport.data.database

import com.gubkinsport.data.models.people.StudentModelRoom
import com.gubkinsport.interfaces.CacheSource

class CacheSourceImplementation(private val dao: StudentDao): CacheSource {

    override suspend fun saveNewStudentData(studentModelRoom: StudentModelRoom) {
        dao.addStudent(studentModelRoom)
    }

    override suspend fun getStudentData(id: String): StudentModelRoom {
        return dao.getStudent(id)
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }
}