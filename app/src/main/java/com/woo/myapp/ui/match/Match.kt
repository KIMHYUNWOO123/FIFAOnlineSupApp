package com.woo.myapp.ui.match

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.domain.entity.MatchDetailData
import com.example.domain.entity.MatchTypeData
import com.example.domain.entity.PlayerInfo
import com.woo.myapp.R
import com.woo.myapp.utils.LoadingBar

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
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(color = colorResource(id = R.color.app_color8))
                        ) {
                            LazyColumn(
                                modifier = Modifier
                                    .padding(5.dp)
                                    .fillMaxSize(),
                            ) {
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
            LoadingBar()
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
                Text(text = list().date, fontSize = 15.sp)
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
    var onSquad by remember {
        mutableStateOf(false)
    }
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
                    Text(text = "${data.invoke().averageRating1} / 5.0", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.fillMaxWidth(0.5f), contentAlignment = Alignment.Center) {
                    Text(text = "경기평점", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.fillMaxWidth(1f), contentAlignment = Alignment.Center) {
                    Text(text = "${data.invoke().averageRating2} / 5.0", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
            }
            Button(
                onClick = {
                    onSquad = true
                    viewModel.mapSquadList(
                        player1 = data.invoke().player1,
                        player2 = data.invoke().player2,
                        mvp1 = data.invoke().mvpPlayerSpId1,
                        mvp2 = data.invoke().mvpPlayerSpId2,
                        nickname1 = data.invoke().nickname1,
                        nickname2 = data.invoke().nickname2
                    )
                }, colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorResource(id = R.color.app_color8),
                    contentColor = Color.Black,
                    disabledBackgroundColor = colorResource(id = R.color.app_color8),
                    disabledContentColor = Color.Black
                )
            ) {
                Text("스쿼드 보기")
            }
        }
    }
    if (onSquad) {
        SquadDialog {
            onSquad = false
        }
    }
}

@Composable
fun EmptyView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "조회된 기록이 없습니다", fontSize = 15.sp)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SquadDialog(
    viewModel: MatchViewModel = hiltViewModel(), onHide: () -> Unit,
) {
    val data by viewModel.squadData.observeAsState()
    var isFlipped by remember { mutableStateOf(false) }
    val onClick: () -> Unit = {
        isFlipped = !isFlipped
    }
    val rotationY by animateFloatAsState(targetValue = if (isFlipped) 180f else 0f)
    Dialog(onDismissRequest = { onHide.invoke() }, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(
            modifier = Modifier
                .graphicsLayer {
                    this.rotationY = rotationY
                }
                .fillMaxSize(0.9f), contentAlignment = Alignment.Center
        ) {
            if (data != null) {
                if (isFlipped) {
                    CardView(isFlipped = isFlipped, data = { data!!.squad2 }, nickname1 = data!!.nickname2, nickname2 = data!!.nickname1, count = countSquad(data!!.squad2), onHide = onHide) {
                        onClick.invoke()
                    }
                } else {
                    CardView(isFlipped = isFlipped, data = { data!!.squad1 }, nickname1 = data!!.nickname1, nickname2 = data!!.nickname2, count = countSquad(data!!.squad1), onHide = onHide) {
                        onClick.invoke()
                    }
                }
            } else {
                LoadingBar()
            }
        }
    }
}

