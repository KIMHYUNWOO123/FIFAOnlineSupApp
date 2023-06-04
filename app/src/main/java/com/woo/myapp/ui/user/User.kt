package com.woo.myapp.ui.user

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain.entity.BestRankList
import com.woo.myapp.R
import com.woo.myapp.utils.EmptyView
import com.woo.myapp.utils.LoadingBar

@Composable
fun User(
    viewModel: UserViewModel = hiltViewModel(), onMatchView: (String) -> Unit, onTransactionView: (String) -> Unit,
) {
    val name by viewModel.userData.observeAsState()
    val level by viewModel.userLevel.observeAsState()
    val accessId by viewModel.userAccessId.observeAsState()
    val (text: String, setValue: (String) -> Unit) = remember {
        mutableStateOf("")
    }
    val isLoading by viewModel.isLoading.observeAsState()
    val focusManager = LocalFocusManager.current
    val error by viewModel.error.observeAsState()
    val bestRankList by viewModel.bestRankList.observeAsState()
    val onBestRankView = remember {
        mutableStateOf(false)
    }
    val onInitView: () -> Unit = {
        onBestRankView.value = false
    }
    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    LaunchedEffect(Unit) {
//       viewModel.getBestRank("03704d4c66349949a799704a")
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
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
                        Text(text = "유저 정보", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = colorResource(id = R.color.app_color4))
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(interactionSource = MutableInteractionSource(), indication = null) { focusManager.clearFocus() }
                    .background(color = colorResource(id = R.color.app_color1)), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.fillMaxHeight(0.05f))
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
                        placeholder = { (Text(text = "Search for user")) },
                        textStyle = TextStyle(fontSize = 20.sp, color = Color.Black),
                        maxLines = 1,
                        shape = RoundedCornerShape(30.dp),
                        singleLine = true,
                        colors = customTextFieldColors,
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp), contentAlignment = Alignment.Center
                ) {
                    val interactionSource = remember {
                        MutableInteractionSource()
                    }
                    val isClick by interactionSource.collectIsPressedAsState()

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .padding(vertical = 5.dp)
                            .clickable(interactionSource = interactionSource, indication = null) {
                                viewModel.getUserData(text)
                                focusManager.clearFocus()
                                onInitView.invoke()
                            }
                            .clip(RoundedCornerShape(10.dp))
                            .background(color = if (isClick) colorResource(id = R.color.app_color5) else colorResource(id = R.color.app_color8))
                            .size(40.dp),
                    )
                    Text(text = "검색하기", textAlign = TextAlign.Center, color = if (isClick) Color.White else Color.Black, fontSize = 15.sp)
                }
                if (accessId!!.isNotBlank() && !isLoading!!) {
                    Spacer(modifier = Modifier.fillMaxHeight(0.05f))
                    Box(
                        modifier = Modifier
                            .padding(15.dp)
                            .clip(RoundedCornerShape(15.dp))
//                            .background(colorResource(id = R.color.app_color4))
                            .background(colorResource(id = R.color.app_color8))
                    ) {
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp, start = 10.dp, end = 10.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxWidth(0.4f), contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "닉네임", fontSize = 25.sp, color = colorResource(id = R.color.app_color1))
                                }
                                Box(
                                    modifier = Modifier.fillMaxWidth(1f), contentAlignment = Alignment.Center
                                ) {
                                    Text(text = name.toString(), fontSize = 25.sp, color = colorResource(id = R.color.app_color1))
                                }
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp, start = 10.dp, end = 10.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxWidth(0.4f), contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "LV", fontSize = 25.sp, color = colorResource(id = R.color.app_color1))
                                }
                                Box(
                                    modifier = Modifier.fillMaxWidth(1f), contentAlignment = Alignment.Center
                                ) {
                                    Text(text = level.toString(), fontSize = 25.sp, color = colorResource(id = R.color.app_color1))
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp), contentAlignment = Alignment.TopCenter
                            ) {
                                Canvas(
                                    modifier = Modifier
                                        .size(80.dp)
                                ) {
                                    drawArc(
                                        topLeft = Offset(0f, size.height / 2), color = Color.White,
                                        size = Size(size.width, size.height), startAngle = -180f, sweepAngle = 180f, useCenter = false,
                                    )
                                }
                                Column {
                                    Spacer(modifier = Modifier.fillMaxHeight(0.6f))
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize(), contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "최고기록", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                    }
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                                    .clip(RoundedCornerShape(15.dp))
                                    .background(Color.White)
                            ) {
                                BestRankView { bestRankList!! }
                            }
                        }
                    }
                    Box(
                        Modifier
                            .fillMaxSize()
                            .padding(top = 15.dp), contentAlignment = Alignment.TopCenter
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(0.9f), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                            val matchInteractionSource = remember {
                                MutableInteractionSource()
                            }
                            val matchIsPress by matchInteractionSource.collectIsPressedAsState()
                            Box(modifier = Modifier
                                .clickable(interactionSource = matchInteractionSource, indication = null) {
                                    onMatchView(accessId.toString())
                                }
                                .wrapContentHeight()
                                .clip(RoundedCornerShape(25.dp))
                                .background(if (matchIsPress) colorResource(id = R.color.app_color8) else colorResource(id = R.color.app_color2))
                                .padding(20.dp), contentAlignment = Alignment.Center) {
                                Text(
                                    modifier = Modifier.padding(vertical = 15.dp),
                                    text = "경기 기록",
                                    fontSize = 25.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (matchIsPress) colorResource(id = R.color.app_color2) else colorResource(id = R.color.app_color8)
                                )
                            }
                            Spacer(modifier = Modifier.fillMaxWidth(0.1f))
                            val transactionInteractionSource = remember {
                                MutableInteractionSource()
                            }
                            val transactionIsPress by transactionInteractionSource.collectIsPressedAsState()
                            Box(modifier = Modifier
                                .clickable(interactionSource = transactionInteractionSource, indication = null) {
                                    onTransactionView(accessId.toString())
                                }
                                .wrapContentHeight()
                                .clip(RoundedCornerShape(25.dp))
                                .background(if (transactionIsPress) colorResource(id = R.color.app_color8) else colorResource(id = R.color.app_color2))
                                .padding(20.dp), contentAlignment = Alignment.Center) {
                                Text(
                                    modifier = Modifier.padding(vertical = 15.dp),
                                    text = "거래 내역",
                                    fontSize = 25.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (transactionIsPress) colorResource(id = R.color.app_color2) else colorResource(id = R.color.app_color8)
                                )
                            }
                        }
                    }
                }
            }
        }
        if (error!!.isNotBlank()) {
            EmptyView()
        }
        if (isLoading!!) {
            LoadingBar()
        }
    }
}

@Composable
fun BestRankView(list: () -> List<BestRankList>) {
    Box(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(5.dp)
            .padding(top = 10.dp), contentAlignment = Alignment.Center
    ) {
        LazyColumn {
            items(list()) { item ->
                BestRankListCard(item = item)
                Spacer(modifier = Modifier.fillParentMaxHeight(0.01f))
            }
        }
    }
}

@Composable
fun BestRankListCard(item: BestRankList) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(30.dp))
            .background(colorResource(id = R.color.app_color7)), contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(10.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = item.matchType, fontSize = 15.sp, color = colorResource(id = R.color.app_color1))
            Text(text = item.division, fontSize = 15.sp, color = colorResource(id = R.color.app_color1))
            Text(text = item.date, fontSize = 15.sp, color = colorResource(id = R.color.app_color1))
        }
    }
}

