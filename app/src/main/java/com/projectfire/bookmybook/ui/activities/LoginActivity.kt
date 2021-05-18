package com.projectfire.bookmybook.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import com.projectfire.bookmybook.R
import com.projectfire.bookmybook.firestore.FirestoreClass
import com.projectfire.bookmybook.models.User
import com.projectfire.bookmybook.utilities.Constants
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        tv_forgot_password.setOnClickListener(this)
        btn_login.setOnClickListener(this)
        tv_register.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.tv_forgot_password -> {
                    val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
                    startActivity(intent)
                }

                R.id.btn_login -> {
                    logInRegisteredUser()
                }

                R.id.tv_register -> {
                    val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(et_l_email.text.toString().trim { it <= ' ' }) -> {
                showSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(et_l_password.text.toString().trim { it <= ' ' }) -> {
                showSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else -> {
                true
            }
        }
    }

    private fun logInRegisteredUser() {
        if (validateLoginDetails()) {
            //show progress dialog
            showProgressDialog(resources.getString(R.string.please_wait))

            //get input
            val email = et_l_email.text.toString().trim { it <= ' ' }
            val password = et_l_password.text.toString()

            //Log-in with FirebaseAuth

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    hideProgressDialog()

                    if (task.isSuccessful) {

                        FirestoreClass().getUserDetails(this@LoginActivity)

                } else {
                showSnackBar(task.exception!!.message.toString(), true)
            }
        }
    }
}

fun userLoggedInSuccess(user: User) {
    hideProgressDialog()

//    Logging as of now
//    Log.i("First Name: ", user.firstName)
//    Log.i("Last Name: ", user.lastName)
//    Log.i("Email: ", user.email)

    if (user.profileCompleted == 0) {
        val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
        intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
        startActivity(intent)
        finish()
    } else {
        startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
        finish()
    }
}
    override fun onBackPressed() {
        doubleBackToExit()
    }
}