package com.woo.myapp.ui.main

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.woo.myapp.R

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
        if (System.currentTimeMillis() - backPressedTime <= 2000L) {
            (context as Activity).finish()
        } else {
            backPressedState = true
            Toast.makeText(context, "한 번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.background(color = colorResource(id = R.color.app_color2)), contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp), contentAlignment = Alignment.CenterStart
            ) {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(text = "FIFA Online 4 Support App", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = colorResource(id = R.color.app_color4))
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(id = R.color.app_color1)), contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp), contentAlignment = Alignment.Center
            ) {
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
                                .clip(CircleShape)
                                .background(color = if (isPressed) colorResource(id = R.color.app_color4) else colorResource(id = R.color.app_color3))
                                .clickable(interactionSource = interactionSource, indication = null) {
                                    moveUser.invoke()
                                }, contentAlignment = Alignment.Center
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(15.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
                            ) {
                                Spacer(modifier = Modifier.fillMaxWidth(0.05f))
                                Text(
                                    text = "유저정보",
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isPressed) colorResource(id = R.color.app_color5) else colorResource(id = R.color.app_color8)
                                )
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
                                .clip(CircleShape)
                                .background(color = if (isPressed) colorResource(id = R.color.app_color4) else colorResource(id = R.color.app_color3))
                                .clickable(interactionSource = interactionSource, indication = null) {
                                    moveRanker.invoke()
                                }, contentAlignment = Alignment.Center
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(15.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
                            ) {
                                Spacer(modifier = Modifier.fillMaxWidth(0.05f))
                                Text(
                                    text = "TOP 10,000이\n자주 사용하는 선수",
                                    fontSize = 25.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    color = if (isPressed) colorResource(id = R.color.app_color5) else colorResource(id = R.color.app_color8)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}