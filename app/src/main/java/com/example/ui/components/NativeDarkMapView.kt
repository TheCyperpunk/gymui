package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.home.GymLocation
import kotlinx.coroutines.launch

/**
 * 100% Native Jetpack Compose Hardware-Accelerated Dark Map.
 *
 * Modeled after Apna's Job Map UI & Cult.fit dark theme:
 * - High-contrast dark obsidian street grid & arterial highways
 * - Natural waterways, parks & city blocks
 * - District typography ("DOWNTOWN", "SOMA", "MISSION", etc.)
 * - Pulsing GPS radar wave around user location
 * - Interactive pins with glowing Cult.fit badges & live status dots
 * - 60-120 FPS hardware-accelerated smooth pan & pinch-to-zoom
 * - Zero external CDN / network dependencies, NEVER black screen!
 */
@Composable
fun NativeDarkMapView(
    gyms: List<GymLocation>,
    selectedGym: GymLocation?,
    recenterTrigger: Int,
    onGymClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current

    // Virtual origin for user location
    val userVirtualX = 800f
    val userVirtualY = 800f

    // Animated pan offsets & zoom
    val animPanX = remember { Animatable(0f) }
    val animPanY = remember { Animatable(0f) }
    val animZoom = remember { Animatable(1.15f) }

    // Map gym IDs to virtual coordinates around user location (800, 800)
    val gymCoordinates = remember {
        mapOf(
            "1" to Offset(660f, 620f),   // Pulse Fitness (North-West)
            "2" to Offset(990f, 650f),   // Iron Vault Gym (North-East)
            "3" to Offset(440f, 490f),   // Zenith Health Club (Marina North)
            "4" to Offset(610f, 960f),   // Apex Strength & Conditioning (Mission South-West)
            "5" to Offset(1040f, 830f),  // Velocity Athletic Club (SoMa East)
            "6" to Offset(880f, 1140f)   // Titan Powerhouse (Potrero South)
        )
    }

    // Recenter animation when FAB is tapped
    LaunchedEffect(recenterTrigger) {
        if (recenterTrigger > 0) {
            launch { animPanX.animateTo(0f, tween(450, easing = FastOutSlowInEasing)) }
            launch { animPanY.animateTo(0f, tween(450, easing = FastOutSlowInEasing)) }
            launch { animZoom.animateTo(1.15f, tween(450, easing = FastOutSlowInEasing)) }
        }
    }

    // Smoothly fly camera to selected gym
    LaunchedEffect(selectedGym?.id) {
        selectedGym?.id?.let { gymId ->
            val coord = gymCoordinates[gymId]
            if (coord != null) {
                val targetPanX = (userVirtualX - coord.x) * animZoom.value
                val targetPanY = (userVirtualY - coord.y) * animZoom.value + 30f
                launch { animPanX.animateTo(targetPanX, tween(480, easing = FastOutSlowInEasing)) }
                launch { animPanY.animateTo(targetPanY, tween(480, easing = FastOutSlowInEasing)) }
            }
        }
    }

    // Pulsing radar wave animation for user location
    val infiniteTransition = rememberInfiniteTransition(label = "map_radar_pulse")
    val radarPulseRadius by infiniteTransition.animateFloat(
        initialValue = 10f,
        targetValue = 54f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radarRadius"
    )
    val radarPulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.65f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radarAlpha"
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F0E11))
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    coroutineScope.launch {
                        val newZoom = (animZoom.value * zoom).coerceIn(0.7f, 2.4f)
                        animZoom.snapTo(newZoom)
                        animPanX.snapTo(animPanX.value + pan.x)
                        animPanY.snapTo(animPanY.value + pan.y)
                    }
                }
            }
    ) {
        val screenWidth = constraints.maxWidth.toFloat()
        val screenHeight = constraints.maxHeight.toFloat()
        val viewCenterX = screenWidth / 2f
        val viewCenterY = screenHeight * 0.38f // Center map viewport above bottom sheet

        // 1. HARDWARE-ACCELERATED VECTOR MAP CANVAS
        Canvas(modifier = Modifier.fillMaxSize()) {
            withTransform({
                translate(viewCenterX + animPanX.value, viewCenterY + animPanY.value)
                scale(animZoom.value, animZoom.value, Offset.Zero)
                translate(-userVirtualX, -userVirtualY)
            }) {
                // Background dark obsidian surface
                drawRect(color = Color(0xFF111014), size = Size(1600f, 1600f))

                // ----------------------------------------------------
                // 1. NATURAL WATERWAY / BAY AREA
                // ----------------------------------------------------
                val waterPath = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(1600f, 0f)
                    lineTo(1600f, 320f)
                    cubicTo(1400f, 340f, 1250f, 260f, 1100f, 310f)
                    cubicTo(950f, 360f, 850f, 290f, 700f, 330f)
                    cubicTo(550f, 370f, 400f, 280f, 250f, 350f)
                    cubicTo(120f, 410f, 50f, 380f, 0f, 420f)
                    close()
                }
                drawPath(waterPath, color = Color(0xFF0A131C))
                drawPath(waterPath, color = Color(0xFF142434), style = Stroke(width = 3f))

                // Secondary River Channel
                val riverPath = Path().apply {
                    moveTo(1600f, 820f)
                    cubicTo(1350f, 800f, 1200f, 920f, 1050f, 900f)
                    cubicTo(900f, 880f, 820f, 960f, 700f, 980f)
                    cubicTo(580f, 1000f, 450f, 940f, 300f, 970f)
                    cubicTo(150f, 1000f, 80f, 1080f, 0f, 1100f)
                    lineTo(0f, 1135f)
                    cubicTo(80f, 1115f, 150f, 1035f, 300f, 1005f)
                    cubicTo(450f, 975f, 580f, 1035f, 700f, 1015f)
                    cubicTo(820f, 995f, 900f, 915f, 1050f, 935f)
                    cubicTo(1200f, 955f, 1350f, 835f, 1600f, 855f)
                    close()
                }
                drawPath(riverPath, color = Color(0xFF091118))
                drawPath(riverPath, color = Color(0xFF12202C), style = Stroke(width = 2f))

                // ----------------------------------------------------
                // 2. PARKS & NATURE RESERVES
                // ----------------------------------------------------
                val marinaPark = Path().apply {
                    moveTo(180f, 440f)
                    cubicTo(260f, 430f, 380f, 450f, 440f, 490f)
                    cubicTo(420f, 580f, 260f, 590f, 160f, 560f)
                    close()
                }
                drawPath(marinaPark, color = Color(0xFF0F1E14))
                drawPath(marinaPark, color = Color(0xFF183220), style = Stroke(width = 1.5f))

                val missionPark = Path().apply {
                    moveTo(720f, 740f)
                    cubicTo(800f, 730f, 880f, 760f, 890f, 820f)
                    cubicTo(860f, 870f, 760f, 880f, 710f, 840f)
                    close()
                }
                drawPath(missionPark, color = Color(0xFF0F1E14))
                drawPath(missionPark, color = Color(0xFF183220), style = Stroke(width = 1.5f))

                val southPark = Path().apply {
                    moveTo(1020f, 1120f)
                    cubicTo(1150f, 1100f, 1260f, 1150f, 1240f, 1260f)
                    cubicTo(1160f, 1300f, 1000f, 1260f, 980f, 1180f)
                    close()
                }
                drawPath(southPark, color = Color(0xFF0F1E14))
                drawPath(southPark, color = Color(0xFF183220), style = Stroke(width = 1.5f))

                // ----------------------------------------------------
                // 3. CITY BLOCKS
                // ----------------------------------------------------
                val blockColor = Color(0xFF161519)
                val blockBorder = Color(0xFF222026)

                val cityBlocks = listOf(
                    Triple(220f, 620f, Size(120f, 80f)),
                    Triple(360f, 620f, Size(110f, 80f)),
                    Triple(220f, 720f, Size(120f, 90f)),
                    Triple(360f, 720f, Size(110f, 90f)),
                    Triple(490f, 620f, Size(130f, 80f)),
                    Triple(490f, 720f, Size(130f, 90f)),
                    Triple(640f, 480f, Size(130f, 100f)),
                    Triple(790f, 480f, Size(140f, 100f)),
                    Triple(950f, 480f, Size(130f, 100f)),
                    Triple(1100f, 480f, Size(120f, 100f)),
                    Triple(640f, 600f, Size(130f, 110f)),
                    Triple(790f, 600f, Size(140f, 110f)),
                    Triple(950f, 600f, Size(130f, 110f)),
                    Triple(1100f, 600f, Size(120f, 110f)),
                    Triple(920f, 740f, Size(160f, 110f)),
                    Triple(1100f, 740f, Size(130f, 110f)),
                    Triple(1250f, 740f, Size(140f, 110f)),
                    Triple(520f, 840f, Size(150f, 90f)),
                    Triple(520f, 950f, Size(150f, 110f)),
                    Triple(700f, 900f, Size(130f, 100f)),
                    Triple(850f, 900f, Size(120f, 100f)),
                    Triple(680f, 1060f, Size(140f, 100f)),
                    Triple(840f, 1060f, Size(140f, 100f)),
                    Triple(1000f, 1060f, Size(130f, 100f)),
                    Triple(680f, 1180f, Size(140f, 110f)),
                    Triple(840f, 1180f, Size(140f, 110f))
                )

                for ((bx, by, bsize) in cityBlocks) {
                    drawRoundRect(
                        color = blockColor,
                        topLeft = Offset(bx, by),
                        size = bsize,
                        cornerRadius = CornerRadius(6f, 6f)
                    )
                    drawRoundRect(
                        color = blockBorder,
                        topLeft = Offset(bx, by),
                        size = bsize,
                        cornerRadius = CornerRadius(6f, 6f),
                        style = Stroke(width = 1f)
                    )
                }

                // ----------------------------------------------------
                // 4. STREET GRID & AVENUES (High-Contrast Dark Grid)
                // ----------------------------------------------------
                val streetColor = Color(0xFF222026)
                val avenueColor = Color(0xFF2E2C33)

                // Horizontal streets
                listOf(460f, 590f, 720f, 830f, 870f, 1020f, 1040f, 1170f, 1310f).forEach { yPos ->
                    drawLine(
                        color = streetColor,
                        start = Offset(80f, yPos),
                        end = Offset(1520f, yPos),
                        strokeWidth = 3.5f
                    )
                }

                // Vertical streets
                listOf(200f, 350f, 480f, 630f, 780f, 935f, 1090f, 1240f, 1400f).forEach { xPos ->
                    drawLine(
                        color = streetColor,
                        start = Offset(xPos, 360f),
                        end = Offset(xPos, 1440f),
                        strokeWidth = 3.5f
                    )
                }

                // Major Avenues
                drawLine(
                    color = avenueColor,
                    start = Offset(80f, 725f),
                    end = Offset(1520f, 725f),
                    strokeWidth = 7f,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = avenueColor,
                    start = Offset(780f, 340f),
                    end = Offset(780f, 1440f),
                    strokeWidth = 7f,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = avenueColor,
                    start = Offset(1090f, 340f),
                    end = Offset(1090f, 1440f),
                    strokeWidth = 7f,
                    cap = StrokeCap.Round
                )

                // Diagonal Broadway / Market St Boulevard
                drawLine(
                    color = Color(0xFF383540),
                    start = Offset(160f, 1120f),
                    end = Offset(1380f, 460f),
                    strokeWidth = 9f,
                    cap = StrokeCap.Round
                )

                // ----------------------------------------------------
                // 5. MAJOR EXPRESSWAY / HIGHWAY (HWY 101)
                // ----------------------------------------------------
                val hwyPath = Path().apply {
                    moveTo(1450f, 340f)
                    cubicTo(1380f, 550f, 1280f, 850f, 1200f, 1050f)
                    cubicTo(1150f, 1200f, 1050f, 1350f, 980f, 1500f)
                }
                drawPath(
                    hwyPath,
                    color = Color(0xFF423E4D),
                    style = Stroke(width = 14f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                )
                drawPath(
                    hwyPath,
                    color = Color(0xFF1E1D24),
                    style = Stroke(width = 2.5f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                )

                // ----------------------------------------------------
                // 6. DISTRICT & STREET LABELS (Apna Style Typography)
                // ----------------------------------------------------
                drawContext.canvas.nativeCanvas.apply {
                    val cityPaint = android.graphics.Paint().apply {
                        color = android.graphics.Color.argb(80, 255, 255, 255)
                        textSize = 38f
                        isAntiAlias = true
                        letterSpacing = 0.08f
                        typeface = android.graphics.Typeface.create(
                            android.graphics.Typeface.DEFAULT,
                            android.graphics.Typeface.BOLD
                        )
                    }

                    val districtPaint = android.graphics.Paint().apply {
                        color = android.graphics.Color.argb(160, 200, 198, 206)
                        textSize = 22f
                        isAntiAlias = true
                        letterSpacing = 0.22f
                        typeface = android.graphics.Typeface.create(
                            android.graphics.Typeface.DEFAULT,
                            android.graphics.Typeface.BOLD
                        )
                    }

                    val streetPaint = android.graphics.Paint().apply {
                        color = android.graphics.Color.argb(100, 150, 148, 156)
                        textSize = 15f
                        isAntiAlias = true
                        letterSpacing = 0.12f
                        typeface = android.graphics.Typeface.create(
                            android.graphics.Typeface.DEFAULT,
                            android.graphics.Typeface.NORMAL
                        )
                    }

                    drawText("San Francisco", 320f, 830f, cityPaint)

                    drawText("MARINA", 230f, 520f, districtPaint)
                    drawText("DOWNTOWN", 710f, 545f, districtPaint)
                    drawText("FINANCIAL DIST", 970f, 545f, districtPaint)
                    drawText("SOMA", 840f, 790f, districtPaint)
                    drawText("MISSION BAY", 620f, 980f, districtPaint)
                    drawText("POTRERO HILL", 910f, 1220f, districtPaint)

                    drawText("MARKET ST", 680f, 800f, streetPaint)
                    drawText("BRANNAN ST", 950f, 715f, streetPaint)
                    drawText("HWY 101", 1250f, 920f, streetPaint)
                    drawText("4TH AVE", 790f, 430f, streetPaint)
                    drawText("MISSION ST", 520f, 910f, streetPaint)
                }

                // ----------------------------------------------------
                // 7. ACTIVE NAVIGATION / WALKING ROUTE LINE
                // ----------------------------------------------------
                selectedGym?.id?.let { gymId ->
                    val gymCoord = gymCoordinates[gymId]
                    if (gymCoord != null) {
                        val routePath = Path().apply {
                            moveTo(userVirtualX, userVirtualY)
                            val midX = userVirtualX
                            val midY = gymCoord.y
                            lineTo(midX, midY)
                            lineTo(gymCoord.x, gymCoord.y)
                        }

                        // Glow behind route
                        drawPath(
                            routePath,
                            color = Color(0x38A6CE39),
                            style = Stroke(width = 9f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                        )

                        // Dashed neon route
                        drawPath(
                            routePath,
                            color = Color(0xFFA6CE39),
                            style = Stroke(
                                width = 3.5f,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 10f), 0f),
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )
                    }
                }

                // ----------------------------------------------------
                // 8. USER LOCATION PULSING RADAR PIN
                // ----------------------------------------------------
                // Expanding Radar Wave (Neon Lime / White Pulse)
                drawCircle(
                    color = Color(0xFFA6CE39).copy(alpha = radarPulseAlpha),
                    radius = radarPulseRadius,
                    center = Offset(userVirtualX, userVirtualY)
                )

                // Outer aura ring
                drawCircle(
                    color = Color(0x40A6CE39),
                    radius = 20f,
                    center = Offset(userVirtualX, userVirtualY)
                )

                // Dark border ring
                drawCircle(
                    color = Color(0xFF141316),
                    radius = 10f,
                    center = Offset(userVirtualX, userVirtualY)
                )

                // Pure White center GPS core
                drawCircle(
                    color = Color(0xFFFFFFFF),
                    radius = 7f,
                    center = Offset(userVirtualX, userVirtualY)
                )
            }
        }

        // ----------------------------------------------------
        // 9. INTERACTIVE FLOATING GYM PINS & BADGES
        // ----------------------------------------------------
        gyms.forEach { gym ->
            val coord = gymCoordinates[gym.id] ?: Offset(800f, 800f)
            val isSelected = (gym.id == selectedGym?.id)

            // Convert virtual coordinates to screen space
            val pinScreenX = viewCenterX + (coord.x - userVirtualX) * animZoom.value + animPanX.value
            val pinScreenY = viewCenterY + (coord.y - userVirtualY) * animZoom.value + animPanY.value

            val pinXDp = with(density) { pinScreenX.toDp() }
            val pinYDp = with(density) { pinScreenY.toDp() }

            Box(
                modifier = Modifier
                    .offset(x = pinXDp - 70.dp, y = pinYDp - 62.dp)
                    .width(140.dp)
                    .clickable { onGymClicked(gym.id) },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onGymClicked(gym.id) }
                ) {
                    // Floating Badge Pill (Apna style)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(
                                if (isSelected) Color(0xFF262529) else Color(0xF01C1B1F)
                            )
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) Color(0xFFA6CE39) else Color(0x38FFFFFF),
                                shape = RoundedCornerShape(50.dp)
                            )
                            .padding(horizontal = 9.dp, vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            // Live Green status dot
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFA6CE39))
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = gym.name.take(15),
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                color = if (isSelected) Color(0xFFFFFFFF) else Color(0xFFE2E2E6),
                                maxLines = 1
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    // Marker Pin Circle
                    Box(
                        modifier = Modifier
                            .size(if (isSelected) 38.dp else 32.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) Color(0xFF2E2C33) else Color(0xFF232227)
                            )
                            .border(
                                width = if (isSelected) 2.dp else 1.5.dp,
                                color = if (isSelected) Color(0xFFA6CE39) else Color(0xB3FFFFFF),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🏋️",
                            fontSize = if (isSelected) 16.sp else 13.sp
                        )
                    }
                }
            }
        }
    }
}
