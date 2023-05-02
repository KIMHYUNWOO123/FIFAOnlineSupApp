package com.example.myapp.ui.user

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain.entity.BestRankList
import com.example.myapp.R
import com.example.myapp.utils.EmptyView
import com.example.myapp.utils.LoadingBar

@Composable
fun User(
    viewModel: UserViewModel = hiltViewModel(), onMatchView: (String) -> Unit, onTransactionView: (String) -> Unit
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
                Text(text = "유저 정보", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { setValue.invoke(it) },
                    enabled = true,
                    label = { (Icon(painter = painterResource(id = R.drawable.ic_usersearch), contentDescription = "")) },
                    placeholder = { (Text(text = "Search for user")) },
                    textStyle = TextStyle(fontSize = 20.sp, color = Color.Black),
                    maxLines = 1,
                    singleLine = true
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
                        .fillMaxWidth(0.3f)
                        .clickable(interactionSource = interactionSource, indication = null) {
                            viewModel.getUserData(text)
                            focusManager.clearFocus()
                            onInitView.invoke()
                        }
                        .clip(RoundedCornerShape(10.dp))
                        .background(color = if (isClick) colorResource(id = R.color.app_color) else colorResource(id = R.color.white))
                        .border(width = 1.dp, color = colorResource(id = R.color.app_color), shape = RoundedCornerShape(10.dp))
                        .size(40.dp),
                )
                Text(text = "검색하기", textAlign = TextAlign.Center, color = if (isClick) Color.White else Color.Black, fontSize = 15.sp)
            }
            if (accessId!!.isNotBlank()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(0.4f), contentAlignment = Alignment.Center
                    ) {
                        Image(modifier = Modifier.size(30.dp), painter = painterResource(id = R.drawable.ic_nickname), contentDescription = "")
                    }
                    Box(
                        modifier = Modifier.fillMaxWidth(1f), contentAlignment = Alignment.Center
                    ) {
                        Text(text = name.toString(), fontSize = 25.sp, fontStyle = FontStyle.Italic)
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(0.4f), contentAlignment = Alignment.Center
                    ) {
                        Text(text = "LV", fontSize = 25.sp, color = Color.Blue)
                    }
                    Box(
                        modifier = Modifier.fillMaxWidth(1f), contentAlignment = Alignment.Center
                    ) {
                        Text(text = level.toString(), fontSize = 25.sp)
                    }
                }
                if (!bestRankList.isNullOrEmpty() && isLoading == false) {
                    Column(modifier = Modifier.wrapContentHeight()) {
                        Box(
                            modifier = Modifier
                                .wrapContentHeight()
                                .fillMaxWidth(), contentAlignment = Alignment.Center
                        ) {
                            Text(text = "최고기록", fontSize = 20.sp)
                        }
                        BestRankView { bestRankList!! }
                    }
                    val matchInteractionSource = remember {
                        MutableInteractionSource()
                    }
                    val matchIsPress by matchInteractionSource.collectIsPressedAsState()
                    Box(modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .clickable(interactionSource = matchInteractionSource, indication = null) {
                            onMatchView(accessId.toString())
                        }
                        .padding(top = 30.dp)
                        .wrapContentHeight()
                        .clip(RectangleShape)
                        .border(width = 1.dp, color = colorResource(id = R.color.app_color))
                        .background(if (matchIsPress) colorResource(id = R.color.app_color) else Color.White), contentAlignment = Alignment.Center) {
                        Text(modifier = Modifier.padding(vertical = 15.dp), text = "경기 기록", fontSize = 30.sp, color = if (matchIsPress) Color.White else Color.Black)
                    }
                    val transactionInteractionSource = remember {
                        MutableInteractionSource()
                    }
                    val transactionIsPress by transactionInteractionSource.collectIsPressedAsState()
                    Box(modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .clickable(interactionSource = transactionInteractionSource, indication = null) {
                            onTransactionView(accessId.toString())
                        }
                        .padding(top = 30.dp)
                        .wrapContentHeight()
                        .clip(RectangleShape)
                        .border(width = 1.dp, color = colorResource(id = R.color.app_color))
                        .background(if (transactionIsPress) colorResource(id = R.color.app_color) else Color.White), contentAlignment = Alignment.Center) {
                        Text(modifier = Modifier.padding(vertical = 15.dp), text = "거래 내역", fontSize = 30.sp, color = if (transactionIsPress) Color.White else Color.Black)
                    }
                }
            }
            if (error!!.isNotBlank()) {
                EmptyView()
            }
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
                Spacer(modifier = Modifier.fillMaxHeight(0.1f))
                Divider(
                    color = Color.LightGray, modifier = Modifier
                        .fillMaxHeight(0.05f)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun BestRankListCard(item: BestRankList) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(5.dp), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = item.matchType, fontSize = 15.sp)
        Text(text = item.division, fontSize = 15.sp)
        Text(text = item.date, fontSize = 15.sp)
    }
}

