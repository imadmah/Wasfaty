package com.example.wasfaty.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.wasfaty.R
import com.example.wasfaty.models.entity.LoaderIntro
import com.example.wasfaty.models.entity.OnBoardingData
import com.example.wasfaty.ui.theme.Grey300
import com.example.wasfaty.ui.theme.OnboardingTheme
import com.example.wasfaty.ui.theme.RedLight
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

class OnBoarding : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.grey_900)
        setContent {
            OnboardingTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainFunction()
                }
            }
        }
    }

    private fun navToMainActivity() {
        val context = this
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun MainFunction() {
        val items = listOf(
            OnBoardingData(
                R.raw.whatdoieat,
                "What should I eat today?",
                "If you can't decide what to eat, Plan Your Weekly Meals now!"
            ),
            OnBoardingData(
                R.raw.foodcarousel,
                "Organize Your Favorite Recipes!",
                "Easily save and categorize your favorite recipes in one place."
            ),
            OnBoardingData(
                R.raw.orderfood,
                "Share Your Delicious Creations!",
                "Share your favorite recipes with friends and family."
            )
        )

        val pagerState = rememberPagerState()

        OnBoardingPager(
            item = items,
            pagerState = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
        )
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun OnBoardingPager(
        item: List<OnBoardingData>,
        pagerState: PagerState,
        modifier: Modifier = Modifier,
    ) {
        Box(modifier = modifier) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalPager(
                    count = item.size, // use 'count' instead of 'pageCount'
                    state = pagerState,
                ) { page ->
                    Column(
                        modifier = Modifier
                            .padding(60.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Assuming you have a LoaderIntro composable for displaying the image
                        LoaderIntro(
                            modifier = Modifier
                                .size(200.dp)
                                .fillMaxWidth()
                                .align(alignment = Alignment.CenterHorizontally),
                            image = item[page].image
                        )
                        Text(
                            text = item[page].title,
                            modifier = Modifier.padding(top = 50.dp),
                            color = Color.Black,
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = item[page].desc,
                            modifier = Modifier.padding(top = 30.dp, start = 20.dp, end = 20.dp),
                            color = Color.Black,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                PagerIndicator(size = item.size, currentPage = pagerState.currentPage)
            }

            Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                val coroutineScope = rememberCoroutineScope()

                BottomSection(
                    currentPage = pagerState.currentPage,
                    onSkipClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(item.size - 1) // Skip to last page
                        }
                    },
                    onNextClick = {
                        coroutineScope.launch {
                            if (pagerState.currentPage == item.size - 1) {
                                navToMainActivity()
                            } else {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    },
                    isSkipClicked = true
                )
            }
        }
    }

    @Composable
    fun PagerIndicator(
        size: Int,
        currentPage: Int
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(top = 60.dp)
        ) {
            repeat(size) {
                Indicator(isSelected = it == currentPage)
            }
        }
    }

    @Composable
    fun Indicator(isSelected: Boolean) {
        val width = animateDpAsState(targetValue = if (isSelected) 25.dp else 10.dp)

        Box(
            modifier = Modifier
                .padding(1.dp)
                .height(10.dp)
                .width(width.value)
                .clip(CircleShape)
                .background(
                    if (isSelected) RedLight else Grey300.copy(alpha = 0.5f)
                )
        )
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun BottomSection(
        currentPage: Int,
        onSkipClick: () -> Unit,
        onNextClick: () -> Unit,
        isSkipClicked: Boolean
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, bottom = 50.dp)
                .fillMaxWidth()
        ) {
            if (currentPage != 2) {
                if (isSkipClicked) {
                    Text(
                        text = "Skip",
                        color = RedLight,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(top = 10.dp, bottom = 10.dp)
                            .clickable {
                                onSkipClick()
                            }
                    )
                }
            }
            if (currentPage == 2) {
                Text(
                    text = "Done",
                    color = RedLight,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 10.dp)
                        .clickable(onClick = {
                            navToMainActivity()
                        })
                )
            } else {
                Text(
                    text = "Next",
                    color = RedLight,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 10.dp)
                        .clickable(onClick = {
                            onNextClick()
                        })
                )
            }
        }
    }
}
