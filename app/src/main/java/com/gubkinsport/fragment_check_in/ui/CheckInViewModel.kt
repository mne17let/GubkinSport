package com.gubkinsport.fragment_check_in.ui

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gubkinsport.BaseViewModel
import com.gubkinsport.data.RepositoryImplementation
import com.gubkinsport.data.models.people.Booking
import com.gubkinsport.data.models.people.StudentModel
import com.gubkinsport.interfaces.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CheckInViewModel(
    application: Application,
    repository: Repository
): BaseViewModel(application, repository) {

    val profileDataLiveData = MutableLiveData<StudentModel>()

    fun getProfileData(id: String){
        viewModelScope.launch {
            val result = repository.getStudent(id)
                profileDataLiveData.value = result.mapForUi()
        }
    }

    fun sendBooking(newBooking: Booking){
        viewModelScope.launch{
            repository.sendBooking(newBooking)
            Toast.makeText(getApplication(), "Заявка отправлена", Toast.LENGTH_SHORT).show()
        }
    }
}