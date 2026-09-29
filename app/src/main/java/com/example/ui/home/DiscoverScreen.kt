package com.example.ui.home

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.launch
import com.example.ui.components.FloatingBlurBalls
import com.example.ui.components.LiquidGlassCard
import com.example.ui.components.LiquidGlassTextField
import com.example.ui.components.MapLibreMapView
import com.example.ui.theme.DarkNavy
import com.example.ui.theme.GlassBorderLight
import com.example.ui.theme.GlassSurfaceMedium

/**
 * Screen for Discover Nearby Gyms.
 * Unified card design across default feed and search mode.
 */
@Composable
fun DiscoverScreen(
    onGymSelect: (GymLocation) -> Unit = {},
    onBackClick: () -> Unit = {},
    onTabSelected: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    var sortBy by remember { mutableStateOf("Distance: Nearest") }
    var isSearchActive by remember { mutableStateOf(false) }

    val filters = listOf("All", "Free weights", "Sauna", "Classes", "Open 24/7")

    val isSearching = isSearchActive || searchQuery.isNotBlank()

    Crossfade(targetState = isSearching, label = "discover_view_crossfade") { searching ->
        if (searching) {
            // Map + Draggable Nearby Gyms View (Shown while searching)
            DiscoverMapSearchView(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                selectedFilter = selectedFilter,
                onFilterSelected = { selectedFilter = it },
                sortBy = sortBy,
                onSortBySelected = { sortBy = it },
                filters = filters,
                onBackClick = {
                    searchQuery = ""
                    isSearchActive = false
                },
                onGymSelect = onGymSelect,
                onTabSelected = onTabSelected,
                modifier = modifier
            )
        } else {
            // Standard Discover UI (Default View)
            DiscoverDefaultView(
                searchQuery = searchQuery,
                onSearchQueryChange = {
                    searchQuery = it
                    if (it.isNotBlank()) isSearchActive = true
                },
                onSearchFocus = { isSearchActive = true },
                selectedFilter = selectedFilter,
                onFilterSelected = { selectedFilter = it },
                sortBy = sortBy,
                onSortBySelected = { sortBy = it },
                filters = filters,
                onBackClick = onBackClick,
                onGymSelect = onGymSelect,
                onTabSelected = onTabSelected,
                modifier = modifier
            )
        }
    }
}

/**
 * Standard Discover Feed View
 */
@Composable
private fun DiscoverDefaultView(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchFocus: () -> Unit,
    selectedFilter: String,
    onFilterSelected: (String) -> Unit,
    sortBy: String,
    onSortBySelected: (String) -> Unit,
    filters: List<String>,
    onBackClick: () -> Unit,
    onGymSelect: (GymLocation) -> Unit,
    onTabSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val isScrolling = listState.isScrollInProgress
    var isSortDropdownExpanded by remember { mutableStateOf(false) }

    val filteredGyms = remember(searchQuery, selectedFilter, sortBy) {
        val baseList = SAMPLE_GYMS.filter { gym ->
            val matchesQuery = searchQuery.isBlank() ||
                    gym.name.contains(searchQuery, ignoreCase = true) ||
                    gym.cityArea.contains(searchQuery, ignoreCase = true)
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

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkNavy)
    ) {
        // Cult.fit Aurora Animated Background Orbs
        FloatingBlurBalls()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = statusBarPadding + 10.dp, bottom = navBarPadding + 90.dp)
                .padding(horizontal = 18.dp)
        ) {
            // Header Row: Circular Dark Back Button + Discover Gyms Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(GlassSurfaceMedium)
                        .border(1.dp, GlassBorderLight, CircleShape)
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

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Discover Gyms",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFFFFF)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar (Capsule shape with #2C2B2C background)
            LiquidGlassTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = "Search by gym name or area...",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color(0xFF9E9D9E),
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { onSearchFocus() }
                    )
                },
                testTag = "discover_search_input"
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Top Sort Dropdown + Count Info Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Dropdown Sort Selector Button
                Box {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color(0xFF2D2C2D))
                            .border(1.dp, Color(0x22FFFFFF), RoundedCornerShape(50.dp))
                            .clickable { isSortDropdownExpanded = true }
                            .padding(horizontal = 14.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = sortBy,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFFFFFFF)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Dropdown",
                            tint = Color(0xFFB0B0B0),
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // Dropdown Menu
                    DropdownMenu(
                        expanded = isSortDropdownExpanded,
                        onDismissRequest = { isSortDropdownExpanded = false },
                        modifier = Modifier
                            .background(Color(0xFF262526))
                            .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(14.dp))
                    ) {
                        listOf(
                            "Distance: Nearest",
                            "Highest Rated",
                            "Name: A to Z"
                        ).forEach { option ->
                            val isChosen = (option == sortBy)
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = option,
                                        color = if (isChosen) Color(0xFFFFFFFF) else Color(0xFFCCCCCC),
                                        fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 13.sp
                                    )
                                },
                                onClick = {
                                    onSortBySelected(option)
                                    isSortDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // Count text
                Text(
                    text = "${filteredGyms.size} nearby",
                    fontSize = 12.sp,
                    color = Color(0xFF9E9D9E)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Filter Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filters) { filter ->
                    val isSelected = filter == selectedFilter
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(if (isSelected) Color(0xFFFFFFFF) else Color(0xFF2D2C2D))
                            .clickable { onFilterSelected(filter) }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
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

            Spacer(modifier = Modifier.height(16.dp))

            // Gym Cards List
            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filteredGyms, key = { it.id }) { gym ->
                    DiscoverGymCard(
                        gym = gym,
                        isSelected = false,
                        onClick = { onGymSelect(gym) }
                    )
                }
            }
        }

        // Bottom Nav Bar with scroll border fade
        GlassBottomBar(
            selectedTab = "Discover",
            onTabSelected = onTabSelected,
            isScrolling = isScrolling,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

/**
 * Unified Gym Card Component used across both Search & Default views
 */
@Composable
fun DiscoverGymCard(
    gym: GymLocation,
    isSelected: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LiquidGlassCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Gym Image Thumbnail
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Image(
                    painter = painterResource(id = gym.imageRes),
                    contentDescription = gym.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = gym.name,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFFFFF)
                )

                Spacer(modifier = Modifier.height(3.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color(0xFF9E9D9E),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "${gym.distance} · ${gym.cityArea}",
                        fontSize = 12.sp,
                        color = Color(0xFF9E9D9E)
                    )
                }

                Spacer(modifier = Modifier.height(7.dp))

                Text(
                    text = gym.tags.joinToString(" · "),
                    fontSize = 12.sp,
                    color = Color(0xFF8E8D8E)
                )
            }
        }
    }
}

