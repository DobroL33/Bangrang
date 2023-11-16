package com.ssafyb109.bangrang.view

import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafyb109.bangrang.R
import com.ssafyb109.bangrang.api.MyRankDTO
import com.ssafyb109.bangrang.api.TotalRegionDTO
import com.ssafyb109.bangrang.ui.theme.heavySkyBlue
import com.ssafyb109.bangrang.view.utill.BarGraph
import com.ssafyb109.bangrang.view.utill.HalfPieGraph

@Composable
fun RankMyPage(
    animationLaunch : Int,
    allRankResponse : MyRankDTO?,
    totlaRankResponse : TotalRegionDTO?,
) {
    val allRate = allRankResponse?.rating // 상위 몇 %
    val topRate = allRankResponse?.korea?.get(0)?.percent?.div(100) // 정복도
    val topRank = totlaRankResponse?.myRank?.myRatings?.get(0)?.rate?.toInt() // 순위

    val cityRanks = totlaRankResponse?.myRank?.myRatings?.map { myRegionRankDTO ->
        Pair(myRegionRankDTO.region, myRegionRankDTO.rate.toInt() to myRegionRankDTO.percent.toInt())
    } ?: emptyList()

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "전국 정복도", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            if (allRate != null && topRank != null && topRate != null) {
                // 정복도 % , 순위 , 상위 %
                HalfPieGraph(topRate,topRank,allRate,animationLaunch)
            }
            else{
                HalfPieGraph(0.0,0,0,animationLaunch)
            }
        }

        Text(text = "지역별 정복도", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Box(modifier = Modifier.height(200.dp)) {
            BarGraph(cityRanks)
        }
        Spacer(modifier = Modifier.height(8.dp))

        cityRanks.forEach { (city, rankInfo) ->
            RankRate(cityName = city, rank = rankInfo.first, percentage = rankInfo.second)
            Divider()
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun RankRate(cityName: String, rank: Int, percentage: Int) {
    val medal = when(rank) {
        1 -> painterResource(id = R.drawable.first)
        2 -> painterResource(id = R.drawable.second)
        3 -> painterResource(id = R.drawable.third)
        else -> null
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (medal != null) {
            Image(painter = medal, contentDescription = "$rank 등 메달", modifier = Modifier.size(30.dp))
        } else {
            Text(
                text = rank.toString(),
                color = heavySkyBlue,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
        Text(text = cityName, fontSize = 20.sp)
        Text(text = "$percentage%", fontSize = 20.sp, color = heavySkyBlue, fontWeight = FontWeight.Bold)
    }
}

