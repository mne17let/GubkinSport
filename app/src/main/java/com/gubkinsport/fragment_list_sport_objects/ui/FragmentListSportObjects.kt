package com.gubkinsport.fragment_list_sport_objects.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.gubkinsport.MainActivity
import com.gubkinsport.R
import com.gubkinsport.authentication.FirebaseAuthenticationHelper
import com.gubkinsport.data.models.people.StudentModel
import com.gubkinsport.data.models.sport_objects.ui_format.UiSportObject
import com.gubkinsport.fragment_list_sport_objects.helpers.GetProfileListener_FB
import com.gubkinsport.fragment_list_sport_objects.helpers.SportObjectsListListener_FB

class FragmentListSportObjects: Fragment(R.layout.fragment_list_sport_objects),
    ListSportObjectsAdapter.OnClickListener {

    // Тег для логов
    private val TAG_FRAGMENT = "FragListSportObjects"

    // Firebase Auth
    private val authenticationHelper = FirebaseAuthenticationHelper()
    private var currentUser: FirebaseUser? = null

    // Firebase
    private val fDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val sportObjectsReference: DatabaseReference = fDatabase.getReference("sport_objects_data")
    private val userReference = fDatabase.getReference("people")

    // ProfileData
    private var currentUserProfileData: StudentModel? = null


    private lateinit var recycler: RecyclerView
    private lateinit var adapter: ListSportObjectsAdapter

    // For show load
    private lateinit var progressBar: ProgressBar
    private lateinit var mainContent: LinearLayout

    // ActionBar
    private lateinit var helloTextView: TextView
    private lateinit var buttonOpenProfile: Button

    private val list = mutableListOf<UiSportObject>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val listOfNames = mutableListOf<String>()
        for(i in list){
            i.name.let { listOfNames.add(it) }
        }

        Log.d(TAG_FRAGMENT, "Список в onViewCreated: ${listOfNames}")


        recycler = view.findViewById(R.id.id_recyclerview_list_sport_objects)
        progressBar = view.findViewById(R.id.id_progress_bar_list_sport_objects)
        mainContent = view.findViewById(R.id.id_main_content_fragment_list_sport_objects)
        helloTextView = view.findViewById(R.id.id_textview_hello_name)
        buttonOpenProfile = view.findViewById(R.id.id_button_open_profile)

        setRecyclerView()
        setDataSportObjectsChanged()
        setHelloTextView()
        setOpenProfileButton()
    }

    private fun setRecyclerView(){
        recycler.layoutManager = LinearLayoutManager(requireContext())
        adapter = ListSportObjectsAdapter(this)
        recycler.adapter = adapter
    }

    private fun setNewListForAdapter(){
        adapter.setList(list)
    }

    private fun setOpenProfileButton(){
        buttonOpenProfile.setOnClickListener {
            (activity as MainActivity).showProfileFragment()
            // Toast.makeText(requireContext(), "Когда-нибудь вам будут доступны настройки", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setHelloTextView(){
        currentUser = authenticationHelper.getCurrentUser()
        val checkUser = currentUser

        if(checkUser != null){
            val profileReference = userReference.child(checkUser.uid)
            val profileListener = GetProfileListener_FB(object : ProfileListenerCallBack {
                override fun onSuccess(data: StudentModel) {
                    currentUserProfileData = data
                    val newHelloText = "Привет, ${data.firstName}"
                    helloTextView.text = newHelloText
                }

                override fun onError() {
                }
            })

            profileReference.addValueEventListener(profileListener)
        } else{
            helloTextView.text = "Привет, незнакомец!"
        }
    }

    private fun setDataSportObjectsChanged(){
        val listener = SportObjectsListListener_FB()

        sportObjectsReference.addValueEventListener(listener)

        listener.liveData.observe(viewLifecycleOwner) { newList ->

            Log.d(TAG_FRAGMENT, "Лайвдата передала: ${newList}")

            val listOfNames = mutableListOf<String>()
            for(i in list){
                i.name.let { listOfNames.add(it) }
            }

            Log.d(TAG_FRAGMENT, "Старый список после обновления лайвдаты: ${listOfNames}")
            list.clear()
            list.addAll(newList)

            val listOfNewNames = mutableListOf<String>()
            for(i in list){
                i.name.let { listOfNewNames.add(it) }
            }
            //Log.d(TAG_FRAGMENT, "Новый список после обновления лайвдаты: ${list}")
            setNewListForAdapter()
            Log.d(TAG_FRAGMENT, "Новый список после обновления лайвдаты: ${listOfNewNames}")

        }
    }

    private fun showLoad(){
        progressBar.visibility = View.VISIBLE
        mainContent.visibility = View.GONE
    }

    private fun showContent(){
        progressBar.visibility = View.GONE
        mainContent.visibility = View.VISIBLE
    }

    override fun onClick(sportObjectId: String) {
        //showLoad()
        Log.d(TAG_FRAGMENT, "Во фрагменте получено: $sportObjectId")

        val checkCurrentUser = currentUser

        if(checkCurrentUser != null){

            if(currentUserProfileData != null){
                (activity as MainActivity).showCheckInOrLoginFragment(
                    sportObjectId,
                    haveProfile = true,
                    haveAccount = true
                )
            } else{
                Toast.makeText(requireContext(), "Сначала заполните свой профиль в настройках", Toast.LENGTH_SHORT).show()
            }
        } else{
            (activity as MainActivity).showCheckInOrLoginFragment(
                sportObjectId,
                haveProfile = false,
                haveAccount = false
            )
        }
    }

    interface ProfileListenerCallBack{
        fun onSuccess(data: StudentModel)
        fun onError()
    }
}