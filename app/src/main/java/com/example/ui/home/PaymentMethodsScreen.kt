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
fun PaymentMethodsScreen(
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
                    text = "Payment Methods",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Saved Cards Section
            Text(
                text = "Saved Cards",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Card 1
            PaymentCardItem(
                cardType = "Visa",
                lastFourDigits = "4242",
                expiryDate = "12/25",
                isDefault = true,
                onRemove = {}
            )

            // Card 2
            PaymentCardItem(
                cardType = "Mastercard",
                lastFourDigits = "8888",
                expiryDate = "08/26",
                isDefault = false,
                onRemove = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Add New Card Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(GlassSurfaceMedium)
                    .border(2.dp, ByceGreen, RoundedCornerShape(14.dp))
                    .clickable { }
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = ByceGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Add New Card",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = ByceGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // UPI Section
            Text(
                text = "UPI & Wallets",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )

            Spacer(modifier = Modifier.height(12.dp))

            UPIItem(
                icon = "💳",
                title = "Google Pay",
                value = "alex@oksbi",
                onClick = {}
            )

            UPIItem(
                icon = "📱",
                title = "PhonePe",
                value = "Not linked",
                onClick = {}
            )

            UPIItem(
                icon = "🏦",
                title = "Paytm",
                value = "Not linked",
                onClick = {}
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Net Banking Section
            Text(
                text = "Net Banking",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )

            Spacer(modifier = Modifier.height(12.dp))

            BankItem(
                bankName = "State Bank of India",
                accountNumber = "XXXX1234",
                onClick = {}
            )

            BankItem(
                bankName = "HDFC Bank",
                accountNumber = "Not linked",
                onClick = {}
            )
        }
    }
}

@Composable
private fun PaymentCardItem(
    cardType: String,
    lastFourDigits: String,
    expiryDate: String,
    isDefault: Boolean,
    onRemove: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(GlassSurfaceMedium)
            .border(1.dp, if (isDefault) ByceGreen else GlassBorderLight, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (cardType == "Visa") "💳" else "💳",
                        fontSize = 24.sp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "$cardType •••• $lastFourDigits",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Expires $expiryDate",
                            fontSize = 12.sp,
                            color = TextSubtle
                        )
                    }
                }

                if (isDefault) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(ByceGreen.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Default",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = ByceGreen
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (isDefault) "Default payment method" else "Set as default",
                    fontSize = 12.sp,
                    color = if (isDefault) ByceGreen else TextMuted,
                    modifier = Modifier.clickable { }
                )
                
                Text(
                    text = "Remove",
                    fontSize = 12.sp,
                    color = androidx.compose.ui.graphics.Color(0xFFFF6B6B),
                    modifier = Modifier.clickable(onClick = onRemove)
                )
            }
        }
    }
    
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
private fun UPIItem(
    icon: String,
    title: String,
    value: String,
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
                Text(text = icon, fontSize = 28.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
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
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TextSubtle,
                modifier = Modifier.size(20.dp)
            )
        }
    }
    
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun BankItem(
    bankName: String,
    accountNumber: String,
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
                    imageVector = Icons.Default.AccountBalance,
                    contentDescription = null,
                    tint = ByceGreen,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = bankName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextWhite
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = accountNumber,
                        fontSize = 12.sp,
                        color = TextSubtle
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TextSubtle,
                modifier = Modifier.size(20.dp)
            )
        }
    }
    
    Spacer(modifier = Modifier.height(8.dp))
}
