package com.example.myapp.ui.transaction

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapp.R

@Composable
fun Transaction(
    viewModel: TransactionViewModel = hiltViewModel(),
    accessId: String
) {
    var isBuy by remember {
        mutableStateOf(true)
    }
    LaunchedEffect(Unit) {
        viewModel.getTransactionRecord(accessId, if (isBuy) "buy" else "sell")
    }
    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp), contentAlignment = Alignment.TopStart
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_back_24),
                modifier = Modifier
                    .size(30.dp)
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    ) { backPressedDispatcher?.onBackPressed() },
                contentDescription = "뒤로가기",
                contentScale = ContentScale.Crop
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
                Text(text = "거래 내역", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.03f))
            Row(modifier = Modifier.border(1.dp, color = Color.LightGray)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .background(if (isBuy) colorResource(id = R.color.app_color) else Color.White)
                        .padding(15.dp)
                        .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                            isBuy = true
                            viewModel.getTransactionRecord(accessId = accessId, "buy")
                        }, contentAlignment = Alignment.Center
                ) {
                    Text("구매내역", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = if (isBuy) Color.White else colorResource(id = R.color.app_color))
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .background(if (!isBuy) colorResource(id = R.color.app_color) else Color.White)
                        .padding(15.dp)
                        .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                            isBuy = false
                            viewModel.getTransactionRecord(accessId = accessId, "sell")
                        }, contentAlignment = Alignment.Center
                ) {
                    Text("판매내역", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = if (!isBuy) Color.White else colorResource(id = R.color.app_color))
                }
            }
        }
    }
}