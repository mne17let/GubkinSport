package com.gubkinsport.fragment_bookings

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gubkinsport.BaseViewModel
import com.gubkinsport.data.RepositoryImplementation
import com.gubkinsport.data.models.people.Booking
import com.gubkinsport.data.models.people.StudentModel
import com.gubkinsport.interfaces.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookingsViewModel(
    application: Application,
    repository: Repository
): BaseViewModel(application, repository) {

    fun deleteBooking(bookingToDelete: Booking){
        viewModelScope.launch{
            repository.deleteBooking(bookingToDelete)
            Log.d("TAG_LOGIN_VIEWMODEL", "Выполнен deleteBooking в BookingViewModel")
        }
    }
}