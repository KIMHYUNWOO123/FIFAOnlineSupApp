package com.example.myapp.ui.match

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
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.example.domain.entity.MatchDetailData
import com.example.domain.entity.MatchTypeData
import com.example.myapp.R
import com.example.myapp.utils.CircleLoadingBar
import com.example.myapp.utils.LoadingBar

@Composable
fun Match(
    viewModel: MatchViewModel = hiltViewModel(), accessId: String,
) {
    LaunchedEffect(Unit) {
        viewModel.getMatchTypeList()
    }
    var isRefresh by remember {
        mutableStateOf(false)
    }
    var isAppend by remember {
        mutableStateOf(false)
    }
    var isPrepend by remember {
        mutableStateOf(false)
    }
    val matchList = viewModel.matchTypeList.observeAsState()
    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val pagingData = viewModel.matchRecordPagingData.collectAsLazyPagingItems()
    isAppend = when (pagingData.loadState.append) {
        LoadState.Loading -> {
            isRefresh = false
            true
        }
        else -> {
            false
        }
    }
    isPrepend = when (pagingData.loadState.prepend) {
        LoadState.Loading -> {
            true
        }
        else -> {
            false
        }
    }
    val onLoading: () -> Unit = {
        isRefresh = true
    }
    LaunchedEffect(pagingData.loadState.refresh) {
        if (pagingData.loadState.refresh !is LoadState.Loading && pagingData.itemSnapshotList.size < 30) {
            isRefresh = false
        }
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
                        Text(text = "경기 기록", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = colorResource(id = R.color.app_color4))
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.app_color1)), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!matchList.value.isNullOrEmpty()) {
                    MatchTypeRow(accessId = accessId, onLoading = onLoading) {
                        matchList.value!!
                    }
                }
                if (!isRefresh) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .fillMaxHeight(1f), contentAlignment = Alignment.TopCenter
                    ) {
                        val clicked = remember { mutableStateListOf<Boolean>(*Array(300) { false }) }
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(color = colorResource(id = R.color.app_color8))) {
                            LazyColumn(modifier = Modifier.padding(5.dp).fillMaxSize()) {
                                itemsIndexed(pagingData) { index, item ->
                                    if (item != null) {
                                        DisplayCard(list = { item }, index = index, isExpanded = clicked[index]) {
                                            clicked[index] = !clicked[index]
                                        }
                                        Spacer(modifier = Modifier.fillParentMaxHeight(0.03f))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (!isRefresh && pagingData.itemSnapshotList.size == 0) {
            EmptyView()
        }
        if (isRefresh) {
            LoadingBar()
        }
        if (isAppend || isPrepend) {
            CircleLoadingBar()
        }
    }
}

@Composable
fun MatchTypeRow(accessId: String, onLoading: () -> Unit, list: () -> List<MatchTypeData>) {
    var selectedIndex by remember {
        mutableStateOf(-1)
    }
    val onItemClick = { index: Int -> selectedIndex = index }
    Box(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(5.dp), contentAlignment = Alignment.Center
    ) {
        LazyRow {
            itemsIndexed(list()) { index, item ->
                MatchListCard(accessId = accessId, item = item, index = index, onLoading = onLoading, selected = selectedIndex == index, onClick = onItemClick)
                Spacer(modifier = Modifier.fillParentMaxWidth(0.01f))
            }
        }
    }
}

@Composable
fun MatchListCard(accessId: String, item: MatchTypeData, index: Int, selected: Boolean, onLoading: () -> Unit, onClick: (Int) -> Unit, viewModel: MatchViewModel = hiltViewModel()) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = Color.Black, CircleShape)
            .clip(CircleShape)
            .background(if (selected) colorResource(id = R.color.app_color6) else colorResource(id = R.color.app_color8))
            .padding(15.dp)
            .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                onLoading.invoke()
                onClick.invoke(index)
                viewModel.getMatchRecordPagingData(accessId, item.matchType)
            }, contentAlignment = Alignment.Center

    ) {
        Text(text = item.desc, fontSize = 17.sp)
    }
}

