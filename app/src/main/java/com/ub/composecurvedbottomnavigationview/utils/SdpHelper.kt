package com.ub.composecurvedbottomnavigationview.utils


import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp


val Int.sdp: Dp
    @Composable
    get() = this.sdpGet()

val Int.ssp: TextUnit
    @Composable get() = this.textSdp(density = LocalDensity.current)

@Composable
private fun Int.textSdp(density: Density): TextUnit = with(density) {
    this@textSdp.sdp.toSp()
}

private const val SDP_BASE_WIDTH_DP = 310f
@Composable
private fun getFieldId(id: String): Int {
    val context = LocalContext.current
    return context.resources.getIdentifier(id, "dimen", context.packageName)

}


val Float.sdp: Dp
    @Composable
    get() = this.sdpGet()

val Float.ssp: TextUnit
    @Composable
    get() = this.textSdp(density = LocalDensity.current)

val Double.sdp: Dp
    @Composable
    get() = this.toFloat().sdpGet()

val Double.ssp: TextUnit
    @Composable
    get() = this.toFloat().textSdp(density = LocalDensity.current)

@Composable
private fun Float.textSdp(density: Density): TextUnit = with(density) {
    this@textSdp.sdp.toSp()
}



@Composable
private fun Int.sdpGet(): Dp {
    val config = LocalConfiguration.current
    val screenWidthDp = config.screenWidthDp
    val scale = screenWidthDp / SDP_BASE_WIDTH_DP
    return (this * scale).dp
}

@Composable
private fun Float.sdpGet(): Dp {
    val config = LocalConfiguration.current
    val screenWidthDp = config.screenWidthDp
    val scale = screenWidthDp / SDP_BASE_WIDTH_DP
    return (this * scale).dp
}