package com.example.myapp.ui.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapp.R
import com.example.myapp.utils.LoadingBar

@Composable
fun User(
    viewModel: UserViewModel = hiltViewModel()
) {
    val name by viewModel.userData.observeAsState()
    val level by viewModel.userLevel.observeAsState()
    val accessId by viewModel.userAccessId.observeAsState()
    val (text: String, setValue: (String) -> Unit) = remember {
        mutableStateOf("")
    }
    val isLoading by viewModel.isLoading.observeAsState()
    val focusManager = LocalFocusManager.current
    LaunchedEffect(Unit) {
//        viewModel.getBestRank("03704d4c66349949a799704a")
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                        }
                        .clip(RectangleShape)
                        .background(color = if (isClick) colorResource(id = R.color.light_red) else colorResource(id = R.color.lawn_green))
                        .border(width = 1.dp, color = Color.Red)
                        .size(30.dp),
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
                        modifier = Modifier
                            .fillMaxWidth(0.4f), contentAlignment = Alignment.Center
                    ) {
                        Image(modifier = Modifier.size(30.dp), painter = painterResource(id = R.drawable.ic_nickname), contentDescription = "")
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(1f), contentAlignment = Alignment.Center
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
                        modifier = Modifier
                            .fillMaxWidth(0.4f), contentAlignment = Alignment.Center
                    ) {
                        Text(text = "LV", fontSize = 25.sp, color = Color.Blue)
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(1f), contentAlignment = Alignment.Center
                    ) {
                        Text(text = level.toString(), fontSize = 25.sp)
                    }
                }
                var isExpanded by remember {
                    mutableStateOf(false)
                }
                var isMatchMenuExpanded by remember {
                    mutableStateOf(false)
                }
                var isTransactionMenuExpanded by remember {
                    mutableStateOf(false)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .background(Color.Yellow)
                            .clickable {
                                isExpanded = !isExpanded
                            }, contentAlignment = Alignment.Center
                    ) {
                        Text("조회하기")
                    }
                    DropdownMenu(modifier = Modifier.wrapContentSize(), expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
                        DropdownMenuItem(onClick = {
                            isExpanded = false
                            viewModel.getBestRank()
                        }) {
                            Text(text = "역대 최고 등급 조회", fontSize = 10.sp)
                        }
                        DropdownMenuItem(onClick = {
                            isExpanded = false
                            isMatchMenuExpanded = true
                        }) {
                            Text(text = "유저의 매치 기록 조회", fontSize = 10.sp)
                        }
                        DropdownMenuItem(onClick = {
                            isExpanded = false
                            isTransactionMenuExpanded = true
                        }) {
                            Text(text = "유저 거래 기록 조회", fontSize = 10.sp)
                        }
                    }
                    DropdownMenu(modifier = Modifier.wrapContentSize(), expanded = isMatchMenuExpanded, onDismissRequest = { isMatchMenuExpanded = false }) {
                        DropdownMenuItem(onClick = { isMatchMenuExpanded = false }) {
                            Text(text = "리그 친선", fontSize = 10.sp)
                        }
                        DropdownMenuItem(onClick = { isMatchMenuExpanded = false }) {
                            Text(text = "클래식 1on1", fontSize = 10.sp)
                        }
                        DropdownMenuItem(onClick = { isMatchMenuExpanded = false }) {
                            Text(text = "공식경기", fontSize = 10.sp)
                        }
                        DropdownMenuItem(onClick = { isMatchMenuExpanded = false }) {
                            Text(text = "감독모드", fontSize = 10.sp)
                        }
                        DropdownMenuItem(onClick = { isMatchMenuExpanded = false }) {
                            Text(text = "공식 친선", fontSize = 10.sp)
                        }
                        DropdownMenuItem(onClick = { isMatchMenuExpanded = false }) {
                            Text(text = "볼타 친선", fontSize = 10.sp)
                        }
                        DropdownMenuItem(onClick = { isMatchMenuExpanded = false }) {
                            Text(text = "볼타 공식", fontSize = 10.sp)
                        }
                    }
                    DropdownMenu(modifier = Modifier.wrapContentSize(), expanded = isTransactionMenuExpanded, onDismissRequest = { isTransactionMenuExpanded = false }) {
                        DropdownMenuItem(onClick = { isTransactionMenuExpanded = false }) {
                            Text(text = "구입 거래 조회", fontSize = 10.sp)
                        }
                        DropdownMenuItem(onClick = { isTransactionMenuExpanded = false }) {
                            Text(text = "판매 거래 조회", fontSize = 10.sp)
                        }
                    }
                }
            }
        }
    }
    if (isLoading!!) {
        LoadingBar()
    }
}