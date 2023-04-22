package com.example.myapp.ui.match

import android.util.Log
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain.entity.DisplayMatchData
import com.example.domain.entity.MatchTypeData
import com.example.myapp.R
import com.example.myapp.utils.LoadingBar

@Composable
fun Match(
    viewModel: MatchViewModel = hiltViewModel(), accessId: String
) {
    LaunchedEffect(Unit) {
        viewModel.getMatchTypeList()
    }
    val isLoading = viewModel.isLoading.observeAsState()
    val isMatchRecordLoading = viewModel.isMatchRecordLoading.observeAsState()
    val matchList = viewModel.matchTypeList.observeAsState()
    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val displayList = viewModel.displayMatchData.observeAsState()
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
                .padding(top = 10.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.wrapContentSize(), contentAlignment = Alignment.TopCenter
            ) {
                Text(text = "경기 기록", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            if (!matchList.value.isNullOrEmpty()) {
                MatchTypeRow(accessId = accessId) {
                    matchList.value!!
                }
            }
            if (!displayList.value.isNullOrEmpty() && isMatchRecordLoading.value == false) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .fillMaxHeight(1f), contentAlignment = Alignment.CenterStart
                ) {
                    val clicked = remember { mutableStateListOf<Boolean>(*Array(displayList.value!!.size) { false }) }
                    LazyColumn {
                        itemsIndexed(displayList.value!!) { index, item ->
                            DisplayCard(item, index, isExpanded = clicked[index]) {
                                clicked[index] = !clicked[index]
                            }
                            Spacer(modifier = Modifier.fillParentMaxHeight(0.03f))
                        }
                    }
                }
            }
//            Log.d("###", "Match: ${isMatchRecordLoading.value} ${isLoading.value}")
            if (displayList.value.isNullOrEmpty() && isMatchRecordLoading.value == false) {
                EmptyView()
            }
            if (isLoading.value!! || isMatchRecordLoading.value!!) {
                LoadingBar()
            }
        }
    }
}

@Composable
fun MatchTypeRow(accessId: String, list: () -> List<MatchTypeData>) {
    var selectedIndex by remember {
        mutableStateOf(-1)
    }
    val onItemClick = { index: Int -> selectedIndex = index }
    Box(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(5.dp)
            .padding(top = 20.dp), contentAlignment = Alignment.Center
    ) {
        LazyRow {
            itemsIndexed(list()) { index, item ->
                MatchListCard(accessId = accessId, item = item, index = index, selected = selectedIndex == index, onItemClick)
                Spacer(modifier = Modifier.fillParentMaxWidth(0.01f))
            }
        }
    }
}

@Composable
fun MatchListCard(accessId: String, item: MatchTypeData, index: Int, selected: Boolean, onClick: (Int) -> Unit, viewModel: MatchViewModel = hiltViewModel()) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = Color.Black, CircleShape)
            .clip(CircleShape)
            .background(if (selected) colorResource(id = R.color.app_color) else Color.White)
            .padding(15.dp)
            .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                onClick.invoke(index)
                viewModel.getMatchRecord(accessId, item.matchType)
            }, contentAlignment = Alignment.Center

    ) {
        Text(text = item.desc, fontSize = 17.sp)
    }
}

