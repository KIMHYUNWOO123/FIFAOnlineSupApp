package com.example.myapp.ui.main

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapp.R

@Composable
fun Main(
    moveUser: () -> Unit,
    moveRanker: () -> Unit,
) {
    val context = LocalContext.current
    var backPressedState by remember {
        mutableStateOf(true)
    }
    var backPressedTime = 0L
    BackHandler(enabled = backPressedState) {
        if (System.currentTimeMillis() - backPressedTime <= 400L) {
            (context as Activity).finish()
        } else {
            backPressedState = true
            Toast.makeText(context, "한 번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.05f)
                .background(color = colorResource(id = R.color.white)), contentAlignment = Alignment.CenterStart
        ) {
            Row {
                Image(painter = painterResource(id = R.drawable.icon), contentDescription = "")
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "FIFA Online Support App")
                }
            }
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(modifier = Modifier.fillMaxHeight(0.8f), verticalArrangement = Arrangement.SpaceEvenly) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.333f)
                        .padding(10.dp)
                ) {
                    val interactionSource = remember { MutableInteractionSource() }
                    val isPressed by interactionSource.collectIsPressedAsState()
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(width = 1.dp, color = if (isPressed) Color.Red else colorResource(id = R.color.app_color))
                            .clickable(interactionSource = interactionSource, indication = null) {
                                moveUser.invoke()
                            }, contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(15.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
                        ) {
                            Image(modifier = Modifier.size(30.dp), painter = painterResource(id = R.drawable.ic_user), contentDescription = null)
                            Spacer(modifier = Modifier.fillMaxWidth(0.05f))
                            Text(text = "유저정보", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f)
                        .padding(10.dp)
                ) {
                    val interactionSource = remember { MutableInteractionSource() }
                    val isPressed by interactionSource.collectIsPressedAsState()
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(width = 1.dp, color = if (isPressed) Color.Red else colorResource(id = R.color.app_color))
                            .clickable(interactionSource = interactionSource, indication = null) {
                                moveRanker.invoke()
                            }, contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(15.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
                        ) {
                            Image(modifier = Modifier.size(30.dp), painter = painterResource(id = R.drawable.ic_meta), contentDescription = null)
                            Spacer(modifier = Modifier.fillMaxWidth(0.05f))
                            Text(text = "TOP 10,000\n랭커들이\n사용한 선수", fontSize = 25.sp, fontWeight = FontWeight.Bold, color = Color.Black, textAlign = TextAlign.Center)
                        }
                    }
                }
            }
        }
    }
}