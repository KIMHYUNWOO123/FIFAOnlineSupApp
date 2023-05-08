package com.example.myapp.ui.ranker

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.domain.entity.SearchRankerData
import com.example.myapp.R
import kotlinx.coroutines.delay

@Composable
fun Ranker(viewModel: RankerViewModel = hiltViewModel()) {
    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val focusManager = LocalFocusManager.current
    val (text: String, setValue: (String) -> Unit) = remember {
        mutableStateOf("")
    }
    val searchData by viewModel.spIdData.observeAsState()
    LaunchedEffect(text) {
        if (text.isNotBlank()) {
            delay(500)
            viewModel.searchSpId(text)
        } else {
            delay(500)
            viewModel.setSpIdList()
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(interactionSource = MutableInteractionSource(), indication = null) { focusManager.clearFocus() }, contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp), contentAlignment = Alignment.TopStart
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_back_24), modifier = Modifier
                    .size(30.dp)
                    .clickable(
                        interactionSource = MutableInteractionSource(), indication = null
                    ) { backPressedDispatcher?.onBackPressed() }, contentDescription = "뒤로가기", contentScale = ContentScale.Crop
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .wrapContentSize(), contentAlignment = Alignment.TopCenter
            ) {
                Text(text = "랭커들이 쓴 선수조회", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
                OutlinedTextField(
                    value = text,
                    onValueChange = setValue,
                    enabled = true,
                    label = { (Icon(painter = painterResource(id = R.drawable.ic_usersearch), contentDescription = "")) },
                    placeholder = { (Text(text = "Search for player")) },
                    textStyle = TextStyle(fontSize = 20.sp, color = Color.Black),
                    maxLines = 1,
                    singleLine = true
                )
            }
            if (searchData != null) {
                LazyColumn(modifier = Modifier
                    .border(1.dp, color = Color.DarkGray)
                    .padding(start = 10.dp, end = 10.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(0.3f)) {
                    itemsIndexed(searchData!!) { index, item ->
                        SearchResultCard(item = item, index = index)
                        Divider(thickness = 1.dp, color = Color.LightGray)
                    }
                }
            }
        }
    }
}

@Composable
fun SearchResultCard(item: SearchRankerData, index: Int) {
    Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.padding(start = 5.dp)) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = rememberAsyncImagePainter(model = item.image),
                contentDescription = null,
                modifier = Modifier.size(25.dp)
            )
            Spacer(modifier = Modifier.fillMaxWidth(0.01f))
            Text(text = item.name, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}