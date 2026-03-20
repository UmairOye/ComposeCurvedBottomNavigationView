package com.ub.composecurvedbottomnavigationview.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.ub.composecurvedbottomnavigationview.R
import com.ub.composecurvedbottomnavigationview.screen.CurvedModel
import com.ub.composecurvedbottomnavigationview.screen.NavMode
import com.ub.composecurvedbottomnavigationview.screen.NavState
import com.ub.composecurvedbottomnavigationview.utils.sdp

@Composable
fun CurvedBottomNav(
    navState: NavState,
    menuItems: List<CurvedModel>,
    onItemClick: (Int) -> Unit
) {
    val density = LocalDensity.current

    val layoutHeightDp = when (navState.mode) {
        NavMode.MINIMAL -> 56.sdp
        else -> 84.sdp
    }

    val animatedHeight by animateDpAsState(
        targetValue = layoutHeightDp,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "animatedHeight"
    )

    val curveDepth by animateFloatAsState(
        targetValue = when (navState.mode) {
            NavMode.HIGHLIGHT -> 24f
            NavMode.MINIMAL -> 8f
            else -> 12f
        },
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "curveDepth"
    )

    val animatedSelectedIndex by animateFloatAsState(
        targetValue = navState.selectedIndex.toFloat(),
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "animatedSelectedIndex"
    )

    val navBgColor = colorResource(id = R.color.nav_bg_color)
    val path = remember { Path() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(animatedHeight)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val menuCellWidth = width / menuItems.size

            val currentOffsetX = (animatedSelectedIndex * menuCellWidth).toInt()

            val fabSize = with(density) { 56.sdp.toPx() }
            val cbnHeight = with(density) { 56.sdp.toPx() }
            val bottomNavOffsetY = height - cbnHeight
            val curveBottomOffset = with(density) { curveDepth.dp.toPx() }
            val fabTopOffset = with(density) { 8.sdp.toPx() }

            val fabRadius = fabSize / 2
            val fabMargin = height - fabSize - fabTopOffset - curveBottomOffset

            val topControlX = fabRadius + fabRadius / 2
            val topControlY = bottomNavOffsetY + fabRadius / 6
            val bottomControlX = fabRadius + (fabRadius / 2)
            val bottomControlY = fabRadius / 4
            val curveHalfWidth = fabRadius * 2 + fabMargin

            val firstCurveStart = Offset(
                currentOffsetX + (menuCellWidth / 2) - curveHalfWidth,
                bottomNavOffsetY
            )
            val firstCurveEnd = Offset(
                currentOffsetX + (menuCellWidth / 2f),
                height - curveBottomOffset
            )
            val firstCurveControlPoint1 = Offset(
                firstCurveStart.x + topControlX,
                topControlY
            )
            val firstCurveControlPoint2 = Offset(
                firstCurveEnd.x - bottomControlX,
                firstCurveEnd.y - bottomControlY
            )

            val secondCurveStart = Offset(firstCurveEnd.x, firstCurveEnd.y)
            val secondCurveEnd = Offset(
                currentOffsetX + (menuCellWidth / 2) + curveHalfWidth,
                bottomNavOffsetY
            )
            val secondCurveControlPoint1 = Offset(
                secondCurveStart.x + bottomControlX,
                secondCurveStart.y - bottomControlY
            )
            val secondCurveControlPoint2 = Offset(
                secondCurveEnd.x - topControlX,
                topControlY
            )

            path.reset()
            path.apply {
                moveTo(0f, bottomNavOffsetY)
                lineTo(firstCurveStart.x, firstCurveStart.y)
                cubicTo(
                    firstCurveControlPoint1.x, firstCurveControlPoint1.y,
                    firstCurveControlPoint2.x, firstCurveControlPoint2.y,
                    firstCurveEnd.x, firstCurveEnd.y
                )
                cubicTo(
                    secondCurveControlPoint1.x, secondCurveControlPoint1.y,
                    secondCurveControlPoint2.x, secondCurveControlPoint2.y,
                    secondCurveEnd.x, secondCurveEnd.y
                )
                lineTo(width, bottomNavOffsetY)
                lineTo(width, height)
                lineTo(0f, height)
                close()
            }

            drawPath(
                path = path,
                color = navBgColor
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.sdp)
                .align(Alignment.BottomCenter)
        ) {
            menuItems.forEachIndexed { index, item ->
                Box(modifier = Modifier.weight(1f)) {
                    NavigationItem(
                        item = item,
                        isSelected = index == navState.selectedIndex,
                        showLabel = navState.mode != NavMode.MINIMAL,
                        onClick = { onItemClick(index) }
                    )
                }
            }
        }

        val configuration = LocalConfiguration.current
        val screenWidthPx = with(density) { configuration.screenWidthDp.sdp.toPx() }
        val cellWidthPx = screenWidthPx / menuItems.size

        val activeItemCenterX = (animatedSelectedIndex * cellWidthPx) + (cellWidthPx / 2f)

        val fabSizeDp = 56.sdp
        val fabSizePx = with(density) { fabSizeDp.toPx() }

        val fabTopOffsetPx = with(density) { 8.sdp.toPx() }

        val fabCenterY = (fabSizePx / 2f) + fabTopOffsetPx

        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = (activeItemCenterX - (fabSizePx / 2f)).toInt(),
                        y = (fabCenterY - (fabSizePx / 2f)).toInt()
                    )
                }
                .size(fabSizeDp)
                .clip(CircleShape)
                .background(colorResource(id = R.color.fab_bg_color)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = menuItems[navState.selectedIndex].icon),
                contentDescription = stringResource(id = menuItems[navState.selectedIndex].title),
                modifier = Modifier.size(24.sdp),
                tint = Color.White
            )
        }
    }
}
