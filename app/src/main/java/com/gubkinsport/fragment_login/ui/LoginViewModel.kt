package com.gubkinsport.fragment_login.ui

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gubkinsport.BaseViewModel
import com.gubkinsport.data.RepositoryImplementation
import com.gubkinsport.data.models.people.Booking
import com.gubkinsport.data.models.people.StudentModel
import com.gubkinsport.interfaces.Repository
import kotlinx.coroutines.launch

class LoginViewModel(
    app: Application, repository: Repository
): BaseViewModel(app, repository) {

    private val TAG_LOGIN_VIEWMODEL = "TAG_LOGIN_VIEWMODEL"

    val stateLogin = MutableLiveData<LoginState>()

    val saveStudentProfileLiveData = MutableLiveData<Boolean>()

    fun saveProfileData(studentModel: StudentModel, source: String){
        viewModelScope.launch {
            Log.d(TAG_LOGIN_VIEWMODEL, "Вызвана вьюмодель логина. SaveProfileData. Source == $source")
            studentModel.mapForRoom()?.let { repository.saveStudent(it) }
            Toast.makeText(getApplication(), "Вход выполнен", Toast.LENGTH_LONG).show()
            stateLogin.value = LoginState.Success()
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

    sealed class LoginState(){
        class Success(): LoginState()
        class Error(): LoginState()
    }
}