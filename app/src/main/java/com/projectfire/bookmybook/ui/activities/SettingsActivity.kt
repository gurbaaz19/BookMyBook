package com.projectfire.bookmybook.ui.activities

import android.os.Bundle
import android.view.View
import com.projectfire.bookmybook.R
import com.projectfire.bookmybook.models.User

class SettingsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mUserDetails: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

//        setupActionBar()
//
//        tv_edit.setOnClickListener(this)
//        btn_logout.setOnClickListener(this)
    }

//    private fun setupActionBar() {                       // Remember this method
//        setSupportActionBar(toolbar_settings_activity)
//
//        val actionBar = supportActionBar
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true)
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
//        }
//
//        toolbar_settings_activity.setNavigationOnClickListener { onBackPressed() }
//    }
//
//    private fun getUserDetails() {
//        showProgressDialog(resources.getString(R.string.please_wait))
//
//        FirestoreClass().getUserDetails(this@SettingsActivity)
//    }
//
//    fun userDetailsSuccess(user: User) {
//
//        mUserDetails = user
//
//        hideProgressDialog()
//
//        GlideLoader(this@SettingsActivity).loadUserPicture(user.image, iv_user_photo)
//        tv_name.text = "${user.firstName} ${user.lastName}"
//        tv_email.text = user.email
//        tv_gender.text = user.gender
//        tv_mobile_number.text = "${user.mobile}"
//    }
//
//    override fun onResume() {
//        super.onResume()
//        getUserDetails()
//    }
//
    override fun onClick(view: View?) {
//        if (view != null) {
//            when (view.id) {
//                R.id.btn_logout -> {
//                    FirebaseAuth.getInstance().signOut()
//                    val intent = Intent(this@SettingsActivity, LoginActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                    startActivity(intent)
//                    finish()
//                }
//
//                R.id.tv_edit -> {
//                    val intent = Intent(this@SettingsActivity, UserProfileActivity::class.java)
//                    intent.putExtra(Constants.EXTRA_USER_DETAILS, mUserDetails)
//                    startActivity(intent)
//                }
//            }
//        }
    }
}