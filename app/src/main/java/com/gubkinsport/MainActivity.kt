package com.gubkinsport

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentManager
import com.gubkinsport.data.RepositoryImplementation
import com.gubkinsport.data.database.CacheSourceImplementation
import com.gubkinsport.data.database.CacheStudentDataBase
import com.gubkinsport.fragment_bookings.FragmentBookings
import com.gubkinsport.fragment_check_in.ui.FragmentCheckIn
import com.gubkinsport.fragment_list_sport_objects.ui.FragmentListSportObjects
import com.gubkinsport.fragment_login.ui.LoginFragment
import com.gubkinsport.fragment_profile.FragmentProfile
import com.gubkinsport.fragment_write_profile.ui.WriteProfileFragment
import com.gubkinsport.interfaces.Repository

class MainActivity : AppCompatActivity() {

    // Тег для логов
    private val TAG_ACTIVITY = "MyMainActivity"

    lateinit var repository: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showStartFragment()
        initRepo()
    }

    fun initRepo(){
        val dataBaseInstance = CacheStudentDataBase.getStudentDatabase(applicationContext)
        val dao = dataBaseInstance.studentDao()
        val cache = CacheSourceImplementation(dao)
        repository = RepositoryImplementation(applicationContext, cache)
    }

    fun showCheckInOrLoginFragment(
        sportObjectId: String?,
        haveProfile: Boolean,
        haveAccount: Boolean
    ) {

        if (!haveAccount) {
            showLoginFragment()
        } else {
            if (sportObjectId != null) {
                showCheckInFragment(sportObjectId)
            }
        }
    }

    fun showSportObjectsListAfterLogIn() {
        repeat(supportFragmentManager.backStackEntryCount) {
            supportFragmentManager.popBackStack()
        }

        showSportObjectsList()
    }

    private fun showStartFragment() {
        showSportObjectsList()
    }

    private fun showSportObjectsList() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.id_frame_container, FragmentListSportObjects())
            .commit()
    }

    private fun showCheckInFragment(id: String) {
        val newArgs = Bundle()
        newArgs.putString("id", id)

        Log.d(TAG_ACTIVITY, "В активити получено: $id")

        val newFragment = FragmentCheckIn()
        newFragment.arguments = newArgs

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.id_frame_container, newFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showLoginFragment() {
        val newFragment = LoginFragment()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.id_frame_container, newFragment)
            .addToBackStack(null)
            .commit()
    }

    fun showFirstProfileSettings() {
        val newFragment = WriteProfileFragment()

        repeat(supportFragmentManager.backStackEntryCount) {
            supportFragmentManager.popBackStack()
        }

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.id_frame_container, newFragment)
            .commit()
    }

    fun showProfileFragment() {
        val newProfileFragment = FragmentProfile()

        supportFragmentManager.beginTransaction()
            .replace(R.id.id_frame_container, newProfileFragment)
            .addToBackStack(null)
            .commit()
    }

    fun showBookingsFragment() {
        val newBookingFragment = FragmentBookings()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.id_frame_container, newBookingFragment)
            .addToBackStack(null)
            .commit()
    }

    fun showSportObjectsListAfterSignOut() {
        val newFragment = FragmentListSportObjects()

        repeat(supportFragmentManager.backStackEntryCount) {
            supportFragmentManager.popBackStack()
        }

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.id_frame_container, newFragment)
            .commit()
    }

    fun showMainPageAfterSendBooking() {

        repeat(supportFragmentManager.backStackEntryCount) {
            supportFragmentManager.popBackStack()
        }

        showSportObjectsList()
    }

    fun showSOListAfterLetsGoButtonClicked() {
        repeat(supportFragmentManager.backStackEntryCount) {
            supportFragmentManager.popBackStack()
        }

        showSportObjectsList()
    }
}