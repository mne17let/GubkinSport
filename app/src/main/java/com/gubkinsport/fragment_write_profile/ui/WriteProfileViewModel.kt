package com.gubkinsport.fragment_write_profile.ui

import android.app.Application
import androidx.lifecycle.*
import com.gubkinsport.BaseViewModel
import com.gubkinsport.data.RepositoryImplementation
import com.gubkinsport.data.models.people.Booking
import com.gubkinsport.data.models.people.StudentModel
import com.gubkinsport.interfaces.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WriteProfileViewModel(application: Application,
                            repository: Repository
): BaseViewModel(application, repository) {

    val saveStudentProfileLiveData = MutableLiveData<Boolean>()

    fun saveProfileData(studentModel: StudentModel){
        viewModelScope.launch {
            studentModel.mapForRoom()?.let { repository.saveStudent(it) }
        }
    }
}