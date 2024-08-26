package com.example.wasfaty.view.navBar

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.wasfaty.R
import com.example.wasfaty.ui.theme.GreenMain
import com.example.wasfaty.ui.theme.SearchBar
import com.example.wasfaty.view.navBar.colorButtons.ColorButton
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Straight
import com.exyte.animatednavbar.animation.indendshape.StraightIndent
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius


@Composable
fun ColorButtonNavBar(onHomeClick: ()-> Unit,onBookmarkClick: ()-> Unit,onCalendarClick: ()-> Unit,onSettingsClick: ()-> Unit) {
    var selectedItem by remember { mutableIntStateOf(0) }
    var prevSelectedIndex by remember { mutableIntStateOf(0) }

    AnimatedNavigationBar(
        modifier = Modifier
            .height(80.dp),
        selectedIndex = selectedItem,
        ballColor = GreenMain,
        barColor = SearchBar,
        cornerRadius = shapeCornerRadius( 24.dp),
        ballAnimation = Straight(
            spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessVeryLow)
        ),
        indentAnimation = StraightIndent(
            indentWidth = 56.dp,
            indentHeight = 15.dp,
            animationSpec = tween(1000)
        )
    ) {
        colorButtons.forEachIndexed { index, it ->
            ColorButton(
                modifier = Modifier
                    .fillMaxSize(),

                prevSelectedIndex = prevSelectedIndex,
                selectedIndex = selectedItem,
                index = index,


                onClick = {
                    prevSelectedIndex = selectedItem
                    selectedItem = index
                    when (it.description) {
                        R.string.Home -> onHomeClick()
                        R.string.Bookmark -> onBookmarkClick()
                        R.string.Calendar -> onCalendarClick()
                        R.string.Settings -> onSettingsClick()

                        // Add more cases as needed
                    }
                },
                icon = it.icon,
                contentDescription = stringResource(id = it.description),
                animationType = it.animationType,
                background = it.animationType.background
            )
        }
    }
}