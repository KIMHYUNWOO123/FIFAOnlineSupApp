package com.example.myapp.ui.ranker

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.domain.entity.RankerPlayerData
import com.example.domain.entity.SearchRankerData
import com.example.myapp.R
import com.example.myapp.utils.CircleLoadingBar
import com.example.myapp.utils.Percentage
import kotlinx.coroutines.delay

@Composable
fun Ranker(viewModel: RankerViewModel = hiltViewModel()) {
    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val focusManager = LocalFocusManager.current
    val (text: String, setValue: (String) -> Unit) = remember {
        mutableStateOf("")
    }
    val searchData by viewModel.spIdData.observeAsState()
    val rankerData by viewModel.rankerData.observeAsState()
    LaunchedEffect(text) {
        if (text.isNotBlank()) {
            delay(500)
            viewModel.searchSpId(text)
        } else {
            delay(500)
            viewModel.setSpIdList()
            viewModel.setRankerList()
        }
    }
    val hideKeyboard = {
        focusManager.clearFocus()
    }
    val isSearchLoading by viewModel.isSearchLoading.observeAsState(false)
    val isRankerLoading by viewModel.isRankerLoading.observeAsState(false)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(interactionSource = MutableInteractionSource(), indication = null) { hideKeyboard.invoke() }, contentAlignment = Alignment.Center
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
                Text(text = "랭커들이 쓴 평균 선수 스탯", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
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
            Box(
                contentAlignment = Alignment.Center, modifier = Modifier
                    .border(1.dp, color = Color.DarkGray)
                    .padding(start = 10.dp, end = 10.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(0.3f)
            ) {
                if (searchData != null) {
                    var isSelectIndex by remember {
                        mutableStateOf(-1)
                    }
                    val onSelected: (Int) -> Unit = {
                        isSelectIndex = it
                    }
                    LazyColumn(verticalArrangement = Arrangement.Top) {
                        itemsIndexed(searchData!!) { index, item ->
                            SearchResultCard(item = item, index = index, onSelected, isSelected = isSelectIndex == index, hideKeyboard)
                            Divider(thickness = 1.dp, color = Color.LightGray)
                        }
                    }
                    if (isSearchLoading!!) {
                        isSelectIndex = -1
                        viewModel.setRankerList()
                        CircleLoadingBar()
                    }
                }
            }
            if (rankerData != null) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(5.dp)) {
                    LazyColumn(verticalArrangement = Arrangement.Top) {
                        items(rankerData!!) {
                            RankerDataCard(item = it)
                            Spacer(modifier = Modifier.fillParentMaxHeight(0.03f))
                        }
                    }
                    if (isRankerLoading!!) {
                        CircleLoadingBar()
                    }
                }
            }
        }
    }
}

@Composable
fun SearchResultCard(item: SearchRankerData, index: Int, onSelected: (Int) -> Unit, isSelected: Boolean, hideKeyboard: () -> Unit, viewModel: RankerViewModel = hiltViewModel()) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.background(color = if (isSelected) colorResource(id = R.color.app_color) else Color.White)) {
        Box(contentAlignment = Alignment.CenterStart, modifier = Modifier
            .padding(start = 5.dp)
            .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                onSelected.invoke(index)
                hideKeyboard.invoke()
                viewModel.getPlayerData(50, item.id)
                viewModel.setRankerList()
            }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp), verticalAlignment = Alignment.CenterVertically
            ) {
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
}

@Composable
fun RankerDataCard(item: RankerPlayerData, viewModel: RankerViewModel = hiltViewModel()) {
    val color = when (item.spPositionInt) {
        0 -> colorResource(id = R.color.gk)
        in 1..8 -> colorResource(id = R.color.df)
        in 9..19 -> colorResource(id = R.color.mf)
        else -> colorResource(id = R.color.fw)
    }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp), contentAlignment = Alignment.BottomCenter
        ) {
            Canvas(
                modifier = Modifier
                    .size(100.dp)
            ) {
                drawArc(
                    topLeft = Offset(0f, size.height / 2), color = color,
                    size = Size(size.width, size.height), startAngle = -180f, sweepAngle = 180f, useCenter = false,
                )
            }
            Column {
                Spacer(modifier = Modifier.fillMaxHeight(0.6f))
                Box(
                    modifier = Modifier
                        .fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    Text(text = if (item.status.matchCount != 20) "${item.status.matchCount}회 사용" else "20회 이상 사용", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
        Box(
            contentAlignment = Alignment.Center, modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(10.dp))
                .border(3.dp, color = color, shape = RoundedCornerShape(10.dp))
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight(0.1f)
                        .fillMaxWidth()
                        .background(color = color), contentAlignment = Alignment.Center
                ) {
                    Text(text = item.spPosition, fontSize = 15.sp, color = Color.White)
                }
                Divider(thickness = 1.dp, color = Color.LightGray)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp, horizontal = 5.dp), verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight(0.4f)
                            .fillMaxWidth(0.2f), contentAlignment = Alignment.Center
                    ) {
                        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "골")
                            Divider(thickness = 0.5.dp, color = Color.LightGray)
                            Text(text = item.status.goal.toString())
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxHeight(0.4f)
                            .fillMaxWidth(0.25f), contentAlignment = Alignment.Center
                    ) {
                        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "어시스트")
                            Divider(thickness = 0.5.dp, color = Color.LightGray)
                            Text(text = item.status.assist.toString())
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxHeight(0.4f)
                            .fillMaxWidth(0.333f), contentAlignment = Alignment.Center
                    ) {
                        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "슈팅")
                            Divider(thickness = 0.5.dp, color = Color.LightGray)
                            Text(text = item.status.shoot.toString())
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxHeight(0.4f)
                            .fillMaxWidth(0.5f), contentAlignment = Alignment.Center
                    ) {
                        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "패스")
                            Divider(thickness = 0.5.dp, color = Color.LightGray)
                            Text(text = item.status.passTry.toString())
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxHeight(0.4f)
                            .fillMaxWidth(1f), contentAlignment = Alignment.Center
                    ) {
                        val sum = (item.status.tackle + item.status.block) / 2
                        val defence = String.format("%.2f", sum).toFloat()
                        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "수비")
                            Divider(thickness = 0.5.dp, color = Color.LightGray)
                            Text(text = defence.toString())
                        }
                    }
                }
                Divider(thickness = 1.dp, color = Color.LightGray)
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 5.dp, horizontal = 3.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.333f)
                            .fillMaxHeight(), contentAlignment = Alignment.Center
                    ) {
                        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("슈팅성공률")
                            Percentage(percentage = item.status.validShootPercentage, color)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .fillMaxHeight(), contentAlignment = Alignment.Center
                    ) {
                        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("패스성공률")
                            Percentage(percentage = item.status.validPassPercentage, color)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .fillMaxHeight(), contentAlignment = Alignment.Center
                    ) {
                        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("드리블성공률")
                            Percentage(percentage = item.status.validDribblePercentage, color)
                        }
                    }
                }
            }
        }
    }
}