package com.gubkinsport.data.cloud

import com.gubkinsport.data.models.people.Booking
import retrofit2.Call
import retrofit2.http.*

interface FirebaseApi {

    @GET("https://gubkin-sport-default-rtdb.firebaseio.com/sport_objects_data/id_1/days/20-10-2021/period_1/close.json")
    fun getAuthToken(@Query("auth") token: String): Call<String>

    @PUT("https://gubkin-sport-default-rtdb.firebaseio.com/people/{user_id}/listOfBookings/{openTimeString}.json")
    fun sendNewBooking(
        @Path("user_id") user_id: String,
        @Path("openTimeString") openTimeString: String,
        @Body booking: Booking
    ): Call<Booking>

    @DELETE("https://gubkin-sport-default-rtdb.firebaseio.com/people/{user_id}/listOfBookings/{openTimeString}.json")
    fun deleteBooking(
        @Path("user_id") user_id: String,
        @Path("openTimeString") openTimeString: String,
    ): Call<Booking>
}