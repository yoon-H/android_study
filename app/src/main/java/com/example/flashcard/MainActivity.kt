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
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flashcard.navigation.BottomNavigationBar
import com.example.flashcard.navigation.Screen
import com.example.flashcard.screens.*
import com.example.flashcard.ui.theme.DeepOrange

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

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
                        HomeScreen(navController)
                    }
                    composable(Screen.Search.route) {
                        showBottomBar(true)
                        SearchScreen(navController)
                    }
                    composable(Screen.Create.route) {
                        showBottomBar(false)
                        CreateScreen(navController)
                    }
                    composable(Screen.More.route) {
                        showBottomBar(true)
                        MoreScreen(navController)
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

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
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


@Composable
fun CardItemField() {
    val (frontText, setFrontText) = remember {
        mutableStateOf("")
    }

    val (backText, setBackText) = remember {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color.LightGray)
    ) {
        ConstraintLayout {
            val (front, back, delete, divider) = createRefs()
            TextField(
                value = frontText,
                onValueChange = setFrontText,
                modifier = Modifier
                    .constrainAs(front) {
                        top.linkTo(parent.top) //자신의 top을 parent의 top에 배치한다
                    }
                    .fillMaxWidth()
                    .padding(8.dp),
                textStyle = MaterialTheme.typography.h6,
                placeholder = {
                    Text(
                        text = "Front",
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.LightGray
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = DeepOrange,
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                maxLines = 2
            )
            Divider(
                modifier = Modifier
                    .constrainAs(divider) {
                        top.linkTo(front.bottom)
                    }
                    .fillMaxWidth()
                    .height(2.dp),
                color = Color.LightGray
            )
            TextField(
                value = backText,
                onValueChange = setBackText,
                modifier = Modifier
                    .constrainAs(back) {
                        // 본인의 top을 divider의 bottom에 붙인다.
                        top.linkTo(divider.bottom)
                    }
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(8.dp),
                textStyle = MaterialTheme.typography.body1,
                placeholder = {
                    Text(
                        text = "Back",
                        style = MaterialTheme.typography.body1,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.LightGray
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = DeepOrange,
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            IconButton(
                onClick = {

                },
                modifier = Modifier
                    .constrainAs(delete) {
                        bottom.linkTo(parent.bottom, 10.dp)
                        start.linkTo(parent.start, 8.dp)
                    }
            ) {
                Icon(imageVector = Icons.Outlined.Delete, contentDescription = "delete")
            }


        }
    }
}