package com.example.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.LiquidGlassCard
import com.example.ui.components.FloatingBlurBalls
import com.example.ui.theme.ByceGreen
import com.example.ui.theme.ByceGreenGlow
import com.example.ui.theme.CharcoalSurface
import com.example.ui.theme.DarkNavy
import com.example.ui.theme.DarkNavyDepth
import com.example.ui.theme.GlassBorderLight
import com.example.ui.theme.GlassBorderSpecular
import com.example.ui.theme.GlassSurfaceLight
import com.example.ui.theme.GlassSurfaceMedium
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSubtle
import com.example.ui.theme.TextWhite

// Data Models for Home Screen
enum class MembershipStatus {
    ACTIVE,
    NO_MEMBERSHIP,
    PAST_DUE
}

data class GymLocation(
    val id: String,
    val name: String,
    val distance: String,
    val cityArea: String,
    val imageRes: Int,
    val tags: List<String>,
    val statusText: String,
    val address: String = "100 Market St, San Francisco, CA",
    val hours: String = "6:00 AM - 10:00 PM",
    val latitude: Double = 37.789172,
    val longitude: Double = -122.401449,
    val rating: Double = 4.9
)

data class VisitLog(
    val id: String,
    val gymName: String,
    val dateText: String,
    val timeText: String,
    val status: String = "Checked in",
    val imageRes: Int = R.drawable.pulse_fitness_gym_1789554105342
)

// Sample Data
val SAMPLE_GYMS = listOf(
    GymLocation(
        id = "1",
        name = "Pulse Fitness",
        distance = "1.2 km away",
        cityArea = "Downtown · SF",
        imageRes = R.drawable.pulse_fitness_gym_1789554105342,
        tags = listOf("Free weights", "Classes", "Sauna"),
        statusText = "Open · Quiet now",
        latitude = 37.789172,
        longitude = -122.401449
    ),
    GymLocation(
        id = "2",
        name = "Iron Vault Gym",
        distance = "2.5 km away",
        cityArea = "SoMa · SF",
        imageRes = R.drawable.iron_vault_gym_1789554122844,
        tags = listOf("Heavy lifting", "Sauna", "Turf"),
        statusText = "Open · Moderate crowd",
        latitude = 37.778519,
        longitude = -122.395232
    ),
    GymLocation(
        id = "3",
        name = "Zenith Health Club",
        distance = "3.8 km away",
        cityArea = "Marina · SF",
        imageRes = R.drawable.zenith_health_club_1789554140602,
        tags = listOf("Cardio deck", "Pool", "Yoga"),
        statusText = "Open 24/7",
        latitude = 37.800542,
        longitude = -122.436128
    )
)

val SAMPLE_VISITS = listOf(
    VisitLog("v1", "Pulse Fitness", "Today", "6:42 PM", imageRes = R.drawable.pulse_fitness_gym_1789554105342),
    VisitLog("v2", "Iron Vault Gym", "Yesterday", "7:15 AM", imageRes = R.drawable.iron_vault_gym_1789554122844),
    VisitLog("v3", "Pulse Fitness", "Sep 12", "5:30 PM", imageRes = R.drawable.pulse_fitness_gym_1789554105342)
)

/**
 * Screen 3 — Home for Byce Member App
 */
