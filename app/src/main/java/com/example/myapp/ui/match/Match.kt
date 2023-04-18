package com.example.myapp.ui.match

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.example.domain.entity.DetailMatchRecordEntity
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
                .padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally
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
            if (!displayList.value.isNullOrEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .fillMaxHeight(0.95f), contentAlignment = Alignment.Center
                ) {
                    var expanded by remember {
                        mutableStateOf(false)
                    }
                    LazyColumn {
                        items(displayList.value!!) {
                            DisplayCard(it, expanded) { expanded = !expanded }
                            Spacer(modifier = Modifier.fillParentMaxHeight(0.03f))
                        }
                    }
                }
            } else if (displayList.value.isNullOrEmpty() && isLoading.value == false) {
                EmptyView()
            }
        }
        if (isLoading.value!!) {
            LoadingBar()
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
fun DisplayCard(data: DisplayMatchData, expanded: Boolean, viewModel: MatchViewModel = hiltViewModel(), onClick: () -> Unit) {
    val detailData = viewModel.detailMatchRecordList.observeAsState(null)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(10.dp))
            .background(color = if (data.isWin) colorResource(id = R.color.win).copy(alpha = 0.5f) else colorResource(id = R.color.defeat).copy(alpha = 0.5f))
            .border(1.dp, color = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
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
                    Text(text = data.nickname1, fontSize = 13.sp, fontWeight = FontWeight.Bold)
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
                    Text(text = data.nickname2, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .fillMaxHeight(0.6f)
                    .padding(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.333f), contentAlignment = Alignment.Center
                ) {
                    Text(text = data.goal1, fontSize = 16.sp, fontWeight = FontWeight.Bold)
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
                    Text(text = data.goal2, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
            Divider(color = Color.DarkGray, thickness = 1.dp)
            Box(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
            ) {
                this@Column.AnimatedVisibility(visible = expanded && detailData.value != null, enter = expandIn(), exit = shrinkOut()) {
                    DetailView(detailData.value!!)
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                            onClick.invoke()
                            viewModel.getDetailData(data.matchId)
                        }, contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier
                            .size(30.dp)
                            .alpha(0.5f), painter = painterResource(id = if (expanded) R.drawable.ic_contract else R.drawable.ic_expand), contentDescription = ""
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
fun DetailView(data: DetailMatchRecordEntity) {
    Box(modifier = Modifier.fillMaxSize(0.95f), contentAlignment = Alignment.Center) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text("text")
            Text("text")
            Text("text")
            Text("text")
            Text("text")
            Text("text")
            Text("text")
            Text("text")
            Text("text")
            Text("text")
            Text("text")
            Text("text")
            Text("text")
            Text("text")
            Text("text")
            Text("text")
            Text("text")
            Text("text")
            Text("text")
            Text("text")
            Text("text")
            Text("text")
            Text("text")
            Text("text")
            Text("text")
            Text("text")
            Text("text")
            Text("text")
            Text("text")
            Text("text")
            Text("text")
            Text("text")
            Text("text")
            Text("text")
            Text("text")
            Text("text")
            Text("text")
        }
    }
}