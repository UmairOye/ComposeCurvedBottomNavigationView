package com.ub.composecurvedbottomnavigationview.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.background
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import com.ub.composecurvedbottomnavigationview.R
import com.ub.composecurvedbottomnavigationview.screen.CurvedModel
import com.ub.composecurvedbottomnavigationview.utils.sdp
import com.ub.composecurvedbottomnavigationview.utils.ssp

@Composable
fun NavigationItem(
    item: CurvedModel,
    isSelected: Boolean,
    showLabel: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    val iconTranslateY by animateFloatAsState(
        targetValue = if (isSelected) -20f else 0f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "iconTranslateY"
    )

    val iconAlpha by animateFloatAsState(
        targetValue = if (isSelected) 0f else 1f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "iconAlpha"
    )

    val textAlpha by animateFloatAsState(
        targetValue = if (!isSelected && showLabel) 1f else 0f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "textAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.offset(y = iconTranslateY.sdp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = item.icon),
                contentDescription = stringResource(id = item.title),
                modifier = Modifier
                    .size(24.sdp)
                    .alpha(iconAlpha),
                tint = colorResource(id = R.color.nav_item_unselected)
            )

            if (showLabel) {
                Spacer(modifier = Modifier.height(4.sdp))
                Text(
                    text = stringResource(id = item.title),
                    color = colorResource(id = R.color.nav_item_unselected),
                    fontSize = 12.ssp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.alpha(textAlpha)
                )
            }
        }

        val dotAlpha by animateFloatAsState(
            targetValue = if (isSelected) 1f else 0f,
            animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
            label = "dotAlpha"
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-3).sdp)
                .size(6.sdp)
                .alpha(dotAlpha)
                .clip(CircleShape)
                .background(colorResource(id = R.color.nav_item_selected)),
            contentAlignment = Alignment.Center
        ) {}
    }
}
