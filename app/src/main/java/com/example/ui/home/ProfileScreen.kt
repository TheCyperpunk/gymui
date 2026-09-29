package com.example.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DataUsage
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.MobileFriendly
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Update
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ByceGreen
import com.example.ui.theme.DarkNavy
import com.example.ui.theme.GlassBorderLight
import com.example.ui.theme.GlassSurfaceMedium
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSubtle
import com.example.ui.theme.TextWhite

/**
 * Screen for Member Profile - Enhanced with comprehensive details
 */
@Composable
fun ProfileScreen(
    onBackClick: () -> Unit = {},
    onLogOutClick: () -> Unit = {},
    onTabSelected: (String) -> Unit = {},
    onNavigateToAccountSettings: () -> Unit = {},
    onNavigateToPaymentMethods: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToHelpCenter: () -> Unit = {},
    onNavigateToMembershipHistory: () -> Unit = {},
    onNavigateToPrivacySecurity: () -> Unit = {},
    onNavigateToEditProfile: () -> Unit = {},
    onNavigateToAppearance: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val navBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkNavy)
    ) {
        // Floating ambient blur orbs
        com.example.ui.components.FloatingBlurBalls()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(top = statusBarPadding + 12.dp, bottom = navBarPadding + 90.dp)
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
                    text = "Profile",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Profile Avatar & Info with Edit Button
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(GlassSurfaceMedium)
                            .border(3.dp, ByceGreen, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Avatar",
                            tint = TextWhite,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                    
                    // Edit button overlay
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .align(Alignment.BottomEnd)
                            .clip(CircleShape)
                            .background(ByceGreen)
                            .clickable(onClick = onClick)
                            .border(2.dp, DarkNavy, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = Color.Black,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Alex Morgan",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Fitness Enthusiast",
                    fontSize = 13.sp,
                    color = ByceGreen,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Member since January 2024",
                    fontSize = 12.sp,
                    color = TextSubtle
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Personal Details Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(GlassSurfaceMedium)
                    .border(1.dp, GlassBorderLight, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Personal Information",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    DetailRow(icon = Icons.Default.Email, label = "Email", value = "alex.morgan@email.com")
                    DetailRow(icon = Icons.Default.Phone, label = "Phone", value = "+1 (555) 234-5678")
                    DetailRow(icon = Icons.Default.LocationOn, label = "Location", value = "Kochi, Kerala")
                    DetailRow(icon = Icons.Default.CalendarMonth, label = "Date of Birth", value = "March 15, 1995")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Membership Status Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(GlassSurfaceMedium)
                    .border(1.dp, GlassBorderLight, RoundedCornerShape(16.dp))
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
                                text = "Premium Member",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = ByceGreen
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Valid until Dec 31, 2024",
                                fontSize = 12.sp,
                                color = TextSubtle
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = ByceGreen,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Days Remaining",
                                fontSize = 11.sp,
                                color = TextSubtle
                            )
                            Text(
                                text = "234 days",
                                fontSize = 11.sp,
                                color = ByceGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = 0.64f,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = ByceGreen,
                            trackColor = Color(0xFF2A2A2A)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatItem(value = "47", label = "Check-ins", icon = "✓")
                        StatItem(value = "12", label = "Gyms", icon = "🏋️")
                        StatItem(value = "18", label = "Classes", icon = "📚")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Fitness Goals Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(GlassSurfaceMedium)
                    .border(1.dp, GlassBorderLight, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Fitness Goals",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = null,
                            tint = ByceGreen,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(14.dp))
                    
                    GoalProgressItem(
                        label = "Monthly Visits",
                        current = 14,
                        target = 20,
                        progress = 0.7f,
                        icon = Icons.Default.LocalFireDepartment
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    GoalProgressItem(
                        label = "Workout Hours",
                        current = 18,
                        target = 30,
                        progress = 0.6f,
                        icon = Icons.Default.Timer
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    GoalProgressItem(
                        label = "Calories Burned",
                        current = 8500,
                        target = 12000,
                        progress = 0.71f,
                        icon = Icons.Default.FitnessCenter
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Achievements Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(GlassSurfaceMedium)
                    .border(1.dp, GlassBorderLight, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Achievements",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = "7 Earned",
                            fontSize = 11.sp,
                            color = ByceGreen,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(14.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        AchievementBadge(emoji = "🔥", label = "Streak\n7 Days")
                        AchievementBadge(emoji = "💪", label = "Strong\nStart")
                        AchievementBadge(emoji = "🏆", label = "First\nMonth")
                        AchievementBadge(emoji = "⭐", label = "Early\nBird")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Account Settings Section - EXPANDED
            Text(
                text = "Account",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                ProfileOptionItem(

                    icon = Icons.Default.Edit,

                    title = "Edit Profile",

                    subtitle = "Update your name, photo & contact",

                    onClick = onNavigateToEditProfile

                )
                ProfileOptionItem(

                    icon = Icons.Default.ManageAccounts,

                    title = "Account Settings",

                    subtitle = "Email, phone & password settings",

                    onClick = onNavigateToAccountSettings

                )
                ProfileOptionItem(
                    icon = Icons.Default.Badge,
                    title = "Member Credential Pass",
                    subtitle = "BYCE-9842-7104 • Digital ID"
                )
                ProfileOptionItem(
                    icon = Icons.Default.Verified,
                    title = "Verification Status",
                    subtitle = "Email & phone verified"
                )
                ProfileOptionItem(
                    icon = Icons.Default.Password,
                    title = "Change Password",
                    subtitle = "Update your account password"
                )
                ProfileOptionItem(
                    icon = Icons.Default.VpnKey,
                    title = "Two-Factor Authentication",
                    subtitle = "Add extra security to your account"
                )
                ProfileOptionItem(
                    icon = Icons.Default.AccountCircle,
                    title = "Linked Accounts",
                    subtitle = "Connect Google, Apple, Facebook"
                )
                ProfileOptionItem(

                    icon = Icons.Default.Payment,

                    title = "Payment Methods",

                    subtitle = "Manage cards & billing info",

                    onClick = onNavigateToPaymentMethods

                )
                ProfileOptionItem(
                    icon = Icons.Default.CreditCard,
                    title = "Saved Cards",
                    subtitle = "View & manage payment cards"
                )
                ProfileOptionItem(
                    icon = Icons.Default.Wallet,
                    title = "Byce Wallet",
                    subtitle = "Balance: ₹0 • Add money"
                )
                ProfileOptionItem(
                    icon = Icons.Default.Receipt,
                    title = "Billing & Invoices",
                    subtitle = "Download payment receipts"
                )
                ProfileOptionItem(

                    icon = Icons.Default.History,

                    title = "Membership History",

                    subtitle = "View past memberships & renewals",

                    onClick = onNavigateToMembershipHistory

                )
                ProfileOptionItem(
                    icon = Icons.Default.AccountBalance,
                    title = "Subscription Management",
                    subtitle = "Upgrade, renew or cancel plan"
                )
                ProfileOptionItem(
                    icon = Icons.Default.Block,
                    title = "Deactivate Account",
                    subtitle = "Temporarily disable your account"
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Preferences Section - EXPANDED
            Text(
                text = "Preferences",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                ProfileOptionItem(
                    icon = Icons.Default.Notifications,
                    title = "Notifications",
                    subtitle = "Push alerts & visit updates"
                )
                ProfileOptionItem(

                    icon = Icons.Default.NotificationsActive,

                    title = "Notification Settings",

                    subtitle = "Customize alerts for classes & gyms",

                    onClick = onNavigateToNotifications

                )
                ProfileOptionItem(
                    icon = Icons.Default.Email,
                    title = "Email Preferences",
                    subtitle = "Newsletters & promotional emails"
                )
                ProfileOptionItem(
                    icon = Icons.Default.Language,
                    title = "Language & Region",
                    subtitle = "English (US) • Kerala, India"
                )
                ProfileOptionItem(

                    icon = Icons.Default.DarkMode,

                    title = "Appearance",

                    subtitle = "Theme: Dark • Auto-adjust brightness",

                    onClick = onNavigateToAppearance

                )
                ProfileOptionItem(
                    icon = Icons.Default.MobileFriendly,
                    title = "Display Settings",
                    subtitle = "Text size, layout & accessibility"
                )
                ProfileOptionItem(
                    icon = Icons.Default.Settings,
                    title = "App Settings",
                    subtitle = "General preferences & defaults"
                )
                ProfileOptionItem(
                    icon = Icons.Default.DataUsage,
                    title = "Data & Storage",
                    subtitle = "Cache management & offline mode"
                )
                ProfileOptionItem(
                    icon = Icons.Default.Sync,
                    title = "Sync & Backup",
                    subtitle = "Auto-sync workout data"
                )
                ProfileOptionItem(
                    icon = Icons.Default.LocationOn,
                    title = "Location Services",
                    subtitle = "GPS for nearby gym recommendations"
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Support & Info Section - EXPANDED
            Text(
                text = "Support & Info",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                ProfileOptionItem(

                    icon = Icons.Default.Help,

                    title = "Help Center",

                    subtitle = "FAQs, tutorials & troubleshooting",

                    onClick = onNavigateToHelpCenter

                )
                ProfileOptionItem(
                    icon = Icons.Default.Phone,
                    title = "Contact Support",
                    subtitle = "Chat, call or email us"
                )
                ProfileOptionItem(
                    icon = Icons.Default.Description,
                    title = "User Guide",
                    subtitle = "Learn how to use Byce features"
                )
                ProfileOptionItem(
                    icon = Icons.Default.Feedback,
                    title = "Send Feedback",
                    subtitle = "Report bugs or suggest features"
                )
                ProfileOptionItem(
                    icon = Icons.Default.RateReview,
                    title = "Rate Byce App",
                    subtitle = "Share your experience on Play Store"
                )
                ProfileOptionItem(
                    icon = Icons.Default.Share,
                    title = "Invite Friends",
                    subtitle = "Refer friends & earn rewards"
                )
                ProfileOptionItem(
                    icon = Icons.Default.EmojiEvents,
                    title = "Referral Program",
                    subtitle = "Get ₹500 for each referral"
                )
                ProfileOptionItem(
                    icon = Icons.Default.Lock,
                    title = "Privacy Policy",
                    subtitle = "How we handle your data"
                )
                ProfileOptionItem(
                    icon = Icons.Default.Gavel,
                    title = "Terms of Service",
                    subtitle = "User agreement & conditions"
                )
                ProfileOptionItem(
                    icon = Icons.Default.Policy,
                    title = "Cookie Policy",
                    subtitle = "Information about cookies we use"
                )
                ProfileOptionItem(
                    icon = Icons.Default.Security,
                    title = "Security Center",
                    subtitle = "Account security & data protection"
                )
                ProfileOptionItem(
                    icon = Icons.Default.Shield,
                    title = "Safety Guidelines",
                    subtitle = "Gym safety & COVID protocols"
                )
                ProfileOptionItem(

                    icon = Icons.Default.Fingerprint,

                    title = "Data & Privacy",

                    subtitle = "Manage permissions & data sharing",

                    onClick = onNavigateToPrivacySecurity

                )
                ProfileOptionItem(
                    icon = Icons.Default.Info,
                    title = "About Byce",
                    subtitle = "Version 1.0.0 • Learn more"
                )
                ProfileOptionItem(
                    icon = Icons.Default.Update,
                    title = "Check for Updates",
                    subtitle = "Latest version: 1.0.0"
                )
                ProfileOptionItem(
                    icon = Icons.Default.Description,
                    title = "Open Source Licenses",
                    subtitle = "Third-party software notices"
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Logout button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { onLogOutClick() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Logout",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF6B6B)
                )
            }
        }

        // Bottom Nav Bar
        GlassBottomBar(
            selectedTab = "Profile",
            isScrolling = scrollState.isScrollInProgress,
            onTabSelected = onTabSelected,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun DetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = ByceGreen,
            modifier = Modifier.size(18.dp)
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 11.sp,
                color = TextSubtle
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontSize = 13.sp,
                color = TextWhite,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun GoalProgressItem(
    label: String,
    current: Int,
    target: Int,
    progress: Float,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = ByceGreen,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = label,
                    fontSize = 12.sp,
                    color = TextWhite,
                    fontWeight = FontWeight.Medium
                )
            }
            Text(
                text = "$current / $target",
                fontSize = 11.sp,
                color = TextSubtle
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = ByceGreen,
            trackColor = Color(0xFF2A2A2A)
        )
    }
}

@Composable
private fun AchievementBadge(
    emoji: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(Color(0xFF1E1E1E))
                .border(2.dp, ByceGreen, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = emoji,
                fontSize = 24.sp
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            fontSize = 9.sp,
            color = TextSubtle,
            fontWeight = FontWeight.Medium,
            lineHeight = 11.sp
        )
    }
}

@Composable
private fun ProfileOptionItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(GlassSurfaceMedium),
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
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 11.sp,
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

@Composable
private fun StatItem(
    value: String,
    label: String,
    icon: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = icon,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = ByceGreen
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            color = TextSubtle
        )
    }
}



