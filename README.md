# Compose Curved Bottom Navigation View

A pure **Jetpack Compose** implementation of a beautiful, animated curved bottom navigation view. It preserves the classic Bezier curve UI but takes full advantage of Compose's state-driven architecture, avoiding the limitations of traditional XML.

## 🚀 Features

* **Pure Jetpack Compose:** Built strictly with `Canvas` and `Path` mapping for crisp, fluid rendering. No XML interoperability is required.
* **Context-Aware Behavior:**
  * **Scroll-based Hide/Show:** Automatically integrates with `LazyListState` to hide while scrolling down and reveal while scrolling up, optimizing screen real estate.
  * **Screen-based Modes:** Dynamically reacts to state changes (`DEFAULT`, `MINIMAL`, `HIGHLIGHT`), elegantly animating properties like height and curve depth.
* **State-Driven Architecture:** A clean separation of concerns leveraging MVVM and `StateFlow` to hoist navigation state up to the ViewModel.
* **Dynamic Animations:** Utilizes compose animation APIs (`animateDpAsState`, `animateFloatAsState`, `AnimatedVisibility`) to naturally transition between selections, visibility changes, and modes.
* **Customizable & Extendable:** No hardcoded strings, colors, or sizes - easily adaptable to your Material 3 theme!

---

## 📸 Demo & Screenshots

### Standard Layout (DEFAULT mode)
*(Replace with URL to a screenshot/GIF showcasing standard usage)*
![Default Layout](https://via.placeholder.com/800x400.png?text=Default+Curved+Navigation)

### Minimal & Highlight Modes
*(Replace with URL to a screenshot/GIF showing minimal mode and deep curve mode)*
![Minimal Layout](https://via.placeholder.com/800x400.png?text=Minimal+and+Highlight+Modes)

### Scroll-Aware Hiding
*(Replace with URL to a GIF showing the scroll interactions)*
![Scroll Hide Animation](https://via.placeholder.com/800x400.png?text=Scroll-based+Hide/Show+Demo)

---

## 🛠️ Architecture Overview

Following strict Clean Architecture and MVVM patterns:

* **UI Layer (Screen):** `MainScreen.kt` manages the `Scaffold` and `LazyColumn`. It reacts strictly to `NavState` emissions.
* **UI Layer (Components):** `CurvedBottomNav.kt` and `NavigationItem.kt` handle pure drawing and animation logic cleanly compartmentalized from business rules.
* **Business Logic Layer:** `MainViewModel.kt` owns the `NavState` (visibility, mode, selectedIndex) and exposes predictable `StateFlow` updates.

### File Structure Strictness
```
com.ub.composecurvedbottomnavigationview
 ┣ 📂 screen
 ┃ ┣ 📜 MainScreen.kt        # The single Screen entry point
 ┃ ┗ 📜 MainViewModel.kt     # The business logic driver
 ┗ 📂 components
   ┣ 📜 CurvedBottomNav.kt   # Reusable Compose Canvas logic
   ┗ 📜 NavigationItem.kt    # Individual interactable items
```

---

## 💻 Getting Started

### 1. Setup State

Define your items and state model:

```kotlin
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
```

### 2. Implement ViewModel Logic

Handle selection and visibility context changes from user events:

```kotlin
class MainViewModel : ViewModel() {
    private val _navState = MutableStateFlow(NavState())
    val navState: StateFlow<NavState> = _navState.asStateFlow()

    fun onItemSelected(index: Int) {
        _navState.update { it.copy(selectedIndex = index) }
    }

    // ... Implement scroll awareness updates
}
```

### 3. Add to Scaffold

Embed the `CurvedBottomNav` within an `AnimatedVisibility` wrapper inside your `Scaffold`'s `bottomBar`.

```kotlin
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
    // Your main screen content
}
```

---

## 👨‍💻 Constraints Adhered

* **No XML System Used:** Entire UI rendered manually on `Canvas` dynamically tracking sizes.
* **No hardcoded values:** Fully resourced localized strings and colors.
* **Memory conscious:** Path calculations use `remember {}` avoiding expensive re-allocations during 60fps animations.
* **No Comments in Source:** Ensuring perfectly readable self-documenting code.

## 📄 License
This project is licensed under the MIT License.