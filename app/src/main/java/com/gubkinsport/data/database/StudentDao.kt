package com.gubkinsport.data.database

import androidx.room.*
import com.gubkinsport.data.models.people.StudentModelRoom

@Dao
interface StudentDao {

    @Insert() // onConflict = OnConflictStrategy.IGNORE
    suspend fun addStudent(newStudent: StudentModelRoom)

    @Query("SELECT * FROM student_table WHERE id = :newId")
    suspend fun getStudent(newId: String): StudentModelRoom

    @Query("DELETE FROM student_table")
    suspend fun deleteAll()
}