package com.example.photopicker.ui.pages.home

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.photopicker.ui.popups.addphotopopup.AddPhotosPopup
import coil.compose.rememberAsyncImagePainter
import com.example.photopicker.ui.popups.deletephotopopup.DeletePhotosPopup

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenView() {
    val viewModel: HomeScreenVM = viewModel(factory = HomeScreenVMFactory(LocalContext.current))

    val pickturelauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
            viewModel.fileURL = it.toString()
        }

    val gradient = Brush.horizontalGradient(
        0.7f to Color(0xFF34a8eb),
        0.3f to Color(0xFF34e8eb)
    )
    Scaffold(
        topBar = {
            Text(
                text = "Manage Photos",
                color = Color.White,
                style = androidx.compose.ui.text.TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                ),
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.primary)
                    .padding(16.dp)
                    .fillMaxWidth()
            )

        },
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxHeight(1f)
        ) {

            BodyContent(viewModel = viewModel)
        }
    }
}

@Composable
fun BodyContent(viewModel: HomeScreenVM) {
    val horizontalGradientBrush = Brush.horizontalGradient(
        colors = listOf(
            Color(0xff1E90FF ),
            Color(0XFF1BB3F5 )
        )
    )
    Column(
        Modifier
            .padding(16.dp)
            .fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = viewModel.description)

        Spacer(modifier = Modifier.height(8.dp))

        ImageGrid(viewModel = viewModel)

        if (viewModel.showDialog) {
            AddPhotosPopup(viewModel = viewModel, setShowDialog = {
                viewModel.showDialog = it
            })
        }

        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f, false)
        )

        Button(
            onClick = { viewModel.showDialog = true },
            modifier = Modifier
                .padding(8.dp)
                .height(40.dp)
                .background(
                    brush = horizontalGradientBrush,
                    shape = MaterialTheme.shapes.medium.copy(CornerSize(24.dp))
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            )
        ) {
            Text(text = "Add Photos", style = TextStyle(color = Color.White))
        }

    }
}

@Composable
fun ImageGrid(viewModel: HomeScreenVM) {
    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        if (viewModel.imageList.size > 0) {
//            viewModel.imageList.forEachIndexed { index, item ->
//                ImageView(index = index, viewModel)
//            }
            items(viewModel.imageList.size) { index ->

                ImageView(index = index, viewModel)
            }
        } else {
            items(1) {
                AddPhotosCard(viewModel)
            }
        }
    }

}

@Composable
fun ImageView(index: Int, viewModel: HomeScreenVM) {
    // Created lamda to handle delete action from the delete popup
    var deleteAction: (Boolean) -> Unit = { shouldDelete ->
        viewModel.showDeleteDialog = false
        if (shouldDelete) {
            viewModel.deleteImage()
        }
    }
    Box(
        modifier = Modifier
            .size(220.dp)
            .padding(13.dp),

        ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(shape = MaterialTheme.shapes.medium),
        ) {
            Image(
                painter = rememberAsyncImagePainter(viewModel.imageList[index]),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape = MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )
        }
        Icon(
            imageVector = Icons.Default.Clear,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier
                .absoluteOffset(x = 6.dp, y = (-6).dp)
                .background(Color.LightGray, CircleShape)
                .size(15.dp)
                .align(Alignment.TopEnd)
                .border(width = 1.dp, color = Color.Black, shape = CircleShape)
                .clickable {
                    viewModel.currentDeleteIndex = index
                    viewModel.showDeleteDialog = true


                }
        )

        // If showDeleteDialog is true then show delete popup
        if (viewModel.showDeleteDialog) {
            DeletePhotosPopup(viewModel = viewModel, deleteAction = deleteAction)
        }
    }
}

@Composable
fun AddPhotosCard(viewModel: HomeScreenVM) {

    Box(
        modifier = Modifier
            .size(200.dp)
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clip(shape = MaterialTheme.shapes.medium)
            .background(Color.Gray)
            .clickable {
                viewModel.showDialog = true
            },
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(Color.White, CircleShape)
                .align(Alignment.Center)

        ) {
            // Plus icon in the center
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = Color.Blue,
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier
                .background(Color.White)
                .height(25.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.Bottom,
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Add Photo",
                style = TextStyle(color = Color.Black, fontSize = 12.sp),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(4.dp)
            )
        }
    }
}