@Composable
fun HomeScreen(
    memberName: String = "Alex",
    onNavigateToCheckIn: () -> Unit = {},
    onNavigateToGymDetail: (GymLocation) -> Unit = {},
    onNavigateToDiscover: () -> Unit = {},
    onNavigateToMembership: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToHistory: () -> Unit = {},
    onNavigateToBrowsePlans: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf("Home") }
    var membershipState by remember { mutableStateOf(MembershipStatus.ACTIVE) }
    val scrollState = rememberScrollState()

    val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val navBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        DarkNavy,
                        DarkNavyDepth,
                        Color(0xFF161415)
                    )
                )
            )
    ) {
        // Ambient Moving Blur Orbs
        FloatingBlurBalls()

        // Scrollable Content Container
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = navBarPadding + 90.dp)
        ) {
            // --------------------------------------------------
            // 1. TOP HERO CARD (Touches top & both sides, rounded bottom)
            // --------------------------------------------------
            TopHeroMembershipCard(
                memberName = memberName,
                status = membershipState,
                statusBarPadding = statusBarPadding,
                onProfileClick = onNavigateToProfile,
                onBrowsePlans = onNavigateToBrowsePlans,
                onManageMembership = onNavigateToMembership
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Main Content Container
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                // --------------------------------------------------
                // 2. PRIMARY CHECK-IN ACTION
                // --------------------------------------------------
                PrimaryCheckInCard(
                    hasActiveMembership = (membershipState == MembershipStatus.ACTIVE),
                    onCheckInClick = onNavigateToCheckIn,
                    onBrowsePlansClick = onNavigateToBrowsePlans
                )

                Spacer(modifier = Modifier.height(28.dp))

                // --------------------------------------------------
                // 3. NEARBY GYMS
                // --------------------------------------------------
                NearbyGymsSection(
                    gyms = SAMPLE_GYMS,
                    onSeeAllClick = onNavigateToDiscover,
                    onGymClick = onNavigateToGymDetail
                )

                Spacer(modifier = Modifier.height(28.dp))

                // --------------------------------------------------
                // 4. RECENT VISITS
                // --------------------------------------------------
                RecentActivitySection(
                    visits = SAMPLE_VISITS,
                    onViewHistoryClick = onNavigateToHistory
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // --------------------------------------------------
        // 5. BOTTOM NAVIGATION BAR
        // --------------------------------------------------
        GlassBottomBar(
            selectedTab = selectedTab,
            isScrolling = scrollState.isScrollInProgress,
            onTabSelected = { tab ->
                selectedTab = tab
                when (tab) {
                    "Discover" -> onNavigateToDiscover()
                    "Membership" -> onNavigateToMembership()
                    "Profile" -> onNavigateToProfile()
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

// --------------------------------------------------
// COMPONENT 1: TOP HERO MEMBERSHIP CARD
// --------------------------------------------------
@Composable
private fun TopHeroMembershipCard(
    memberName: String,
    status: MembershipStatus,
    statusBarPadding: androidx.compose.ui.unit.Dp,
    onProfileClick: () -> Unit,
    onBrowsePlans: () -> Unit,
    onManageMembership: () -> Unit
) {
    LiquidGlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp, topStart = 0.dp, topEnd = 0.dp),
        shadowElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = statusBarPadding + 14.dp, bottom = 22.dp)
                .padding(horizontal = 20.dp)
        ) {
            // Header Row: Greeting + Profile Avatar
            HomeTopHeader(
                memberName = memberName,
                onProfileClick = onProfileClick
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Membership Status Content
            MembershipStatusDetails(
                status = status,
                onBrowsePlans = onBrowsePlans,
                onManageMembership = onManageMembership
            )
        }
    }
}

// --------------------------------------------------
// COMPONENT 2: TOP HEADER
// --------------------------------------------------
@Composable
private fun HomeTopHeader(
    memberName: String,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Good afternoon, $memberName",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = TextSubtle
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Ready to train?",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
        }

        // Notification Icon without background
        IconButton(
            onClick = onProfileClick,
            modifier = Modifier.testTag("home_notification_button")
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = TextWhite,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

// --------------------------------------------------
// COMPONENT 3: MEMBERSHIP STATUS DETAILS
// --------------------------------------------------
@Composable
private fun MembershipStatusDetails(
    status: MembershipStatus,
    onBrowsePlans: () -> Unit,
    onManageMembership: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        when (status) {
            MembershipStatus.ACTIVE -> {
                // Header Row: "Your Membership" & "Active" Chip
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "YOUR MEMBERSHIP",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSubtle,
                        letterSpacing = 1.sp
                    )

                    // Active Status Indicator (dot + text only, no background/border)
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(NeonGreen)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Active",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonGreen
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Plan Title & Details
                Column {
                    Text(
                        text = "Byce Monthly",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Renews Oct 16",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }
            }

            MembershipStatus.NO_MEMBERSHIP -> {
                Text(
                    text = "MEMBERSHIP STATUS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSubtle,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "You don’t have a membership yet",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Choose a plan to start training at Byce partner gyms.",
                    fontSize = 13.sp,
                    color = TextMuted
                )

                Spacer(modifier = Modifier.height(14.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(ByceGreen)
                        .clickable { onBrowsePlans() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Browse plans",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkNavy
                    )
                }
            }

            MembershipStatus.PAST_DUE -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "MEMBERSHIP NOTICE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF6B6B),
                        letterSpacing = 1.sp
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x30FF6B6B))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = Color(0xFFFF6B6B),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Past Due",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF6B6B)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Payment past due",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Update your billing information to reactivate your gym access.",
                    fontSize = 13.sp,
                    color = TextMuted
                )

                Spacer(modifier = Modifier.height(14.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF2E1A1A))
                        .border(1.dp, Color(0xFFFF6B6B), RoundedCornerShape(12.dp))
                        .clickable { onManageMembership() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Resolve payment issue",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF6B6B)
                    )
                }
            }
        }
    }
}

// --------------------------------------------------
// COMPONENT 4: PRIMARY CHECK-IN ACTION
// --------------------------------------------------
@Composable
private fun PrimaryCheckInCard(
    hasActiveMembership: Boolean,
    onCheckInClick: () -> Unit,
    onBrowsePlansClick: () -> Unit
) {
    if (hasActiveMembership) {
        LiquidGlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onCheckInClick() }
                .testTag("home_primary_checkin_card"),
            shape = RoundedCornerShape(20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Scanner Icon (no background)
                Icon(
                    imageVector = Icons.Default.QrCodeScanner,
                    contentDescription = "Check in QR",
                    tint = TextWhite,
                    modifier = Modifier.size(30.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Check in",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Show your Byce access code at the gym",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = ByceGreen,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    } else {
        // Disabled / Membership required state
        LiquidGlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Scanner Icon (no background)
                Icon(
                    imageVector = Icons.Default.QrCodeScanner,
                    contentDescription = null,
                    tint = TextSubtle,
                    modifier = Modifier.size(28.dp)
                )

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Membership required",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Active plan needed to generate gym pass",
                        fontSize = 12.sp,
                        color = TextSubtle
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(GlassSurfaceMedium)
                        .border(1.dp, GlassBorderLight, RoundedCornerShape(10.dp))
                        .clickable { onBrowsePlansClick() }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Plans",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                }
            }
        }
    }
}

