package com.projectfire.bookmybook.utilities
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {
    const val RC_GOOGLE_SIGN_IN = 4926

    const val USERS: String = "users"
    const val APP_PREFERENCES: String = "ShoppingAppPrefs"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"
    const val EXTRA_USER_DETAILS: String = "extra_user_details"
    const val READ_STORAGE_PERMISSION_CODE = 2
    const val PICK_IMAGE_REQUEST_CODE = 1

    const val FIRST_NAME: String = "firstName"
    const val LAST_NAME: String = "lastName"
    const val MOBILE: String = "mobile"
    const val GENDER: String = "gender"
    const val MALE: String = "male"
    const val FEMALE: String = "female"
    const val IMAGE: String = "image"
    const val USER_PROFILE_IMAGE: String = "User_Profile_Image"

    const val COMPLETE_PROFILE = "profileCompleted"


    fun showImageChooser(activity: Activity) {
        // intent to launch image selection from phone
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        //Launching the selection prompt
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    fun getFileExtension(activity: Activity, uri: Uri?): String? {
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}