package com.example.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun PrivacySecurityScreen(
    onBackClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkNavy)
    ) {
        com.example.ui.components.FloatingBlurBalls()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(top = statusBarPadding + 12.dp, bottom = 24.dp)
                .padding(horizontal = 20.dp)
        ) {
            // Header
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
                        tint = TextWhite,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Privacy & Security",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Privacy Controls
            Text(
                text = "Privacy Controls",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )

            Spacer(modifier = Modifier.height(12.dp))

            var locationSharing by remember { mutableStateOf(true) }
            var activitySharing by remember { mutableStateOf(false) }
            var profileVisibility by remember { mutableStateOf(true) }

            PrivacyToggleItem(
                icon = Icons.Default.LocationOn,
                title = "Location Sharing",
                subtitle = "Share your location for nearby gyms",
                checked = locationSharing,
                onCheckedChange = { locationSharing = it }
            )

            PrivacyToggleItem(
                icon = Icons.Default.FitnessCenter,
                title = "Activity Sharing",
                subtitle = "Share workout stats with friends",
                checked = activitySharing,
                onCheckedChange = { activitySharing = it }
            )

            PrivacyToggleItem(
                icon = Icons.Default.Visibility,
                title = "Profile Visibility",
                subtitle = "Make your profile visible to others",
                checked = profileVisibility,
                onCheckedChange = { profileVisibility = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Data & Permissions
            Text(
                text = "Data & Permissions",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )

            Spacer(modifier = Modifier.height(12.dp))

            PrivacyOptionItem(
                icon = Icons.Default.Download,
                title = "Download Your Data",
                subtitle = "Get a copy of your Byce data",
                onClick = {}
            )

            PrivacyOptionItem(
                icon = Icons.Default.Delete,
                title = "Delete My Data",
                subtitle = "Permanently delete your data",
                onClick = {}
            )

            PrivacyOptionItem(
                icon = Icons.Default.ManageAccounts,
                title = "Manage Permissions",
                subtitle = "Camera, location, storage access",
                onClick = {}
            )

            PrivacyOptionItem(
                icon = Icons.Default.History,
                title = "Activity Log",
                subtitle = "View your account activity",
                onClick = {}
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Legal Documents
            Text(
                text = "Legal & Policies",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )

            Spacer(modifier = Modifier.height(12.dp))

            LegalDocumentItem(
                title = "Privacy Policy",
                subtitle = "Last updated: Jan 1, 2024",
                onClick = {}
            )

            LegalDocumentItem(
                title = "Terms of Service",
                subtitle = "User agreement & conditions",
                onClick = {}
            )

            LegalDocumentItem(
                title = "Cookie Policy",
                subtitle = "How we use cookies",
                onClick = {}
            )

            LegalDocumentItem(
                title = "Data Protection",
                subtitle = "GDPR compliance information",
                onClick = {}
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Security Status Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(ByceGreen.copy(alpha = 0.1f))
                    .border(1.dp, ByceGreen, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = null,
                        tint = ByceGreen,
                        modifier = Modifier.size(32.dp)
                    )
                    
                    Spacer(modifier = Modifier.width(14.dp))
                    
                    Column {
                        Text(
                            text = "Account Security: Strong",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = ByceGreen
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Your account is well protected",
                            fontSize = 12.sp,
                            color = TextWhite
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PrivacyToggleItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(GlassSurfaceMedium)
            .border(1.dp, GlassBorderLight, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = ByceGreen,
                modifier = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextWhite
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = TextSubtle
                )
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = TextWhite,
                    checkedTrackColor = ByceGreen,
                    uncheckedThumbColor = TextMuted,
                    uncheckedTrackColor = androidx.compose.ui.graphics.Color(0xFF2A2A2A)
                )
            )
        }
    }
    
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun PrivacyOptionItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(GlassSurfaceMedium)
            .border(1.dp, GlassBorderLight, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = ByceGreen,
                    modifier = Modifier.size(22.dp)
                )

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextWhite
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = TextSubtle
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = TextSubtle,
                modifier = Modifier.size(20.dp)
            )
        }
    }
    
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun LegalDocumentItem(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(GlassSurfaceMedium)
            .border(1.dp, GlassBorderLight, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextWhite
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = TextSubtle
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = TextSubtle,
                modifier = Modifier.size(20.dp)
            )
        }
    }
    
    Spacer(modifier = Modifier.height(8.dp))
}