/**
 * Search Mode: Interactive Full-Screen Dark Map + Draggable Bottom Sheet
 */
@Composable
private fun DiscoverMapSearchView(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedFilter: String,
    onFilterSelected: (String) -> Unit,
    sortBy: String,
    onSortBySelected: (String) -> Unit,
    filters: List<String>,
    onBackClick: () -> Unit,
    onGymSelect: (GymLocation) -> Unit,
    onTabSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedGym by remember { mutableStateOf<GymLocation?>(SAMPLE_GYMS.firstOrNull()) }
    val listState = rememberLazyListState()
    val isScrolling = listState.isScrollInProgress
    var isSortDropdownExpanded by remember { mutableStateOf(false) }

    val density = LocalDensity.current

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
            .background(Color(0xFF191819))
    ) {
        val screenHeightPx = constraints.maxHeight.toFloat()
        val minSheetHeightPx = with(density) { (130.dp + navBarPadding).toPx() }
        val halfSheetHeightPx = screenHeightPx * 0.48f
        val fullSheetHeightPx = screenHeightPx * 0.84f

        var targetSheetHeightPx by remember { mutableFloatStateOf(halfSheetHeightPx) }
        var isDragging by remember { mutableStateOf(false) }

        val animatedSheetHeightPx by animateFloatAsState(
            targetValue = targetSheetHeightPx,
            animationSpec = spring(dampingRatio = 0.85f, stiffness = 500f),
            label = "sheetHeightAnimation"
        )

        val currentSheetHeightPx = if (isDragging) targetSheetHeightPx else animatedSheetHeightPx
        val currentSheetHeightDp = with(density) { currentSheetHeightPx.toDp() }

        var recenterTrigger by remember { mutableIntStateOf(0) }

        // 1. FULL BACKGROUND: MapLibre GL JS + OpenFreeMap Dark Theme Vector Engine
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

            // Recenter FAB (floats above the bottom sheet)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp)
                    .offset(y = -currentSheetHeightDp - 14.dp)
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xD9262526))
                    .border(1.dp, Color(0x33FFFFFF), CircleShape)
                    .clickable { recenterTrigger++ },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "Recenter Map",
                    tint = Color(0xFFFFFFFF),
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // 2. FLOATING TOP OVERLAY: Back Button + Live Search Bar + Filter Pills
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
                // Back Button (Exits search mode and returns to default Discover UI)
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF2A292A))
                        .clickable { onBackClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Exit Search",
                        tint = Color(0xFFFFFFFF),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Search Bar with Clear button
                Box(modifier = Modifier.weight(1f)) {
                    LiquidGlassTextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChange,
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
                                        .clickable { onSearchQueryChange("") }
                                )
                            }
                        },
                        testTag = "map_search_input"
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Filter Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filters) { filter ->
                    val isSelected = filter == selectedFilter
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(if (isSelected) Color(0xFFFFFFFF) else Color(0xFF2D2C2D))
                            .clickable { onFilterSelected(filter) }
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

        // 3. DRAGGABLE UBER-STYLE SLIDING GLASS SHEET
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(currentSheetHeightDp)
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .background(Color(0xFF1E1D1E))
                .border(
                    width = 1.dp,
                    color = Color(0x22FFFFFF),
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
                    // Drag Handle Bar
                    Box(
                        modifier = Modifier
                            .size(width = 42.dp, height = 4.dp)
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
                                text = "Nearby Gyms",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFFFFF)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFF333233))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "${filteredGyms.size} found",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFE2E8F0)
                                )
                            }
                        }

                        // Top Sort Dropdown in Sheet Header
                        Box {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color(0xFF2B2A2B))
                                    .clickable { isSortDropdownExpanded = true }
                                    .padding(horizontal = 10.dp, vertical = 4.dp),
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
                                                color = if (isChosen) Color(0xFFFFFFFF) else Color(0xFFCCCCCC),
                                                fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Normal,
                                                fontSize = 12.sp
                                            )
                                        },
                                        onClick = {
                                            onSortBySelected(option)
                                            isSortDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                // Scrollable Nearby Gym List (Uses the same clean unified DiscoverGymCard)
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
            selectedTab = "Discover",
            onTabSelected = onTabSelected,
            isScrolling = isScrolling,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
