package com.example.photopicker.ui.pages.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.io.File
import java.security.Permission
import java.util.Date

class HomeScreenVM(val context: Context) : ViewModel() {
    var showDialog by mutableStateOf(false)
    var showDeleteDialog by mutableStateOf(false)
    var imageUri by mutableStateOf("")
    var hasPermission by mutableStateOf(
        ActivityCompat.checkSelfPermission(
            context,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    )
    var currentDeleteIndex by mutableStateOf(0)
    var imageList by mutableStateOf(mutableListOf<String>())
    var fileURL by mutableStateOf("")
    var description by mutableStateOf("Photos are the first thing members look in your profile.  Add multiple photos to get more matches")
    var phoneNumber = "9942533023"
    fun createImageFile() {
        try {
            // Create an image file name
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val file = File.createTempFile(
                "JPEG_${timeStamp}_", //prefix
                ".jpg", //suffix
                storageDir //directory
            )
            imageUri = storageDir!!.absolutePath
            val imgUri = FileProvider.getUriForFile(
                context,
                "com.example.photopicker.provider",
                file
            )
            imageUri = imgUri.toString()

        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    fun addImage(imagePath: String) {
        try {
            // If the imageList size is less than 6 then add the imagePath to imageList else show toast
            if (imageList.size < 6) {
                imageList.add(imagePath)
            } else {
                Toast.makeText(context, "Image limit exceeded", Toast.LENGTH_SHORT).show()
            }

            if(imageList.size > 0){
                description = "Drag photos to reorder. First photo will be displayed as your profile photo"
            }
        } catch (ex: Exception) {

        }
    }

    fun deleteImage() {
        try {
            // Remove a string from imageList using currentDeleteIndex
            imageList = imageList.toMutableList().apply { removeAt(currentDeleteIndex) }

            // If the imageList size is 0 then set the description
            if (imageList.size == 0) {
                description =
                    "Photos are the first thing members look in your profile.  Add multiple photos to get more matches"
            }
        } catch (ex: Exception) {

        }
    }

    fun checkAndRequestCameraPermission() {
        try {
            viewModelScope.launch {
                // If camera permission is not granted
                if (ActivityCompat.checkSelfPermission(
                        context,
                        android.Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // Request camera permission
                    ActivityCompat.requestPermissions(
                        context as Activity,
                        arrayOf(android.Manifest.permission.CAMERA), 1
                    )
                } else {
                    hasPermission = true
                }
                if (ActivityCompat.checkSelfPermission(
                        context,
                        android.Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_DENIED
                ) {
                    Toast.makeText(context, "Please grant permission ", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    hasPermission = true
                }
            }

        } catch (ex: Exception) {

        }
    }

    fun openWhatsapp(){
        try{
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://wa.me/$phoneNumber")
            context.startActivity(intent)
        } catch (ex: Exception){

        }
    }
}

class HomeScreenVMFactory(val context: Context) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeScreenVM(context) as T
    }
}