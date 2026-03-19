package com.ub.composecurvedbottomnavigationview.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ub.composecurvedbottomnavigationview.R
import com.ub.composecurvedbottomnavigationview.components.CurvedBottomNav

@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {
    val navState by viewModel.navState.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        var lastScrollOffset = 0
        var lastVisibleItemIndex = 0

        snapshotFlow { listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset }
            .collect { (visibleItemIndex, visibleItemScrollOffset) ->
                if (visibleItemIndex == lastVisibleItemIndex) {
                    val delta = visibleItemScrollOffset - lastScrollOffset
                    if (delta > 10) {
                        viewModel.setNavVisibility(false)
                    } else if (delta < -10) {
                        viewModel.setNavVisibility(true)
                    }
                } else if (visibleItemIndex > lastVisibleItemIndex) {
                    viewModel.setNavVisibility(false)
                } else {
                    viewModel.setNavVisibility(true)
                }

                lastVisibleItemIndex = visibleItemIndex
                lastScrollOffset = visibleItemScrollOffset
            }
    }

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = navState.isVisible,
                enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(300)) + expandVertically(animationSpec = tween(300)),
                exit = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(300)) + shrinkVertically(animationSpec = tween(300))
            ) {
                CurvedBottomNav(
                    navState = navState,
                    menuItems = viewModel.menuItems,
                    onItemClick = viewModel::onItemSelected
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize()
            ) {
                items(50) { index ->
                    Text(
                        text = stringResource(id = R.string.list_item_label, index),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }

            val titleText = stringResource(id = viewModel.menuItems[navState.selectedIndex].title)
            Text(
                text = stringResource(id = R.string.selected_mode_label, titleText, navState.mode.name),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
            )
        }
    }
}
