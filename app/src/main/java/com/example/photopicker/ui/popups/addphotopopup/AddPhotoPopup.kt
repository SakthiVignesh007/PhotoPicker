package com.example.photopicker.ui.popups.addphotopopup

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.window.Dialog
import com.example.photopicker.ui.pages.home.HomeScreenVM
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.core.net.toUri
import com.example.photopicker.R
import com.google.android.material.bottomsheet.BottomSheetDialog

@Composable
fun AddPhotosPopup(viewModel: HomeScreenVM, setShowDialog: (Boolean) -> Unit) {

    Dialog(
        onDismissRequest = { setShowDialog(false) },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 24.dp)
                .background(Color.Transparent)
                .clickable {
                    setShowDialog(false)
                },
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(text = "ADD PHOTOS", style = TextStyle(color = Color.Black, fontSize = 20.sp))
                Spacer(modifier = Modifier.height(8.dp))

                PickFromGallery(viewModel = viewModel, setShowDialog = setShowDialog)

                Divider(thickness = 1.dp, color = Color.Gray)

                CaptureFromCamera(viewModel = viewModel, setShowDialog = setShowDialog)

                Divider(thickness = 1.dp, color = Color.Gray)

                OpenWhatsapp(viewModel = viewModel, setShowDialog = setShowDialog)

            }
        }

    }
}

@Composable
fun PickFromGallery(viewModel: HomeScreenVM, setShowDialog: (Boolean) -> Unit){
    // Create gallery launcher and handle onResult
    val picturelauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
            if(it != null) {
                viewModel.addImage(it.toString())
                setShowDialog(false)
            }
        }
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 0.dp)
            .clickable {
//                        setShowDialog(false)
                if (viewModel.hasPermission) {
                    picturelauncher.launch("image/*")
                } else {
                    viewModel.checkAndRequestCameraPermission()
                }
            }
    ) {
        Icon(
            painter = painterResource(R.drawable.gallery),
            contentDescription = "Gallery",
            Modifier
                .height(25.dp)
                .width(25.dp),
            tint = Color.Black
        )
        Text(text = "Upload from Gallery", style = TextStyle(color = Color.Black))
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, false)
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "Open Gallery",
            modifier = Modifier
                .height(20.dp)
                .width(20.dp),
            tint = Color.Black
        )
    }
}

@Composable
fun CaptureFromCamera(viewModel: HomeScreenVM, setShowDialog: (Boolean) -> Unit){
    // Create camera launcher and handle onResult
    val cameraLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicture()) {
            if (it) {
                viewModel.addImage(viewModel.imageUri)

                setShowDialog(false)
            }
        }
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 0.dp)
            .clickable {
//                        setShowDialog(false)
                if (viewModel.hasPermission) {
                    viewModel.createImageFile()
                    cameraLauncher.launch(viewModel.imageUri.toUri())
                } else {
                    viewModel.checkAndRequestCameraPermission()
                }
            }
    ) {
        Icon(
            painter = painterResource(R.drawable.camera), contentDescription = "Camera",
            Modifier
                .height(25.dp)
                .width(25.dp), tint = Color.Black
        )
        Text(text = "Take a photo", style = TextStyle(color = Color.Black))
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, false)
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "Open Camera",
            modifier = Modifier
                .height(20.dp)
                .width(20.dp),
            tint = Color.Black
        )
    }
}

@Composable
fun OpenWhatsapp(viewModel: HomeScreenVM, setShowDialog: (Boolean) -> Unit){
//    val whatsappLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.StartActivityForResult(),
//        onResult = { result: ActivityResult ->
//            // Handle the result of starting WhatsApp
//            // Extract the photo path from the result intent if available
//            val photoPath = extractPhotoPathFromWhatsApp(result.data)
//            photoPath?.let { onPhotoPicked(it) }
//        }
//    )
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 0.dp)
            .fillMaxWidth()
            .clickable {
                viewModel.openWhatsapp()
                setShowDialog(false)
            }
    ) {
        Image(
            painter = painterResource(R.drawable.whatsapp),
            contentDescription = "Whatsapp",
            Modifier
                .height(30.dp)
                .width(30.dp)
        )
        Text(
            text = "WhatsApp your photo to us @ \n+91 994XX XXXXX",
            style = TextStyle(color = Color.Black)
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, false)
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "Open Whatsapp",
            modifier = Modifier
                .height(20.dp)
                .width(20.dp),
            tint = Color.Black
        )
    }
}