package com.ssafyb109.bangrang.view.utill

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ssafyb109.bangrang.ui.theme.heavySkyBlue
import com.ssafyb109.bangrang.ui.theme.skyBlue

@Composable
fun SelectButton(
    onClick: () -> Unit,
    text: String,
    fonSize: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = heavySkyBlue,
    contentColor: Color = Color.White,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor,
        )
    ) {
        Text(text = text, fontSize = fonSize.sp, fontWeight = FontWeight.Bold)
    }
}
