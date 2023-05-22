package com.woo.myapp.ui.ranker

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import com.woo.myapp.R
import com.woo.myapp.utils.LoadingBar
import com.woo.myapp.utils.Percentage
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
    val error by viewModel.error.observeAsState()
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.app_color1)), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.app_color2))
            ) {
                Box(
                    modifier = Modifier.padding(10.dp), contentAlignment = Alignment.TopStart
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_back_new), modifier = Modifier
                            .size(30.dp)
                            .clickable(
                                interactionSource = MutableInteractionSource(), indication = null
                            ) { backPressedDispatcher?.onBackPressed() }, contentDescription = "뒤로가기", contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(), contentAlignment = Alignment.Center
                    ) {
                        Text(text = "선수 평균 스탯", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = colorResource(id = R.color.app_color4))
                    }
                }
            }
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
                val customTextFieldColors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(id = R.color.app_color5),
                    unfocusedBorderColor = colorResource(id = R.color.app_color6),
                    focusedLabelColor = colorResource(id = R.color.app_color5),
                    unfocusedLabelColor = colorResource(id = R.color.app_color6),
                    backgroundColor = colorResource(id = R.color.app_color8),
                    cursorColor = colorResource(id = R.color.app_color1),
                    placeholderColor = colorResource(id = R.color.app_color5),
                    textColor = colorResource(id = R.color.app_color1)
                )
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
                    OutlinedTextField(
                        value = text,
                        onValueChange = { setValue.invoke(it) },
                        enabled = true,
                        label = { (Icon(painter = painterResource(id = R.drawable.ic_search_user_new), contentDescription = "")) },
                        placeholder = { (Text(text = "Search for player")) },
                        textStyle = TextStyle(fontSize = 20.sp, color = Color.Black),
                        maxLines = 1,
                        shape = RoundedCornerShape(30.dp),
                        singleLine = true,
                        colors = customTextFieldColors,
                    )
                }
                Box(
                    contentAlignment = Alignment.Center, modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp, top = 10.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(color = colorResource(id = R.color.app_color8))
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
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                        ) {
                            itemsIndexed(items = searchData!!) { index, item ->
                                SearchResultCard(item = item, index = index, onSelected, isSelected = isSelectIndex == index, hideKeyboard)
                            }
                        }
                        if (isSearchLoading!!) {
                            isSelectIndex = -1
                            viewModel.setRankerList()
                            LoadingBar()
                        }
                    }
                }
                Box(
                    contentAlignment = Alignment.TopCenter, modifier = Modifier
                        .padding(start = 5.dp, end = 5.dp)
                        .fillMaxSize()
                ) {
                    if (rankerData != null) {
                        LazyColumn(verticalArrangement = Arrangement.Top, modifier = Modifier.fillMaxSize()) {
                            items(rankerData!!) {
                                RankerDataCard(item = it)
                                Spacer(modifier = Modifier.fillParentMaxHeight(0.02f))
                            }
                        }
                        if (isRankerLoading!!) {
                            LoadingBar()
                        }
                    }
                    if (error!!.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(), contentAlignment = Alignment.Center
                        ) {
                            Image(painter = painterResource(id = R.drawable.baseline_cancel_24), contentDescription = null, Modifier.size(50.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchResultCard(item: SearchRankerData, index: Int, onSelected: (Int) -> Unit, isSelected: Boolean, hideKeyboard: () -> Unit, viewModel: RankerViewModel = hiltViewModel()) {
    Box(
        contentAlignment = Alignment.Center, modifier = Modifier
            .fillMaxSize()
            .background(color = if (isSelected) colorResource(id = R.color.app_color) else Color.White)
    ) {
        Box(contentAlignment = Alignment.CenterStart, modifier = Modifier
            .fillMaxSize()
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
                    .fillMaxHeight()
                    .padding(10.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = item.image), contentDescription = null, modifier = Modifier.size(25.dp)
                )
                Spacer(modifier = Modifier.fillMaxWidth(0.02f))
                Text(text = item.name, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, maxLines = 2, modifier = Modifier.fillMaxSize(), color = colorResource(id = R.color.app_color1))
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
                modifier = Modifier.size(100.dp)
            ) {
                drawArc(
                    topLeft = Offset(0f, size.height / 2), color = color,
                    size = Size(size.width, size.height), startAngle = -180f, sweepAngle = 180f, useCenter = false,
                )
            }
            Column {
                Spacer(modifier = Modifier.fillMaxHeight(0.6f))
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    Text(text = if (item.status.matchCount != 20) "${item.status.matchCount}회 사용" else "20회 이상 사용", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
        Box(
            contentAlignment = Alignment.Center, modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(10.dp))
                .background(colorResource(id = R.color.app_color8))
                .border(5.dp, color = color, shape = RoundedCornerShape(10.dp))

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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = color)
                        .padding(vertical = 5.dp, horizontal = 10.dp), verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight(0.4f)
                            .fillMaxWidth(0.2f)
                            .clip(RoundedCornerShape(15.dp))
                            .background(colorResource(id = R.color.app_color8)), contentAlignment = Alignment.Center

                    ) {
                        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "골")
                            Text(text = item.status.goal.toString())
                        }
                    }
                    Spacer(modifier = Modifier.width(3.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxHeight(0.4f)
                            .fillMaxWidth(0.25f)
                            .clip(RoundedCornerShape(15.dp))
                            .background(colorResource(id = R.color.app_color8)), contentAlignment = Alignment.Center
                    ) {
                        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "도움")
                            Text(text = item.status.assist.toString())
                        }
                    }
                    Spacer(modifier = Modifier.width(3.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxHeight(0.4f)
                            .fillMaxWidth(0.333f)
                            .clip(RoundedCornerShape(15.dp))
                            .background(colorResource(id = R.color.app_color8)), contentAlignment = Alignment.Center
                    ) {
                        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "슈팅")
                            Text(text = item.status.shoot.toString())
                        }
                    }
                    Spacer(modifier = Modifier.width(3.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxHeight(0.4f)
                            .fillMaxWidth(0.5f)
                            .clip(RoundedCornerShape(15.dp))
                            .background(colorResource(id = R.color.app_color8)), contentAlignment = Alignment.Center
                    ) {
                        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "패스")
                            Text(text = item.status.passTry.toString())
                        }
                    }
                    Spacer(modifier = Modifier.width(3.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxHeight(0.4f)
                            .fillMaxWidth(1f)
                            .clip(RoundedCornerShape(15.dp))
                            .background(colorResource(id = R.color.app_color8)), contentAlignment = Alignment.Center
                    ) {
                        val sum = (item.status.tackle + item.status.block) / 2
                        val defence = String.format("%.2f", sum).toFloat()
                        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "수비")
                            Text(text = defence.toString())
                        }
                    }
                }
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