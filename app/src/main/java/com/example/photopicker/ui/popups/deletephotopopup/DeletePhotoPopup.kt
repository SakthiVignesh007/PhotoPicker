package com.example.photopicker.ui.popups.deletephotopopup

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRowDefaults.contentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.net.toUri
import com.example.photopicker.R
import com.example.photopicker.ui.pages.home.HomeScreenVM
import com.google.android.material.bottomsheet.BottomSheetDialog

@Composable
fun DeletePhotosPopup(viewModel: HomeScreenVM, deleteAction: (Boolean) -> Unit) {

    Dialog(onDismissRequest = { deleteAction(false) }) {
        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .background(Color.White)
                .padding(12.dp)
        ) {
            Text(
                text = "Delete Photo",
                style = TextStyle(color = Color.Black, fontSize = 18.sp, FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Member with multiple photo will get better responses. Do you still want to delete this photo?",
                style = TextStyle(color = Color.Black)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.End
            ) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, false)
                )
                Button(
                    onClick = { deleteAction(true) },
                    modifier = Modifier
                        .padding(8.dp),
                    border = BorderStroke(1.dp, Color(0XFF1E90FF)),
                    shape = MaterialTheme.shapes.medium.copy(CornerSize((24.dp))),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                    )

                ) {
                    Text(text = "Yes", style = TextStyle(color = Color(0XFF1E90FF)))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick = { deleteAction(false) },
                    modifier = Modifier
                        .padding(8.dp),
                    shape = MaterialTheme.shapes.medium.copy(CornerSize((24.dp))),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0XFF1E90FF),
                    )

                ) {
                    Text(text = "No", style = TextStyle(color = Color.White))
                }
            }
        }

    }
}