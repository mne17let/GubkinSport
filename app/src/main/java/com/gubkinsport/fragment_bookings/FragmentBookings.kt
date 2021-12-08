package com.gubkinsport.fragment_bookings

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.gubkinsport.MainActivity
import com.gubkinsport.MyViewModelFactory
import com.gubkinsport.R
import com.gubkinsport.data.models.people.Booking
import com.gubkinsport.data.models.people.StudentModel

class FragmentBookings : Fragment(R.layout.fragment_bookings),
    BookingsAdapter.OnClickDeleteBookingButtonInProfileBookingsListListener {

    private val TAG_FRAGMENT_BOOKINGS = "MyBookingsFragment"

    private lateinit var viewModel: BookingsViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var yourBookingsTextView: TextView

    private lateinit var emptyBookingsText: TextView
    private lateinit var letsGoButton: Button
    private lateinit var letsGoLinearLayout: LinearLayout

    private val bookingsAdapter = BookingsAdapter(this)

    private val fDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val userReference = fDatabase.getReference("people")

    private val currentUser = FirebaseAuth.getInstance().currentUser
    private var userInfo: StudentModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel =
            MyViewModelFactory(requireActivity().application).create(BookingsViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.id_bookings_recycler)
        yourBookingsTextView = view.findViewById(R.id.id_your_bookings_text)
        emptyBookingsText = view.findViewById(R.id.id_empty_bookings_text)
        letsGoButton = view.findViewById(R.id.id_lets_go_to_sport_objects_list)
        letsGoLinearLayout = view.findViewById(R.id.id_lets_go_linear)

        setUpBookingsRecycler()
        setUpLoadBookings()
        setUpLetsGoButton()
    }

    private fun setUpBookingsRecycler() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = bookingsAdapter
    }

    private fun setUpLetsGoButton() {
        letsGoButton.setOnClickListener {
            (activity as MainActivity).showSOListAfterLetsGoButtonClicked()
        }
    }

    private fun setNewBookingsList() {
        val currentUserInfo = userInfo
        val currentBookings = currentUserInfo?.listOfBookings
        if (currentBookings != null) {
            val newList = mutableListOf<Booking>()
            for (booking in currentBookings) {
                newList.add(booking.value)
            }
            bookingsAdapter.setNewList(newList)
            showNotEmptyBookingsListState()
        } else {
            showEmptyBookingsListState()
        }
    }

    private fun showEmptyBookingsListState() {
        letsGoLinearLayout.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        yourBookingsTextView.visibility = View.GONE
    }

    private fun showNotEmptyBookingsListState() {
        letsGoLinearLayout.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        yourBookingsTextView.visibility = View.VISIBLE
    }

    private fun setUpLoadBookings() {
        if (currentUser != null) {
            val userRef = userReference.child(currentUser.uid)

            userRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    userInfo = snapshot.getValue(StudentModel::class.java)
                    setNewBookingsList()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG_FRAGMENT_BOOKINGS, "Бронирования не получены")

                    yourBookingsTextView.text = "Ошибка загрузки бронирований"
                }

            })
        }
    }

    override fun onClick(bookingForDelete: Booking) {
        viewModel.deleteBooking(bookingForDelete)

        val currentList = bookingsAdapter.getListForDeleteItem()
        val mutableListOfBookings = mutableListOf<Booking>()
        mutableListOfBookings.addAll(currentList)
        mutableListOfBookings.remove(bookingForDelete)

        if (mutableListOfBookings.size == 0) {
            showEmptyBookingsListState()
        }

        bookingsAdapter.setNewList(mutableListOfBookings)
    }
}