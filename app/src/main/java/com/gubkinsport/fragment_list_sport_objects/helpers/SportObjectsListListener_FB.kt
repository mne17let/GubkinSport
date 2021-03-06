package com.gubkinsport.fragment_list_sport_objects.helpers

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.gubkinsport.data.models.sport_objects.SportObjectModel
import com.gubkinsport.data.models.sport_objects.ui_format.UiSportObject

// Слушатель изменений в списке спортивных объектов в базе данных

class SportObjectsListListener_FB : ValueEventListener {

    // Тег для логов
    private val TAG_VALUEEVENTLISTENER = "MyObjectsListListener"
    private var list = mutableListOf<UiSportObject>()
    val liveData: MutableLiveData<MutableList<UiSportObject>> = MutableLiveData()

    override fun onDataChange(snapshot: DataSnapshot) {

        Log.d(TAG_VALUEEVENTLISTENER, "Ответ сразу после приёма: ${snapshot}")

        list.clear()

        val helpList = mutableListOf<SportObjectModel>()

        for (oneSnapshot: DataSnapshot in snapshot.children) {
            val newSportObject: SportObjectModel? =
                oneSnapshot.getValue(SportObjectModel::class.java)

            if (newSportObject != null) {
                helpList.add(newSportObject)
            }

            //Log.d(TAG_VALUEEVENTLISTENER, "Ответ: ${oneSnapshot}")

            //Log.d(TAG_VALUEEVENTLISTENER, "Мой объект: ${newSportObject}")
        }

        val listOfNames = mutableListOf<String>()
        for(i in helpList){
            i.name?.let { listOfNames.add(it) }
        }

        for(crudeSportObject in helpList){
            val pureQualityObject = crudeSportObject.mapForUi()

            Log.d(TAG_VALUEEVENTLISTENER, "Смапил объект: ${pureQualityObject}")

            if (pureQualityObject != null){
                list.add(pureQualityObject)
            }
        }

        Log.d(TAG_VALUEEVENTLISTENER, "Список перед обновлением livedata: ${list}")

        liveData.value = list
    }

    override fun onCancelled(error: DatabaseError) {
        Log.d(TAG_VALUEEVENTLISTENER, "Код ошибки: ${error.code}. Ошибка: ${error.message}")
    }
}