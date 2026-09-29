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
fun HelpCenterScreen(
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
                    text = "Help Center",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Contact Support Section
            Text(
                text = "Contact Support",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )

            Spacer(modifier = Modifier.height(12.dp))

            ContactOptionCard(
                icon = Icons.Default.Chat,
                title = "Live Chat",
                subtitle = "Average response time: 5 minutes",
                isAvailable = true,
                onClick = {}
            )

            ContactOptionCard(
                icon = Icons.Default.Email,
                title = "Email Support",
                subtitle = "support@byce.com • Response in 24 hours",
                isAvailable = true,
                onClick = {}
            )

            ContactOptionCard(
                icon = Icons.Default.Phone,
                title = "Call Us",
                subtitle = "+91 1800 123 4567 • Available 24/7",
                isAvailable = true,
                onClick = {}
            )

            ContactOptionCard(
                icon = Icons.Default.Message,
                title = "WhatsApp",
                subtitle = "+91 98765 43210",
                isAvailable = true,
                onClick = {}
            )

            Spacer(modifier = Modifier.height(24.dp))

            // FAQ Section
            Text(
                text = "Frequently Asked Questions",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )

            Spacer(modifier = Modifier.height(12.dp))

            FAQItem(
                question = "How do I check in at a gym?",
                answer = "Use the QR code scanner on the home screen or search for the gym in the Discover tab."
            )

            FAQItem(
                question = "Can I cancel my membership?",
                answer = "Yes, you can cancel anytime from Account Settings > Subscription Management."
            )

            FAQItem(
                question = "How do I add a payment method?",
                answer = "Go to Profile > Payment Methods > Add New Card and enter your card details."
            )

            FAQItem(
                question = "What if I forgot my password?",
                answer = "Click 'Forgot Password' on the login screen and follow the reset instructions."
            )

            FAQItem(
                question = "How do I invite friends?",
                answer = "Go to Profile > Invite Friends and share your referral code to earn rewards."
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Quick Actions
            Text(
                text = "Quick Actions",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )

            Spacer(modifier = Modifier.height(12.dp))

            QuickActionButton(
                icon = Icons.Default.Description,
                title = "View User Guide",
                onClick = {}
            )

            QuickActionButton(
                icon = Icons.Default.Feedback,
                title = "Submit Feedback",
                onClick = {}
            )

            QuickActionButton(
                icon = Icons.Default.BugReport,
                title = "Report a Bug",
                onClick = {}
            )
        }
    }
}

@Composable
private fun ContactOptionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    isAvailable: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(GlassSurfaceMedium)
            .border(1.dp, GlassBorderLight, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(ByceGreen.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = ByceGreen,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = TextSubtle
                )
            }

            if (isAvailable) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(ByceGreen)
                )
            }
        }
    }
    
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
private fun FAQItem(
    question: String,
    answer: String
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(GlassSurfaceMedium)
            .border(1.dp, GlassBorderLight, RoundedCornerShape(12.dp))
            .clickable { expanded = !expanded }
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = question,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextWhite,
                    modifier = Modifier.weight(1f)
                )
                
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = ByceGreen,
                    modifier = Modifier.size(20.dp)
                )
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = answer,
                    fontSize = 13.sp,
                    color = TextSubtle,
                    lineHeight = 18.sp
                )
            }
        }
    }
    
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun QuickActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(GlassSurfaceMedium)
            .border(1.dp, ByceGreen, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = ByceGreen,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextWhite
            )
        }
    }
    
    Spacer(modifier = Modifier.height(8.dp))
}

