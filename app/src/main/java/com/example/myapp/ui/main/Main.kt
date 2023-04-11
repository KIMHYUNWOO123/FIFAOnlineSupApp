package com.example.myapp.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import com.example.myapp.R

@Composable
fun Main(
    moveUser: () -> Unit,
) {
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.25f)
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
                        .padding(15.dp), verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
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

                    }, contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp), verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(modifier = Modifier.size(30.dp), painter = painterResource(id = R.drawable.ic_match), contentDescription = null)
                    Spacer(modifier = Modifier.fillMaxWidth(0.05f))
                    Text(text = "매치정보", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.Black)
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

                    }, contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp), verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(modifier = Modifier.size(30.dp), painter = painterResource(id = R.drawable.ic_ranker), contentDescription = null)
                    Spacer(modifier = Modifier.fillMaxWidth(0.05f))
                    Text(text = "랭커정보", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            val interactionSource = remember { MutableInteractionSource() }
            val isPressed by interactionSource.collectIsPressedAsState()
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(width = 1.dp, color = if (isPressed) Color.Red else colorResource(id = R.color.app_color))
                    .clickable(interactionSource = interactionSource, indication = null) {

                    }, contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp), verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(modifier = Modifier.size(30.dp), painter = painterResource(id = R.drawable.ic_meta), contentDescription = null)
                    Spacer(modifier = Modifier.fillMaxWidth(0.05f))
                    Text(text = "메타정보", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                }
            }
        }
    }
}