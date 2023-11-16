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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ssafyb109.bangrang.R
import com.ssafyb109.bangrang.ui.theme.lightSkyBlue
import com.ssafyb109.bangrang.ui.theme.skyBlue
import com.ssafyb109.bangrang.viewmodel.RankViewModel

@Composable
fun StampSet(
    navController: NavHostController,
    bangrangPercent: Double?,
    totalNum: Long?
) {

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
                    Image(
                        painter = painterResource(id = R.drawable.stampimg),
                        contentDescription = "Stamp",
                        modifier = Modifier
                            .size(80.dp)
                            .padding(4.dp)
                    )
                    Text(
                        if (totalNum != null)"${totalNum}개" else "0개",
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("모은 도장", color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.width(12.dp))

            // 두 번째 정사각형
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier.size(80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.bangrangpercent),
                        contentDescription = "Stamp",
                        modifier = Modifier
                            .size(80.dp)
                            .padding(4.dp)
                    )
                    Text(
                        text = if (bangrangPercent != null) "$bangrangPercent %" else "인터넷\n미연결",
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("방랑율", color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Bold)
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