package com.gubkinsport.fragment_list_sport_objects.helpers

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.gubkinsport.data.models.people.StudentModel
import com.gubkinsport.data.models.sport_objects.ui_format.UiSportObject
import com.gubkinsport.fragment_list_sport_objects.ui.FragmentListSportObjects

class GetProfileListener_FB: ValueEventListener {

    private val TAG_USER_PROFILE_LISTENER = "MyFBProfileListener"

    val liveData: MutableLiveData<StudentModel> = MutableLiveData()

    override fun onDataChange(snapshot: DataSnapshot) {
        val newStudentProfile = snapshot.getValue(StudentModel::class.java)
        if (newStudentProfile != null){
            liveData.value = newStudentProfile

            Log.d(TAG_USER_PROFILE_LISTENER, "Пришёл объект: $newStudentProfile")
        } else{
            liveData.value = null
        }
    }

    override fun onCancelled(error: DatabaseError) {
        liveData.value = null
    }
}