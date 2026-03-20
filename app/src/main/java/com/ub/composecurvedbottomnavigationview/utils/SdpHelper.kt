package com.ub.composecurvedbottomnavigationview.utils

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val Int.sdp: Dp
    get() = this.dp

val Float.sdp: Dp
    get() = this.dp

val Double.sdp: Dp
    get() = this.toFloat().dp

val Int.ssp: TextUnit
    get() = this.sp

val Float.ssp: TextUnit
    get() = this.sp

val Double.ssp: TextUnit
    get() = this.toFloat().sp
