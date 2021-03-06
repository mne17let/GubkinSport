package com.gubkinsport.fragment_login.ui

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.gubkinsport.MainActivity
import com.gubkinsport.MyViewModelFactory
import com.gubkinsport.R
import com.gubkinsport.authentication.FirebaseAuthenticationHelper
import com.gubkinsport.authentication.ValidationHelper
import com.gubkinsport.data.models.people.StudentModel

class LoginFragment: Fragment(R.layout.fragment_login) {

    private var areLoginViews = true

    private var authenticationHelper: FirebaseAuthenticationHelper? = FirebaseAuthenticationHelper("login_fragment")

    private var valueEventListener: ValueEventListener? = null

    private lateinit var lightTypeface: Typeface
    private lateinit var boldTypeface: Typeface

    private lateinit var signInTextView: TextView
    private lateinit var signUpTextView: TextView
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passEditText: TextInputEditText
    private lateinit var emailTextInputLayout: TextInputLayout
    private lateinit var passTextInputLayout: TextInputLayout
    private lateinit var signInButton: Button
    private lateinit var signUpButton: Button
    private lateinit var signBox: LinearLayout
    private lateinit var progressBar: ProgressBar

    private var viewModel: LoginViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TAG_LOGIN_VIEWMODEL", "???????????????? onCreate ?? LoginFragment")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TAG_LOGIN_VIEWMODEL", "???????????????? onDestroy ?? LoginFragment")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        lightTypeface = Typeface.createFromAsset(resources.assets, "nunito_extralight.ttf")
        boldTypeface = Typeface.createFromAsset(resources.assets, "nunito_bold.ttf")

        viewModel = MyViewModelFactory(requireActivity().application, (activity as MainActivity).repository).create(LoginViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signInTextView = view.findViewById(R.id.id_sign_in_textview)
        signUpTextView = view.findViewById(R.id.id_sign_up_textview)
        emailEditText = view.findViewById(R.id.id_edittext_email)
        passEditText = view.findViewById(R.id.id_edittext_pass)
        signInButton = view.findViewById(R.id.id_sign_in_button)
        signUpButton = view.findViewById(R.id.id_sign_up_button)
        signBox = view.findViewById(R.id.id_login_and_reg_box)
        emailTextInputLayout = view.findViewById(R.id.id_textinputlayout_email)
        passTextInputLayout = view.findViewById(R.id.id_textinputlayout_pass)
        progressBar = view.findViewById(R.id.id_login_progressbar)

        setViews()
        setLiveData()
    }

    private fun setViews(){
        setTextSignTextView()
        setSignInButton()
        setSignUpButton()
        setEditTexts()
    }

    private fun setLiveData(){

        valueEventListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val studentData = snapshot.getValue(StudentModel::class.java)
                if (studentData != null){
                    viewModel?.saveProfileData(studentData, "Login_Fragment")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(),
                    "???????? ????????????????, ???? ???????? ???????????? ???? ??????????????",
                    Toast.LENGTH_LONG).show()
            }

        }

        viewModel?.stateLogin?.observe(viewLifecycleOwner){
            if (it is LoginViewModel.LoginState.Success){
                authenticationHelper = null
                FirebaseDatabase.getInstance().getReference("people").removeEventListener(
                    valueEventListener as ValueEventListener
                )
                valueEventListener = null
                viewModel = null
                
                Log.d("TAG_LOGIN_VIEWMODEL", "?????????????????? FirebaseAuthenticationHelper")

                progressBar.visibility = View.GONE
                (activity as MainActivity).showSportObjectsListAfterLogIn()
            }
        }
    }

    private fun setTextSignTextView(){
        signInTextView.setOnClickListener{
            showSignIn()
        }

        signInTextView.setTypeface(boldTypeface, Typeface.BOLD)

        signUpTextView.setOnClickListener {
            showSignUp()
        }
    }

    private fun showSignIn(){

        if(areLoginViews){
            return
        }

        areLoginViews = true

        signInTextView.setTextSize(28f)
        signUpTextView.setTextSize(16f)

        signInTextView.setTypeface(boldTypeface, Typeface.BOLD)
        signUpTextView.setTypeface(lightTypeface, Typeface.NORMAL)

        signInTextView.setTextColor(resources.getColor(R.color.green))
        signUpTextView.setTextColor(resources.getColor(R.color.white))

        signInButton.visibility = View.VISIBLE
        signUpButton.visibility = View.GONE

        val imm: InputMethodManager = requireActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(signInTextView.windowToken, 0)
    }

    private fun showSignUp(){
        if(!areLoginViews){
            return
        }

        areLoginViews = false

        signInTextView.setTextSize(16f)
        signUpTextView.setTextSize(28f)

        signInTextView.setTextColor(resources.getColor(R.color.white))
        signUpTextView.setTextColor(resources.getColor(R.color.green))

        signUpTextView.setTypeface(boldTypeface, Typeface.BOLD)
        signInTextView.setTypeface(lightTypeface, Typeface.NORMAL)

        signInButton.visibility = View.GONE
        signUpButton.visibility = View.VISIBLE

        val imm: InputMethodManager = requireActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(signUpTextView.windowToken, 0)
    }

    private fun showLoad(){
        signBox.visibility = View.GONE

        progressBar.visibility = View.VISIBLE
    }

    private fun showErrorLoad(){
        signBox.visibility = View.VISIBLE

        progressBar.visibility = View.GONE
    }

    private fun setSignInButton(){
        signInButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val pass = passEditText.text.toString()

            val helper = ValidationHelper()
            val trueEmail = helper.checkEmail(email)
            val truePass = helper.checkPass(pass)

            if (!trueEmail || !truePass){
                emailTextInputLayout.error = helper.getEmailError(email)
                passTextInputLayout.error = helper.getPassError(pass)
                return@setOnClickListener
            } else{
                showLoad()

                authenticationHelper?.signIn(email, pass, object : AuthenticationCallback {
                    override fun onSuccess(data: FirebaseUser) {
                        val currentUser = FirebaseAuth.getInstance().currentUser
                        val currentVEL = valueEventListener
                        if (currentUser != null && currentVEL != null){
                            FirebaseDatabase.getInstance().getReference("people").child(currentUser.uid)
                                .addValueEventListener(currentVEL)
                        }
                    }

                    override fun onError(error: String) {
                        Toast.makeText(requireContext(), "???????????? ??????????: $error", Toast.LENGTH_LONG).show()
                        showErrorLoad()
                    }
                })
            }

        }
    }

    private fun setSignUpButton(){
        signUpButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val pass = passEditText.text.toString()

            val helper = ValidationHelper()
            val trueEmail = helper.checkEmail(email)
            val truePass = helper.checkPass(pass)

            if (!trueEmail || !truePass){
                emailTextInputLayout.error = helper.getEmailError(email)
                passTextInputLayout.error = helper.getPassError(pass)
                return@setOnClickListener
            } else{
                showLoad()

                authenticationHelper?.createNewUser(email, pass, object : AuthenticationCallback {
                    override fun onSuccess(data: FirebaseUser) {
                        Toast.makeText(requireContext(), "?????????????????????? ?????????????????? ??????????????", Toast.LENGTH_LONG).show()
                        progressBar.visibility = View.GONE
                        (activity as MainActivity).showFirstProfileSettings()
                    }

                    override fun onError(error: String) {
                        Toast.makeText(requireContext(), "???????????? ??????????????????????: $error", Toast.LENGTH_LONG).show()
                        showErrorLoad()
                    }
                })
            }
        }
    }

    private fun setEditTexts(){

        emailEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                emailTextInputLayout.error = ""
            }
        })

        passEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                passTextInputLayout.error = ""
            }
        })
    }

    interface AuthenticationCallback{
        fun onSuccess(data: FirebaseUser)

        fun onError(error: String)
    }
}