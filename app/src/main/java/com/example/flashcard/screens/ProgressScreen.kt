package com.example.flashcard.screens

import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flashcard.MainActivity
import com.example.flashcard.ui.theme.DeepOrange

@Preview
@Composable
fun ProgressScreen() {
    var Count by remember {
        mutableStateOf(0f)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        Text(
            text = " ${Count.toInt()} / 10",
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.size(width = 0.dp, height = 10.dp))
        ProgressBar(Count = Count)
        Spacer(modifier = Modifier.size(width = 0.dp, height = 10.dp))
        Button(onClick = { if (Count < 10) Count += 1 }) {
            Text("다음 카드로 넘어가기")
        }
    }
}

@Composable
fun ProgressBar(
    fontSize: TextUnit = 20.sp,
    color: Color = DeepOrange,
    animDuration: Int = 1000,
    animDelay: Int = 0,
    Count: Float,
) {
    val curPercentage by animateFloatAsState(
        targetValue = Count / 10,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = animDelay
        )
    )
    LinearProgressIndicator(
        progress = curPercentage,
        color = DeepOrange,
        backgroundColor = Color.LightGray
    )

}
