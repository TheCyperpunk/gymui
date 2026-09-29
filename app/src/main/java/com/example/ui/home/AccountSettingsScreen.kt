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
fun AccountSettingsScreen(
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
                    text = "Account Settings",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Personal Information Section
            Text(
                text = "Personal Information",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )

            Spacer(modifier = Modifier.height(12.dp))

            SettingsDetailItem(
                icon = Icons.Default.Person,
                title = "Full Name",
                value = "Alex Morgan",
                onClick = {}
            )
            SettingsDetailItem(
                icon = Icons.Default.Email,
                title = "Email Address",
                value = "alex.morgan@email.com",
                onClick = {}
            )
            SettingsDetailItem(
                icon = Icons.Default.Phone,
                title = "Phone Number",
                value = "+1 (555) 234-5678",
                onClick = {}
            )
            SettingsDetailItem(
                icon = Icons.Default.Cake,
                title = "Date of Birth",
                value = "March 15, 1995",
                onClick = {}
            )
            SettingsDetailItem(
                icon = Icons.Default.Wc,
                title = "Gender",
                value = "Male",
                onClick = {}
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Security Section
            Text(
                text = "Security",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )

            Spacer(modifier = Modifier.height(12.dp))

            SettingsDetailItem(
                icon = Icons.Default.Lock,
                title = "Change Password",
                value = "Last changed 30 days ago",
                onClick = {}
            )
            SettingsDetailItem(
                icon = Icons.Default.Fingerprint,
                title = "Biometric Login",
                value = "Enabled",
                onClick = {}
            )
            SettingsDetailItem(
                icon = Icons.Default.Security,
                title = "Two-Factor Authentication",
                value = "Not enabled",
                onClick = {}
            )
            SettingsDetailItem(
                icon = Icons.Default.DevicesFold,
                title = "Trusted Devices",
                value = "3 devices",
                onClick = {}
            )
            SettingsDetailItem(
                icon = Icons.Default.Login,
                title = "Login History",
                value = "View recent logins",
                onClick = {}
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Connected Accounts Section
            Text(
                text = "Connected Accounts",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )

            Spacer(modifier = Modifier.height(12.dp))

            SettingsDetailItem(
                icon = Icons.Default.Email,
                title = "Google",
                value = "Connected",
                onClick = {}
            )
            SettingsDetailItem(
                icon = Icons.Default.AccountCircle,
                title = "Apple",
                value = "Not connected",
                onClick = {}
            )
            SettingsDetailItem(
                icon = Icons.Default.AccountCircle,
                title = "Facebook",
                value = "Not connected",
                onClick = {}
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Delete Account Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(GlassSurfaceMedium)
                    .border(1.dp, androidx.compose.ui.graphics.Color(0xFFFF6B6B), RoundedCornerShape(14.dp))
                    .clickable { }
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Delete Account",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = androidx.compose.ui.graphics.Color(0xFFFF6B6B)
                )
            }
        }
    }
}

@Composable
private fun SettingsDetailItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(GlassSurfaceMedium)
            .border(1.dp, GlassBorderLight, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(androidx.compose.ui.graphics.Color(0xFF1E1E1E)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = ByceGreen,
                modifier = Modifier.size(18.dp)
            )
        }

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
                text = value,
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
    
    Spacer(modifier = Modifier.height(8.dp))
}