// --------------------------------------------------
// COMPONENT 5: NEARBY GYMS
// --------------------------------------------------
@Composable
private fun NearbyGymsSection(
    gyms: List<GymLocation>,
    onSeeAllClick: () -> Unit,
    onGymClick: (GymLocation) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Nearby gyms",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )

            Text(
                text = "See all",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = ByceGreen,
                modifier = Modifier
                    .clickable { onSeeAllClick() }
                    .testTag("home_see_all_gyms")
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(end = 4.dp)
        ) {
            items(gyms, key = { it.id }) { gym ->
                CompactGymCard(
                    gym = gym,
                    onClick = { onGymClick(gym) }
                )
            }
        }
    }
}

@Composable
private fun CompactGymCard(
    gym: GymLocation,
    onClick: () -> Unit
) {
    LiquidGlassCard(
        modifier = Modifier
            .width(220.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Gym Image Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(115.dp)
                    .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
            ) {
                Image(
                    painter = painterResource(id = gym.imageRes),
                    contentDescription = gym.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Dark gradient overlay for contrast
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color(0xAA222021))
                            )
                        )
                )

                // Status chip overlay
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xCC222021))
                        .padding(horizontal = 9.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = gym.statusText,
                        fontSize = 10.sp,
                        color = ByceGreen,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Card Body
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = gym.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = TextSubtle,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "${gym.distance} · ${gym.cityArea}",
                        fontSize = 11.sp,
                        color = TextSubtle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Facility tags
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    gym.tags.take(2).forEach { tag ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color(0x1AFFFFFF))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = tag,
                                fontSize = 10.sp,
                                color = TextMuted
                            )
                        }
                    }
                }
            }
        }
    }
}

// --------------------------------------------------
// COMPONENT 6: RECENT ACTIVITY
// --------------------------------------------------
@Composable
private fun RecentActivitySection(
    visits: List<VisitLog>,
    onViewHistoryClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recent visits",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )

            Text(
                text = "View history",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = ByceGreen,
                modifier = Modifier
                    .clickable { onViewHistoryClick() }
                    .testTag("home_view_history")
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        LiquidGlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                visits.forEachIndexed { index, visit ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = visit.imageRes),
                            contentDescription = visit.gymName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = visit.gymName,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextWhite
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${visit.dateText} · ${visit.timeText}",
                                fontSize = 11.sp,
                                color = TextSubtle
                            )
                        }

                        Text(
                            text = visit.status,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextMuted
                        )
                    }
                }
            }
        }
    }
}

// --------------------------------------------------
// COMPONENT 7: TRANSLUCENT GLASS BOTTOM BAR
// --------------------------------------------------
@Composable
fun GlassBottomBar(
    selectedTab: String,
    onTabSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    isScrolling: Boolean = false
) {
    val borderAlpha by animateFloatAsState(
        targetValue = if (isScrolling) 0f else 1f,
        animationSpec = tween(durationMillis = 250),
        label = "nav_border_alpha"
    )

    val navItems = listOf(
        NavItem("Home", Icons.Default.Home),
        NavItem("Discover", Icons.Default.Search),
        NavItem("Membership", Icons.Default.CreditCard),
        NavItem("Profile", Icons.Default.Person)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 0.dp, bottomEnd = 0.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xF0222021),
                        Color(0xF8161415),
                        Color(0xFF161415)
                    )
                )
            )
            .then(
                if (borderAlpha > 0.001f) {
                    Modifier.border(
                        width = 0.5.dp,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0x25FFFFFF).copy(alpha = 0.25f * borderAlpha),
                                Color(0x08FFFFFF).copy(alpha = 0.08f * borderAlpha)
                            )
                        ),
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 0.dp, bottomEnd = 0.dp)
                    )
                } else Modifier
            )
    ) {
        // Specular highlight at top
        if (borderAlpha > 0.001f) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .align(Alignment.TopCenter)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0x25FFFFFF).copy(alpha = 0.25f * borderAlpha),
                                Color(0x50FFFFFF).copy(alpha = 0.5f * borderAlpha),
                                Color(0x25FFFFFF).copy(alpha = 0.25f * borderAlpha),
                                Color.Transparent
                            )
                        )
                    )
            )
        }
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 14.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navItems.forEach { item ->
                val isSelected = selectedTab == item.label
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clickable { onTabSelected(item.label) }
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .testTag("nav_tab_${item.label.lowercase()}")
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (isSelected) TextWhite else TextSubtle,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Active indicator dot
                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) NeonGreen else Color.Transparent)
                    )
                }
            }
        }
    }
}

private data class NavItem(val label: String, val icon: ImageVector)
