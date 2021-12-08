package com.gubkinsport

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gubkinsport.interfaces.Repository

class MyViewModelFactory(val app: Application, val repository: Repository): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Application::class.java, Repository::class.java)
            .newInstance(app, repository)
    }

}