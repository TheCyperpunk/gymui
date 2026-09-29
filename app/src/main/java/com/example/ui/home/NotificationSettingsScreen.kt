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
fun NotificationSettingsScreen(
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
                    text = "Notification Settings",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Push Notifications Section
            Text(
                text = "Push Notifications",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )

            Spacer(modifier = Modifier.height(12.dp))

            var checkIns by remember { mutableStateOf(true) }
            var classReminders by remember { mutableStateOf(true) }
            var specialOffers by remember { mutableStateOf(false) }
            var workoutTips by remember { mutableStateOf(true) }

            NotificationToggleItem(
                title = "Check-in Confirmations",
                subtitle = "Get notified when you check in",
                checked = checkIns,
                onCheckedChange = { checkIns = it }
            )

            NotificationToggleItem(
                title = "Class Reminders",
                subtitle = "Reminders 30 min before class",
                checked = classReminders,
                onCheckedChange = { classReminders = it }
            )

            NotificationToggleItem(
                title = "Special Offers",
                subtitle = "Deals and promotions",
                checked = specialOffers,
                onCheckedChange = { specialOffers = it }
            )

            NotificationToggleItem(
                title = "Workout Tips",
                subtitle = "Daily fitness tips & motivation",
                checked = workoutTips,
                onCheckedChange = { workoutTips = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Email Notifications Section
            Text(
                text = "Email Notifications",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )

            Spacer(modifier = Modifier.height(12.dp))

            var emailWeekly by remember { mutableStateOf(true) }
            var emailNewsletter by remember { mutableStateOf(false) }
            var emailBilling by remember { mutableStateOf(true) }

            NotificationToggleItem(
                title = "Weekly Summary",
                subtitle = "Your weekly activity report",
                checked = emailWeekly,
                onCheckedChange = { emailWeekly = it }
            )

            NotificationToggleItem(
                title = "Newsletter",
                subtitle = "Monthly fitness newsletter",
                checked = emailNewsletter,
                onCheckedChange = { emailNewsletter = it }
            )

            NotificationToggleItem(
                title = "Billing Notifications",
                subtitle = "Payment & membership updates",
                checked = emailBilling,
                onCheckedChange = { emailBilling = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // SMS Notifications Section
            Text(
                text = "SMS Notifications",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )

            Spacer(modifier = Modifier.height(12.dp))

            var smsImportant by remember { mutableStateOf(true) }
            var smsMarketing by remember { mutableStateOf(false) }

            NotificationToggleItem(
                title = "Important Updates",
                subtitle = "Security & account alerts",
                checked = smsImportant,
                onCheckedChange = { smsImportant = it }
            )

            NotificationToggleItem(
                title = "Marketing Messages",
                subtitle = "Promotional SMS",
                checked = smsMarketing,
                onCheckedChange = { smsMarketing = it }
            )
        }
    }
}

@Composable
private fun NotificationToggleItem(
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

            Spacer(modifier = Modifier.width(12.dp))

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