@Composable
fun CardView(data: () -> List<PlayerInfo>, nickname1: String, nickname2: String, isFlipped: Boolean, count: List<Int>, onHide: () -> Unit, onClick: () -> Unit) {
    var valueCount = 0
    count.forEach {
        if (it != 0) {
            valueCount += 1
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                this.rotationY = if (isFlipped) 180f else 0f
            }
            .background(Color.White), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceEvenly
    ) {
        if (data.invoke().isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(), contentAlignment = Alignment.Center
            ) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                    Box(modifier = Modifier
                        .wrapContentSize()
                        .padding(2.dp)
                        .clickable { onHide.invoke() })
                    Image(painter = painterResource(id = R.drawable.baseline_cancel_24), contentDescription = null)
                }
                Text(text = "$nickname1 의 스쿼드", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }
            if (count[6] != 0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(((1.0 / valueCount.toFloat()).toFloat())), contentAlignment = Alignment.Center
                ) {
                    LazyRow(modifier = Modifier.fillMaxSize(), userScrollEnabled = false, reverseLayout = true, horizontalArrangement = Arrangement.SpaceEvenly) {
                        items(count = count[6]) { i ->
                            PlayerImage(count = i + count[5] + count[4] + count[3] + count[2] + count[1] + count[0] + 1) { data.invoke() }
                        }
                    }
                }
            }
            if (count[5] != 0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(((1.0 / valueCount.toFloat()).toFloat())), contentAlignment = Alignment.Center
                ) {
                    LazyRow(modifier = Modifier.fillMaxSize(), userScrollEnabled = false, reverseLayout = true, horizontalArrangement = Arrangement.SpaceEvenly) {
                        items(count = count[5]) { i ->
                            PlayerImage(count = i + count[4] + count[3] + count[2] + count[1] + count[0] + 1) { data.invoke() }
                        }
                    }
                }
            }
            if (count[4] != 0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(((1.0 / valueCount.toFloat()).toFloat())), contentAlignment = Alignment.Center
                ) {
                    LazyRow(modifier = Modifier.fillMaxSize(), userScrollEnabled = false, reverseLayout = true, horizontalArrangement = Arrangement.SpaceEvenly) {
                        items(count = count[4]) { i ->
                            PlayerImage(count = i + count[3] + count[2] + count[1] + count[0] + 1) { data.invoke() }
                        }
                    }
                }
            }
            if (count[3] != 0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(((1.0 / valueCount.toFloat()).toFloat())), contentAlignment = Alignment.Center
                ) {
                    LazyRow(modifier = Modifier.fillMaxSize(), userScrollEnabled = false, reverseLayout = true, horizontalArrangement = Arrangement.SpaceEvenly) {
                        items(count = count[3]) { i ->
                            PlayerImage(count = i + count[2] + count[1] + count[0] + 1) { data.invoke() }
                        }
                    }
                }
            }
            if (count[2] != 0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(((1.0 / valueCount.toFloat()).toFloat())), contentAlignment = Alignment.Center
                ) {
                    LazyRow(modifier = Modifier.fillMaxSize(), userScrollEnabled = false, reverseLayout = true, horizontalArrangement = Arrangement.SpaceEvenly) {
                        items(count = count[2]) { i ->
                            PlayerImage(count = i + count[1] + count[0] + 1) { data.invoke() }
                        }
                    }
                }
            }
            if (count[1] != 0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(((1.0 / valueCount.toFloat()).toFloat())), contentAlignment = Alignment.Center
                ) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 5.dp), userScrollEnabled = false, reverseLayout = true, horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        items(count = count[0] + count[1]) { i ->
                            if (data.invoke()[i + 1].position == "RWB" || data.invoke()[i + 1].position == "LWB") {
                                PlayerImage(count = i + 1) { data.invoke() }
                            }
                        }
                    }
                }
            }
            if (count[0] != 0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(((1.0 / valueCount.toFloat()).toFloat())), contentAlignment = Alignment.Center
                ) {
                    LazyRow(modifier = Modifier.fillMaxSize(), userScrollEnabled = false, reverseLayout = true, horizontalArrangement = Arrangement.SpaceEvenly) {
                        items(count = count[0] + count[1]) { i ->
                            if (data.invoke()[i + 1].position != "RWB" && data.invoke()[i + 1].position != "LWB") {
                                PlayerImage(count = i + 1) { data.invoke() }
                            }
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(((1.0 / valueCount.toFloat()).toFloat())), contentAlignment = Alignment.Center
            ) {
                LazyRow(modifier = Modifier.fillMaxSize(), userScrollEnabled = false, reverseLayout = true, horizontalArrangement = Arrangement.Center) {
                    items(count = 1) { i ->
                        PlayerImage(count = 0) { data.invoke() }
                    }
                }
            }
            Divider(thickness = 1.dp, color = Color.LightGray)
            Box(contentAlignment = Alignment.Center) {
                Button(
                    onClick = { onClick.invoke() }, colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(id = R.color.app_color6),
                        contentColor = Color.Black,
                        disabledBackgroundColor = colorResource(id = R.color.app_color6),
                        disabledContentColor = Color.Black
                    )
                ) {
                    Text(text = "$nickname2 스쿼드 보기")
                }
            }
        } else {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = "조회된 선수기록이 없습니다.", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.padding(top = 30.dp), contentAlignment = Alignment.Center) {
                    Button(
                        onClick = { onClick.invoke() }, colors = ButtonDefaults.buttonColors(
                            backgroundColor = colorResource(id = R.color.app_color6),
                            contentColor = Color.Black,
                            disabledBackgroundColor = colorResource(id = R.color.app_color6),
                            disabledContentColor = Color.Black
                        )
                    ) {
                        Text(text = "$nickname2 스쿼드 보기")
                    }
                }
            }
        }
    }
}

