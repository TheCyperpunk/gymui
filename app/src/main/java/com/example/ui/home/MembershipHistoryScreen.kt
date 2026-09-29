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
fun MembershipHistoryScreen(
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
                    text = "Membership History",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Current Membership
            Text(
                text = "Current Membership",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )

            Spacer(modifier = Modifier.height(12.dp))

            MembershipCard(
                planName = "Premium Annual",
                startDate = "Jan 1, 2024",
                endDate = "Dec 31, 2024",
                amount = "₹12,999",
                status = "Active",
                isActive = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Past Memberships
            Text(
                text = "Past Memberships",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )

            Spacer(modifier = Modifier.height(12.dp))

            MembershipCard(
                planName = "Premium Monthly",
                startDate = "Oct 1, 2023",
                endDate = "Dec 31, 2023",
                amount = "₹1,299/mo",
                status = "Expired",
                isActive = false
            )

            MembershipCard(
                planName = "Basic Plan",
                startDate = "Jul 1, 2023",
                endDate = "Sep 30, 2023",
                amount = "₹799/mo",
                status = "Expired",
                isActive = false
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Payment History
            Text(
                text = "Payment History",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )

            Spacer(modifier = Modifier.height(12.dp))

            PaymentHistoryItem(
                date = "Jan 1, 2024",
                description = "Annual Premium Membership",
                amount = "₹12,999",
                status = "Paid"
            )

            PaymentHistoryItem(
                date = "Oct 1, 2023",
                description = "Monthly Premium Membership",
                amount = "₹1,299",
                status = "Paid"
            )

            PaymentHistoryItem(
                date = "Sep 1, 2023",
                description = "Monthly Premium Membership",
                amount = "₹1,299",
                status = "Paid"
            )

            PaymentHistoryItem(
                date = "Aug 1, 2023",
                description = "Monthly Premium Membership",
                amount = "₹1,299",
                status = "Paid"
            )
        }
    }
}

@Composable
private fun MembershipCard(
    planName: String,
    startDate: String,
    endDate: String,
    amount: String,
    status: String,
    isActive: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(GlassSurfaceMedium)
            .border(
                2.dp,
                if (isActive) ByceGreen else GlassBorderLight,
                RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = planName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isActive) ByceGreen else TextWhite
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$startDate - $endDate",
                        fontSize = 12.sp,
                        color = TextSubtle
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (isActive) ByceGreen.copy(alpha = 0.2f)
                            else androidx.compose.ui.graphics.Color(0xFF2A2A2A)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = status,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isActive) ByceGreen else TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Amount Paid",
                    fontSize = 12.sp,
                    color = TextSubtle
                )
                Text(
                    text = amount,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
            }

            if (isActive) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(ByceGreen)
                        .clickable { }
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Download Invoice",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = androidx.compose.ui.graphics.Color.Black
                    )
                }
            }
        }
    }
    
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
private fun PaymentHistoryItem(
    date: String,
    description: String,
    amount: String,
    status: String
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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = ByceGreen,
                    modifier = Modifier.size(20.dp)
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column {
                    Text(
                        text = description,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextWhite
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = date,
                        fontSize = 11.sp,
                        color = TextSubtle
                    )
                }
            }

            Text(
                text = amount,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = ByceGreen
            )
        }
    }
    
    Spacer(modifier = Modifier.height(8.dp))
}
