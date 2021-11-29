package com.gubkinsport.fragment_bookings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gubkinsport.data.RepositoryImplementation
import com.gubkinsport.data.models.people.Booking
import com.gubkinsport.data.models.people.StudentModel
import com.gubkinsport.interfaces.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookingsViewModel(application: Application): AndroidViewModel(application) {

    private val repository: Repository

    init {
        repository = RepositoryImplementation(getApplication())
    }

    fun saveProfileData(studentModel: StudentModel){
        viewModelScope.launch {
            studentModel.mapForRoom()?.let { repository.saveStudent(it) }
        }
    }

    fun getProfileData(id: String){
        viewModelScope.launch {
            repository.getStudent(id)
        }
    }

    fun deleteCache(){
        repository
    }

    fun sendBooking(newBooking: Booking){
        viewModelScope.launch{
            repository.sendBooking(newBooking)
        }
    }

    fun deleteBooking(bookingToDelete: Booking){
        viewModelScope.launch{
            repository.deleteBooking(bookingToDelete)
        }
    }
}