package com.example.ui.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.LiquidGlassTextField
import com.example.ui.components.MapLibreMapView
import com.example.ui.theme.DarkNavy

/**
 * Dedicated Full-Screen Gym Map Screen
 * Styled after Apna's Job Map UI with Byce dark obsidian + neon green theme.
 */
@Composable
fun GymMapScreen(
    onGymSelect: (GymLocation) -> Unit = {},
    onBackClick: () -> Unit = {},
    onTabSelected: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    var sortBy by remember { mutableStateOf("Distance: Nearest") }
    var selectedGym by remember { mutableStateOf<GymLocation?>(SAMPLE_GYMS.firstOrNull()) }
    var isSortDropdownExpanded by remember { mutableStateOf(false) }
    var recenterTrigger by remember { mutableIntStateOf(0) }

    val listState = rememberLazyListState()
    val isScrolling = listState.isScrollInProgress
    val density = LocalDensity.current

    val filters = listOf("All", "Free weights", "Sauna", "Classes", "Open 24/7")

    val filteredGyms = remember(searchQuery, selectedFilter, sortBy) {
        val baseList = SAMPLE_GYMS.filter { gym ->
            val matchesQuery = searchQuery.isBlank() ||
                    gym.name.contains(searchQuery, ignoreCase = true) ||
                    gym.cityArea.contains(searchQuery, ignoreCase = true) ||
                    gym.tags.any { it.contains(searchQuery, ignoreCase = true) }
            val matchesFilter = when (selectedFilter) {
                "All" -> true
                "Free weights" -> gym.tags.contains("Free weights") || gym.tags.contains("Heavy lifting")
                "Sauna" -> gym.tags.contains("Sauna")
                "Classes" -> gym.tags.contains("Classes")
                "Open 24/7" -> gym.statusText.contains("24/7")
                else -> true
            }
            matchesQuery && matchesFilter
        }

        when (sortBy) {
            "Distance: Nearest" -> baseList.sortedBy { gym: GymLocation ->
                gym.distance.replace("km", "").replace("mi", "").trim().toDoubleOrNull() ?: 0.0
            }
            "Highest Rated" -> baseList.sortedByDescending { gym: GymLocation -> gym.rating }
            "Name: A to Z" -> baseList.sortedBy { gym: GymLocation -> gym.name }
            else -> baseList
        }
    }

    val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val navBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(DarkNavy)
    ) {
        val screenHeightPx = constraints.maxHeight.toFloat()
        val minSheetHeightPx = with(density) { (140.dp + navBarPadding).toPx() }
        val halfSheetHeightPx = screenHeightPx * 0.44f
        val fullSheetHeightPx = screenHeightPx * 0.82f

        var targetSheetHeightPx by remember { mutableFloatStateOf(halfSheetHeightPx) }
        var isDragging by remember { mutableStateOf(false) }

        val animatedSheetHeightPx by animateFloatAsState(
            targetValue = targetSheetHeightPx,
            animationSpec = spring(dampingRatio = 0.85f, stiffness = 500f),
            label = "gymMapSheetHeight"
        )

        val currentSheetHeightPx = if (isDragging) targetSheetHeightPx else animatedSheetHeightPx
        val currentSheetHeightDp = with(density) { currentSheetHeightPx.toDp() }

        // 1. FULL-SCREEN BACKGROUND: MapLibre GL JS Vector Map with OpenFreeMap Dark Theme
        Box(modifier = Modifier.fillMaxSize()) {
            MapLibreMapView(
                gyms = filteredGyms,
                selectedGym = selectedGym,
                recenterTrigger = recenterTrigger,
                onGymClicked = { gymId ->
                    val clicked = SAMPLE_GYMS.find { it.id == gymId }
                    if (clicked != null) {
                        selectedGym = clicked
                    }
                }
            )

            // Recenter Floating Action Button (above bottom sheet)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp)
                    .offset(y = -currentSheetHeightDp - 16.dp)
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Color(0xE6262526))
                    .border(1.dp, Color(0x33FFFFFF), CircleShape)
                    .clickable { recenterTrigger++ },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "Recenter Map",
                    tint = Color(0xFFA6CE39),
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        // 2. FLOATING TOP OVERLAY: Search Bar + Filter Pills
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = statusBarPadding + 10.dp, start = 16.dp, end = 16.dp)
                .align(Alignment.TopCenter)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back button to return to Home
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0xD9262526))
                        .border(1.dp, Color(0x33FFFFFF), CircleShape)
                        .clickable { onBackClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFFFFFFFF),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Search Bar
                Box(modifier = Modifier.weight(1f)) {
                    LiquidGlassTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = "Search gyms, amenities, areas...",
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color(0xFF9E9D9E),
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear",
                                    tint = Color(0xFF9E9D9E),
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clickable { searchQuery = "" }
                                )
                            }
                        },
                        testTag = "gym_map_search_input"
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Filter Pills Row
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filters) { filter ->
                    val isSelected = filter == selectedFilter
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(if (isSelected) Color(0xFFFFFFFF) else Color(0xD92D2C2D))
                            .border(
                                1.dp,
                                if (isSelected) Color.Transparent else Color(0x22FFFFFF),
                                RoundedCornerShape(50.dp)
                            )
                            .clickable { selectedFilter = filter }
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                    ) {
                        Text(
                            text = filter,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isSelected) Color(0xFF121212) else Color(0xFFFFFFFF)
                        )
                    }
                }
            }
        }

        // 3. DRAGGABLE BOTTOM SHEET: Gym List
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(currentSheetHeightDp)
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .background(Color(0xF21C1B1D))
                .border(
                    width = 1.dp,
                    color = Color(0x28FFFFFF),
                    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = navBarPadding + 76.dp)
            ) {
                // Drag Handle & Header Area
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .pointerInput(Unit) {
                            detectVerticalDragGestures(
                                onDragStart = { isDragging = true },
                                onDragEnd = {
                                    isDragging = false
                                    val distMin = kotlin.math.abs(targetSheetHeightPx - minSheetHeightPx)
                                    val distHalf = kotlin.math.abs(targetSheetHeightPx - halfSheetHeightPx)
                                    val distFull = kotlin.math.abs(targetSheetHeightPx - fullSheetHeightPx)

                                    targetSheetHeightPx = when {
                                        distMin < distHalf && distMin < distFull -> minSheetHeightPx
                                        distFull < distHalf -> fullSheetHeightPx
                                        else -> halfSheetHeightPx
                                    }
                                },
                                onDragCancel = { isDragging = false },
                                onVerticalDrag = { change, dragAmount ->
                                    change.consume()
                                    targetSheetHeightPx = (targetSheetHeightPx - dragAmount)
                                        .coerceIn(minSheetHeightPx, fullSheetHeightPx)
                                }
                            )
                        }
                        .padding(top = 10.dp, bottom = 8.dp, start = 20.dp, end = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Drag Handle
                    Box(
                        modifier = Modifier
                            .size(width = 44.dp, height = 4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color(0xFF555455))
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${filteredGyms.size} gyms nearby",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFFFFF)
                            )
                        }

                        // Sort Dropdown Menu
                        Box {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color(0xFF2B2A2B))
                                    .border(1.dp, Color(0x22FFFFFF), RoundedCornerShape(20.dp))
                                    .clickable { isSortDropdownExpanded = true }
                                    .padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = sortBy.substringBefore(":").ifEmpty { sortBy },
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFFB0B0B0)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Sort Dropdown",
                                    tint = Color(0xFF9E9D9E),
                                    modifier = Modifier.size(14.dp)
                                )
                            }

                            DropdownMenu(
                                expanded = isSortDropdownExpanded,
                                onDismissRequest = { isSortDropdownExpanded = false },
                                modifier = Modifier
                                    .background(Color(0xFF262526))
                                    .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(12.dp))
                            ) {
                                listOf("Distance: Nearest", "Highest Rated", "Name: A to Z").forEach { option ->
                                    val isChosen = (option == sortBy)
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = option,
                                                color = if (isChosen) Color(0xFFA6CE39) else Color(0xFFCCCCCC),
                                                fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Normal,
                                                fontSize = 12.sp
                                            )
                                        },
                                        onClick = {
                                            sortBy = option
                                            isSortDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                // Scrollable Gym Cards List
                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredGyms, key = { it.id }) { gym ->
                        val isSelected = gym.id == selectedGym?.id
                        DiscoverGymCard(
                            gym = gym,
                            isSelected = isSelected,
                            onClick = {
                                if (selectedGym?.id == gym.id) {
                                    onGymSelect(gym)
                                } else {
                                    selectedGym = gym
                                }
                            }
                        )
                    }
                }
            }
        }

        // 4. BOTTOM NAVIGATION BAR
        GlassBottomBar(
            selectedTab = "Gym Map",
            onTabSelected = onTabSelected,
            isScrolling = isScrolling,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