@Composable
fun DisplayCard(list: () -> MatchDetailData, index: Int, isExpanded: Boolean, viewModel: MatchViewModel = hiltViewModel(), onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(20.dp))
            .background(
                color = when (list().result1) {
                    "승", "몰수승" -> colorResource(id = R.color.win).copy(alpha = 0.5f)
                    "패", "몰수패" -> colorResource(id = R.color.defeat).copy(alpha = 0.5f)
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
                Text(text = list().date.replace("T", "  "), fontSize = 15.sp)
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
                    Text(text = list().nickname1, fontSize = 14.sp, fontWeight = FontWeight.Bold)
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
                    Text(text = list().nickname2, fontSize = 14.sp, fontWeight = FontWeight.Bold)
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
                    Text(text = list().displayGoal1, fontSize = 20.sp, fontWeight = FontWeight.Bold)
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
                    Text(text = list().displayGoal2, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }
            Divider(color = Color.DarkGray, thickness = 1.dp)
            Box(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .animateContentSize()
            ) {
                if (isExpanded) {
                    DetailView { list() }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(interactionSource = MutableInteractionSource(), indication = null) {
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
fun DetailView(
    viewModel: MatchViewModel = hiltViewModel(), data: () -> MatchDetailData,
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxSize(0.95f)
                .padding(5.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.fillMaxWidth(0.333f), contentAlignment = Alignment.Center) {
                    Text(text = data.invoke().result1, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.fillMaxWidth(0.5f), contentAlignment = Alignment.Center) {
                    Text(text = "결과", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.fillMaxWidth(1f), contentAlignment = Alignment.Center) {
                    Text(text = data.invoke().result2, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.fillMaxWidth(0.333f), contentAlignment = Alignment.Center) {
                    Text(text = data.invoke().goal1, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.fillMaxWidth(0.5f), contentAlignment = Alignment.Center) {
                    Text(text = "골", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.fillMaxWidth(1f), contentAlignment = Alignment.Center) {
                    Text(text = data.invoke().goal2, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.fillMaxWidth(0.333f), contentAlignment = Alignment.Center) {
                    Text(text = data.invoke().totalShoot1, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.fillMaxWidth(0.5f), contentAlignment = Alignment.Center) {
                    Text(text = "전체슛", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.fillMaxWidth(1f), contentAlignment = Alignment.Center) {
                    Text(text = data.invoke().totalShoot2, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.fillMaxWidth(0.333f), contentAlignment = Alignment.Center) {
                    Text(text = data.invoke().validShoot1, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.fillMaxWidth(0.5f), contentAlignment = Alignment.Center) {
                    Text(text = "유효슛", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.fillMaxWidth(1f), contentAlignment = Alignment.Center) {
                    Text(text = data.invoke().validShoot2, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.fillMaxWidth(0.333f), contentAlignment = Alignment.Center) {
                    Text(text = "${data.invoke().shootRating1} %", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.fillMaxWidth(0.5f), contentAlignment = Alignment.Center) {
                    Text(text = "슈팅성공률", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.fillMaxWidth(1f), contentAlignment = Alignment.Center) {
                    Text(text = "${data.invoke().shootRating2} %", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.fillMaxWidth(0.333f), contentAlignment = Alignment.Center) {
                    Text(text = "${data.invoke().validPass1} %", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.fillMaxWidth(0.5f), contentAlignment = Alignment.Center) {
                    Text(text = "패스성공률", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.fillMaxWidth(1f), contentAlignment = Alignment.Center) {
                    Text(text = "${data.invoke().validPass2} %", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.fillMaxWidth(0.333f), contentAlignment = Alignment.Center) {
                    Text(text = "${data.invoke().validDefence1} %", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.fillMaxWidth(0.5f), contentAlignment = Alignment.Center) {
                    Text(text = "수비성공률", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.fillMaxWidth(1f), contentAlignment = Alignment.Center) {
                    Text(text = "${data.invoke().validDefence2} %", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.fillMaxWidth(0.333f), contentAlignment = Alignment.Center) {
                    Text(text = "${data.invoke().validTackle1} %", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.fillMaxWidth(0.5f), contentAlignment = Alignment.Center) {
                    Text(text = "태클성공률", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.fillMaxWidth(1f), contentAlignment = Alignment.Center) {
                    Text(text = "${data.invoke().validTackle2} %", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.fillMaxWidth(0.333f), contentAlignment = Alignment.Center) {
                    Text(text = "${data.invoke().possession1} %", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.fillMaxWidth(0.5f), contentAlignment = Alignment.Center) {
                    Text(text = "점유율", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.fillMaxWidth(1f), contentAlignment = Alignment.Center) {
                    Text(text = "${data.invoke().possession2} %", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.fillMaxWidth(0.333f), contentAlignment = Alignment.Center) {
                    Text(text = data.invoke().offsideCount1, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.fillMaxWidth(0.5f), contentAlignment = Alignment.Center) {
                    Text(text = "옵사이드", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.fillMaxWidth(1f), contentAlignment = Alignment.Center) {
                    Text(text = data.invoke().offsideCount2, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.fillMaxWidth(0.333f), contentAlignment = Alignment.Center) {
                    Text(text = data.invoke().yellowCards1, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.fillMaxWidth(0.5f), contentAlignment = Alignment.Center) {
                    Text(text = "경고", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.fillMaxWidth(1f), contentAlignment = Alignment.Center) {
                    Text(text = data.invoke().yellowCards2, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.fillMaxWidth(0.333f), contentAlignment = Alignment.Center) {
                    Text(text = data.invoke().redCards1, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.fillMaxWidth(0.5f), contentAlignment = Alignment.Center) {
                    Text(text = "퇴장", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.fillMaxWidth(1f), contentAlignment = Alignment.Center) {
                    Text(text = data.invoke().redCards2, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.fillMaxWidth(0.333f), contentAlignment = Alignment.Center) {
                    Text(text = data.invoke().foul1, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.fillMaxWidth(0.5f), contentAlignment = Alignment.Center) {
                    Text(text = "파울", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.fillMaxWidth(1f), contentAlignment = Alignment.Center) {
                    Text(text = data.invoke().foul2, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.fillMaxWidth(0.333f), contentAlignment = Alignment.Center) {
                    Text(text = data.invoke().injury1, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.fillMaxWidth(0.5f), contentAlignment = Alignment.Center) {
                    Text(text = "부상", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.fillMaxWidth(1f), contentAlignment = Alignment.Center) {
                    Text(text = data.invoke().injury2, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.fillMaxWidth(0.333f), contentAlignment = Alignment.Center) {
                    Text(text = "${data.invoke().averageRating1} / 10.0", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.fillMaxWidth(0.5f), contentAlignment = Alignment.Center) {
                    Text(text = "경기평점", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.fillMaxWidth(1f), contentAlignment = Alignment.Center) {
                    Text(text = "${data.invoke().averageRating2} / 10.0", fontSize = 17.sp, fontWeight = FontWeight.Bold)
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