fun countSquad(data: List<PlayerInfo>): List<Int> {
    var fw1: Int = 0
    var fw2: Int = 0
    var mf1: Int = 0
    var mf2: Int = 0
    var mf3: Int = 0
    var df1: Int = 0
    var df2: Int = 0
    for (item in data) {
        when (item.position) {
            "SW", "RB", "RCB", "CB", "LCB", "LB" -> df1 += 1
            "LWB", "RWB" -> df2 += 1
            "RDM", "CDM", "LDM" -> mf1 += 1
            "RM", "RCM", "CM", "LCM", "LM" -> mf2 += 1
            "RAM", "CAM", "LAM" -> mf3 += 1
            "RF", "CF", "LF" -> fw1 += 1
            "RW", "RS", "ST", "LS", "LW" -> fw2 += 1
        }
    }
    return listOf(df1, df2, mf1, mf2, mf3, fw1, fw2)
}

@Composable
fun PlayerImage(count: Int, data: () -> List<PlayerInfo>) {
    val color = when (data.invoke()[count].grade) {
        1 -> colorResource(id = R.color.basic)
        2, 3, 4 -> colorResource(id = R.color.bronze)
        5, 6, 7 -> colorResource(id = R.color.silver)
        else -> colorResource(id = R.color.gold)
    }
    val textColor = when (data.invoke()[count].grade) {
        1 -> colorResource(id = R.color.basicTextColor)
        2, 3, 4 -> colorResource(id = R.color.bronzeTextColor)
        5, 6, 7 -> colorResource(id = R.color.silverTextColor)
        else -> colorResource(id = R.color.goldTextColor)
    }
    val positionColor = when (data.invoke()[count].positionInt) {
        0 -> colorResource(id = R.color.gk)
        in 1..8 -> colorResource(id = R.color.df)
        in 9..19 -> colorResource(id = R.color.mf)
        else -> colorResource(id = R.color.fw)
    }
    Box(
        modifier = Modifier
            .fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top) {
            Box(modifier = Modifier.weight(0.2f), contentAlignment = Alignment.Center) {
                Text(text = data.invoke()[count].position, fontSize = 13.sp, color = positionColor, fontWeight = FontWeight.Bold)
            }
            Box(modifier = Modifier.weight(0.6f), contentAlignment = Alignment.BottomEnd) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https://fo4.dn.nexoncdn.co.kr/live/externalAssets/common/players/p${data.invoke()[count].pid}.png")
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f)
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .background(color = color)
                        .fillMaxHeight(0.25f)
                        .aspectRatio(1f), contentAlignment = Alignment.Center
                ) {
                    Text(text = data.invoke()[count].grade.toString(), color = textColor, fontWeight = FontWeight.Bold, fontSize = 8.sp)
                }
            }
            Box(modifier = Modifier.weight(0.2f), contentAlignment = Alignment.Center) {
                Row {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(data.invoke()[count].seasonImg)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxHeight(0.7f)
                            .aspectRatio(1f)
                    )
                    Text(
                        text = data.invoke()[count].name,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        textAlign = TextAlign.Center,
                        color = colorResource(id = R.color.app_color1)
                    )
                }
            }
        }
    }
}
