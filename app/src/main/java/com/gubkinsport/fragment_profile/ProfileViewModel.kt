package com.gubkinsport.fragment_profile

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.gubkinsport.BaseViewModel
import com.gubkinsport.data.RepositoryImplementation
import com.gubkinsport.data.models.people.Booking
import com.gubkinsport.data.models.people.StudentModel
import com.gubkinsport.interfaces.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application,
                       repository: Repository
): BaseViewModel(application, repository) {

    val saveStudentProfileLiveData = MutableLiveData<Boolean>()

    fun deleteCache(){
        viewModelScope.launch{
            repository.deleteAllCache()
        }
    }
}

