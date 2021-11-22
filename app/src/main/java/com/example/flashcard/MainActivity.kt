package com.example.flashcard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flashcard.navigation.BottomNavigationBar
import com.example.flashcard.navigation.Screen
import com.example.flashcard.screens.*
import com.example.flashcard.ui.theme.DeepOrange
import com.example.flashcard.viewmodel.CheggViewModel
import com.google.accompanist.pager.ExperimentalPagerApi

class MainActivity : ComponentActivity() {
    @ExperimentalPagerApi //페이저를 쓸 수 있는 어노테이션(annotation)
    @ExperimentalComposeApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            val cheggViewModel: CheggViewModel = viewModel()

            val (bottomBarShown, showBottomBar) = remember {
                mutableStateOf(true)
            }

            Scaffold(
                bottomBar = {
                    if (bottomBarShown) {
                        BottomNavigationBar(navController = navController)
                    }
                }) {
                NavHost(navController = navController, startDestination = Screen.Home.route) {//경로따라 Navigation 이동
                    composable(Screen.Home.route) {
                        showBottomBar(true)
                        HomeScreen(navController, cheggViewModel)
                    }
                    composable(Screen.Search.route) {
                        showBottomBar(true)
                        SearchScreen(navController, cheggViewModel)
                    }
                    composable(Screen.Create.route) {
                        showBottomBar(false)
                        CreateScreen(navController, cheggViewModel)
                    }
                    composable(Screen.More.route) {
                        showBottomBar(true)
                        MoreScreen(navController, cheggViewModel)
                    }
                    composable(Screen.Deck.route + "/{deckTitle}/{cardNum}") { backStackEntry ->
                        // title과 cardsNum을 전달
                        val deckTitle =
                            backStackEntry.arguments?.getString("deckTitle") ?: "no title"
                        val cardsNum = backStackEntry.arguments?.getString("cardNum")?.toInt() ?: 0
                        showBottomBar(false)
                        DeckScreen(
                            navController = navController,
                            title = deckTitle,
                            cardsNum = cardsNum
                        )
                    }

                }

            }

        }
    }
}

@Composable
fun MainScreen() {
    val (count, setCount) = remember { // 현재 카드 수
        mutableStateOf(0f)
    }

    val totalCount = 7 // 총 카드 수

    LaunchedEffect(key1 = true){ // 애니메이션이 시작할 때 적용되는 효과로 안전하게 상태를 변경해주는 Composable
        setCount(1f) // progress bar의 상태를 0에서 1로 증가시킬 때 Setcount가 1f로 동작하게 해준다.
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ){
        // TopBar에 들어갈 텍스트
        Text(
            text = "${count.toInt()} / $totalCount",
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ProgressBar
        MyProgressBar(count = count, totalCount = totalCount)

        Spacer(modifier = Modifier.height(16.dp))

        // 버튼을 누르면 ProgressBar 상태가 바뀌면서 카드가 넘어간다.
        Row(
            Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { if (count > 1) setCount(count - 1) }
            ){
                Text("이전 카드")
            }

            Button(
                onClick = { if (count < totalCount) setCount(count + 1) }
            ){
                Text("다음 카드")
            }
        }
    }
}

@Composable
fun MyProgressBar(
    modifier: Modifier = Modifier,
    color: Color = DeepOrange,
    animDuration: Int = 300,
    animDelay: Int = 0,
    count: Float,
    totalCount: Int,
) {
    val curPercentage by animateFloatAsState(
        targetValue = count / totalCount,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = animDelay,
            easing = LinearOutSlowInEasing // easing function은 애니메이션의 움직임을 보정해주는 함수다.
        )
    )

    LinearProgressIndicator(
        modifier = Modifier
            .fillMaxWidth()
            .height(12.dp)
            .clip(CircleShape),
        progress = curPercentage,
        color = color,
        backgroundColor = Color.LightGray
    )
}

//@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Column(modifier = Modifier.padding(8.dp)) {
        Row() {
        }
    }
}

@Composable
fun DeckInSubject() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = Color.LightGray
            )
            .clickable {

            }
            .padding(16.dp)
    ) {
        Text(
            text = "recursion",
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "8 Cards",
            style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
    }
}

@Composable
fun StudyGuide() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = Color.LightGray
            )
            .clickable {

            }
            .padding(16.dp)) {
        Text(
            text = "c-plus-plus",
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "12 Decks · 207 Cards",
            style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
    }
}


@Composable
fun MyDeckItem() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = Color.LightGray
            )
            .clickable {
            }
            .padding(16.dp)) {
        Text(
            text = "recursion",
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "11 Cards",
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
            Icon(
                imageVector = Icons.Default.VisibilityOff,
                contentDescription = "visibility_off",
                tint = Color.Gray
            )
        }

    }
}