@Composable
fun DisplayCard(data: DisplayMatchData, index: Int, isExpanded: Boolean, viewModel: MatchViewModel = hiltViewModel(), onClick: () -> Unit) {
    val detailData = viewModel.detailMapData.observeAsState(null)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(10.dp))
            .background(
                color = when (data.result) {
                    "승" -> colorResource(id = R.color.win).copy(alpha = 0.5f)
                    "패" -> colorResource(id = R.color.defeat).copy(alpha = 0.5f)
                    else -> Color.DarkGray.copy(
                        alpha = 0.5f
                    )
                }
            )
            .border(1.dp, color = Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(text = data.date.replace("T", "  "), fontSize = 15.sp)
            }
            Divider(color = Color.LightGray, thickness = 1.dp)
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .fillMaxHeight(0.3f)
                    .padding(5.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.333f), contentAlignment = Alignment.Center
                ) {
                    Text(text = data.nickname1, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.5f), contentAlignment = Alignment.Center
                ) {
                    Text(text = "vs", fontSize = 20.sp)
                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(), contentAlignment = Alignment.Center
                ) {
                    Text(text = data.nickname2, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .fillMaxHeight(0.6f)
                    .padding(5.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.333f), contentAlignment = Alignment.Center
                ) {
                    Text(text = data.goal1, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.5f), contentAlignment = Alignment.Center
                ) {
                    Text(text = ":", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(), contentAlignment = Alignment.Center
                ) {
                    Text(text = data.goal2, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }
            Divider(color = Color.DarkGray, thickness = 1.dp)
            Box(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .animateContentSize()
            ) {
                if (isExpanded && detailData.value != null) {
                    DetailView()
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                            if (!isExpanded) {
                                viewModel.getDetailData(data.isFirst, data.matchId)
                            }
                            onClick.invoke()
                        }, contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier
                            .size(30.dp)
                            .alpha(0.5f), painter = painterResource(id = if (isExpanded) R.drawable.ic_contract else R.drawable.ic_expand), contentDescription = ""
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "조회된 기록이 없습니다", fontSize = 15.sp)
    }
}

@Composable
fun DetailView(
    viewModel: MatchViewModel = hiltViewModel()
) {
    val data by viewModel.detailMapData.observeAsState(null)
    if (data != null) {
        val user1 by remember {
            mutableStateOf(data!![0])
        }
        val user2 by remember {
            mutableStateOf(data!![1])
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier
                    .fillMaxSize(0.95f)
                    .padding(5.dp), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.fillMaxWidth(0.333f), contentAlignment = Alignment.Center) {
                        Text(text = user1.result, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.fillMaxWidth(0.5f), contentAlignment = Alignment.Center) {
                        Text(text = "결과", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.fillMaxWidth(1f), contentAlignment = Alignment.Center) {
                        Text(text = user2.result, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.fillMaxWidth(0.333f), contentAlignment = Alignment.Center) {
                        Text(text = user1.goal, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.fillMaxWidth(0.5f), contentAlignment = Alignment.Center) {
                        Text(text = "골", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.fillMaxWidth(1f), contentAlignment = Alignment.Center) {
                        Text(text = user2.goal, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.fillMaxWidth(0.333f), contentAlignment = Alignment.Center) {
                        Text(text = user1.totalShoot, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.fillMaxWidth(0.5f), contentAlignment = Alignment.Center) {
                        Text(text = "전체슛", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.fillMaxWidth(1f), contentAlignment = Alignment.Center) {
                        Text(text = user2.totalShoot, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.fillMaxWidth(0.333f), contentAlignment = Alignment.Center) {
                        Text(text = user1.validShoot, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.fillMaxWidth(0.5f), contentAlignment = Alignment.Center) {
                        Text(text = "유효슛", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.fillMaxWidth(1f), contentAlignment = Alignment.Center) {
                        Text(text = user2.validShoot, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.fillMaxWidth(0.333f), contentAlignment = Alignment.Center) {
                        Text(text = user1.shootRating, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.fillMaxWidth(0.5f), contentAlignment = Alignment.Center) {
                        Text(text = "슈팅성공률", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.fillMaxWidth(1f), contentAlignment = Alignment.Center) {
                        Text(text = user2.shootRating, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.fillMaxWidth(0.333f), contentAlignment = Alignment.Center) {
                        Text(text = "${user1.validPass} %", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.fillMaxWidth(0.5f), contentAlignment = Alignment.Center) {
                        Text(text = "패스성공률", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.fillMaxWidth(1f), contentAlignment = Alignment.Center) {
                        Text(text = "${user2.validPass} %", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.fillMaxWidth(0.333f), contentAlignment = Alignment.Center) {
                        Text(text = "${user1.validDefence} %", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.fillMaxWidth(0.5f), contentAlignment = Alignment.Center) {
                        Text(text = "수비성공률", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.fillMaxWidth(1f), contentAlignment = Alignment.Center) {
                        Text(text = "${user2.validDefence} %", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.fillMaxWidth(0.333f), contentAlignment = Alignment.Center) {
                        Text(text = "${user1.validTackle} %", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.fillMaxWidth(0.5f), contentAlignment = Alignment.Center) {
                        Text(text = "태클성공률", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.fillMaxWidth(1f), contentAlignment = Alignment.Center) {
                        Text(text = "${user2.validTackle} %", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.fillMaxWidth(0.333f), contentAlignment = Alignment.Center) {
                        Text(text = "${user1.possession} %", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.fillMaxWidth(0.5f), contentAlignment = Alignment.Center) {
                        Text(text = "점유율", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.fillMaxWidth(1f), contentAlignment = Alignment.Center) {
                        Text(text = "${user2.possession} %", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.fillMaxWidth(0.333f), contentAlignment = Alignment.Center) {
                        Text(text = user1.offsideCount, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.fillMaxWidth(0.5f), contentAlignment = Alignment.Center) {
                        Text(text = "옵사이드", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.fillMaxWidth(1f), contentAlignment = Alignment.Center) {
                        Text(text = user2.offsideCount, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.fillMaxWidth(0.333f), contentAlignment = Alignment.Center) {
                        Text(text = user1.yellowCards, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.fillMaxWidth(0.5f), contentAlignment = Alignment.Center) {
                        Text(text = "경고", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.fillMaxWidth(1f), contentAlignment = Alignment.Center) {
                        Text(text = user2.yellowCards, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.fillMaxWidth(0.333f), contentAlignment = Alignment.Center) {
                        Text(text = user1.redCards, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.fillMaxWidth(0.5f), contentAlignment = Alignment.Center) {
                        Text(text = "퇴장", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.fillMaxWidth(1f), contentAlignment = Alignment.Center) {
                        Text(text = user2.redCards, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.fillMaxWidth(0.333f), contentAlignment = Alignment.Center) {
                        Text(text = user1.foul, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.fillMaxWidth(0.5f), contentAlignment = Alignment.Center) {
                        Text(text = "파울", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.fillMaxWidth(1f), contentAlignment = Alignment.Center) {
                        Text(text = user2.foul, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.fillMaxWidth(0.333f), contentAlignment = Alignment.Center) {
                        Text(text = user1.injury, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.fillMaxWidth(0.5f), contentAlignment = Alignment.Center) {
                        Text(text = "부상", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.fillMaxWidth(1f), contentAlignment = Alignment.Center) {
                        Text(text = user2.injury, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.fillMaxWidth(0.333f), contentAlignment = Alignment.Center) {
                        Text(text = "${user1.averageRating} / 5.0", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.fillMaxWidth(0.5f), contentAlignment = Alignment.Center) {
                        Text(text = "경기평점", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.fillMaxWidth(1f), contentAlignment = Alignment.Center) {
                        Text(text = "${user2.averageRating} / 5.0", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            LoadingBar()
        }
    }
}