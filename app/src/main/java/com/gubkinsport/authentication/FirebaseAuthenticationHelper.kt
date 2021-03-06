package com.gubkinsport.authentication

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.gubkinsport.fragment_login.ui.LoginFragment
import java.lang.Exception

class FirebaseAuthenticationHelper(source: String) {

    private var callback: LoginFragment.AuthenticationCallback? = null

    private val firebaseAuth = FirebaseAuth.getInstance()
    private var currentUser: FirebaseUser? = firebaseAuth.currentUser

    init {
        Log.d("TAG_LOGIN_VIEWMODEL", "Создание FirebaseAuthenticationHelper. source == $source")
    }

    fun createNewUser(email: String, pass: String, newCallback: LoginFragment.AuthenticationCallback){
        Log.d("TAG_LOGIN_VIEWMODEL", "Вызван createNewUser в FirebaseAuthenticationHelper")

        callback = newCallback

        val task: Task<AuthResult> = firebaseAuth.createUserWithEmailAndPassword(email, pass)
        task.addOnCompleteListener(completeListener)
    }

    private val completeListener = object : OnCompleteListener<AuthResult>{

        override fun onComplete(task: Task<AuthResult>) {
            if(task.isSuccessful){
                currentUser = firebaseAuth.currentUser
                currentUser?.let { callback?.onSuccess(it) }

                callback = null
            } else{
                val error: Exception? = task.exception
                callback?.onError(error.toString())

                callback = null
            }
        }

    }

    fun signIn(email: String, pass: String, newCallback: LoginFragment.AuthenticationCallback){
        Log.d("TAG_LOGIN_VIEWMODEL", "Вызван signIn в FirebaseAuthenticationHelper")

        callback = newCallback

        val task = firebaseAuth.signInWithEmailAndPassword(email, pass)
        task.addOnCompleteListener{ completeTask ->
            if (completeTask.isSuccessful){
                currentUser = firebaseAuth.currentUser
                currentUser?.let { callback?.onSuccess(it) }

                callback = null
            } else{
                val error: Exception? = task.exception
                callback?.onError(error.toString())

                callback = null
            }
        }
    }

    fun signOut(){
        firebaseAuth.signOut()
    }
}