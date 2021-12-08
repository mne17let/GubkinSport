package com.gubkinsport

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.gubkinsport.interfaces.Repository

open class BaseViewModel(val app: Application, val repository: Repository): AndroidViewModel(app) {
}