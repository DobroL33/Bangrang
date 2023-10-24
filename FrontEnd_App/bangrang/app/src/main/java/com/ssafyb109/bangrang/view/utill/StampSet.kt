package com.ssafyb109.bangrang.view.utill

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ssafyb109.bangrang.R
import com.ssafyb109.bangrang.ui.theme.lightSkyBlue
import com.ssafyb109.bangrang.ui.theme.skyBlue

@Composable
fun StampSet(navController: NavHostController) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .background(lightSkyBlue, shape = RoundedCornerShape(20.dp))
            .padding(top = 16.dp, start = 15.dp, end = 15.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            // 첫 번째 정사각형
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable {
                    navController.navigate("StampPage")
                }
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("8개", color = Color.Black, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("모은 도장", color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.width(12.dp))

            // 두 번째 정사각형
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                // 임시
                modifier = Modifier.clickable {
                    navController.navigate("StampPage")
                }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.stamp),
                    contentDescription = "Stamp",
                    modifier = Modifier
                        .size(80.dp)
                        .padding(4.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("정복도", color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.width(12.dp))

            // 세 번째 정사각형
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable {
                    navController.navigate("CollectionPage")
                }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.collectionbook),
                    contentDescription = "Collection Book",
                    modifier = Modifier
                        .size(80.dp)
                        .padding(4.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("도장 콜렉션", color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(40.dp))

        }
    }
}