package com.ub.composecurvedbottomnavigationview.screen

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.ub.composecurvedbottomnavigationview.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class NavMode {
    DEFAULT,
    MINIMAL,
    HIGHLIGHT
}

data class NavState(
    val isVisible: Boolean = true,
    val mode: NavMode = NavMode.DEFAULT,
    val selectedIndex: Int = 0
)

data class CurvedModel(
    @DrawableRes val icon: Int,
    @StringRes val title: Int
)

class MainViewModel : ViewModel() {

    private val _navState = MutableStateFlow(NavState())
    val navState: StateFlow<NavState> = _navState.asStateFlow()

    val menuItems = listOf(
        CurvedModel(R.drawable.ic_home, R.string.nav_home),
        CurvedModel(R.drawable.ic_search, R.string.nav_search),
        CurvedModel(R.drawable.ic_profile, R.string.nav_profile),
        CurvedModel(R.drawable.ic_settings, R.string.nav_settings)
    )

    fun onItemSelected(index: Int) {
        _navState.update { it.copy(selectedIndex = index) }
    }

    fun setNavVisibility(isVisible: Boolean) {
        if (_navState.value.isVisible != isVisible) {
            _navState.update { it.copy(isVisible = isVisible) }
        }
    }

    fun setNavMode(mode: NavMode) {
        _navState.update { it.copy(mode = mode) }
    }
}
