package com.projectfire.bookmybook.ui.activities

import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import com.projectfire.bookmybook.R
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        setupActionBar()

        btn_reset_password.setOnClickListener {
            val email = et_recovery_email.text.toString().trim { it <= ' ' }

            if (email.isEmpty()) {
                showSnackBar(resources.getString(R.string.err_msg_enter_email), true)
            } else {
                showProgressDialog(resources.getString(R.string.please_wait))

                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->

                        hideProgressDialog()

                        if (task.isSuccessful) {
                            showSnackBar(
                                resources.getString(R.string.email_sent_success),
                                false
                            )

                            @Suppress("DEPRECATION")
                            Handler().postDelayed(
                                {
                                    finish()
                                },
                                1500
                            )

                        } else {
                            showSnackBar(task.exception!!.message.toString(), true)
                        }
                    }
            }

        }
    }

    private fun setupActionBar() {                       // Remember this method
        setSupportActionBar(toolbar_forgot_password_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = ""
        }

        toolbar_forgot_password_activity.setNavigationOnClickListener { onBackPressed() }
    }
}