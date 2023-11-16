package com.ssafyb109.bangrang.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.ssafyb109.bangrang.R


@Composable
fun FloatingActionButtonDemo() {

    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(
            shape = CircleShape,
            modifier = Modifier
                .offset(y = 50.dp)
                .zIndex(10f)
                .scale(1.5f)
                .align(alignment = Alignment.BottomCenter),
            onClick = {},
        ) {
            Image(
                painter = painterResource(id = R.drawable.centralbutton),
                contentDescription = null,
                modifier = Modifier
                    .scale(1f)
                    .clip(CircleShape)
            )
        }
    }
}