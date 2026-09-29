package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.ui.auth.AuthScreen
import com.example.ui.home.*
import com.example.ui.onboarding.OnboardingScreen
import com.example.ui.theme.MyApplicationTheme

enum class AppScreen {
    ONBOARDING,
    AUTH,
    HOME,
    CHECK_IN,
    GYM_DETAIL,
    DISCOVER,
    GYM_MAP,
    MEMBERSHIP,
    PROFILE,
    VISIT_HISTORY,
    ACCOUNT_SETTINGS,
    PAYMENT_METHODS,
    NOTIFICATION_SETTINGS,
    HELP_CENTER,
    MEMBERSHIP_HISTORY,
    PRIVACY_SECURITY,
    EDIT_PROFILE,
    APPEARANCE
}

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        var currentScreen by remember { mutableStateOf(AppScreen.ONBOARDING) }
        var selectedGymForDetail by remember { mutableStateOf<GymLocation?>(null) }

        Crossfade(targetState = currentScreen, label = "screen_transition") { screen ->
          when (screen) {
            AppScreen.ONBOARDING -> {
              OnboardingScreen(
                onGetStartedClick = {
                  currentScreen = AppScreen.AUTH
                },
                modifier = Modifier.fillMaxSize()
              )
            }
            AppScreen.AUTH -> {
              AuthScreen(
                onAuthComplete = {
                  currentScreen = AppScreen.HOME
                },
                onBackToOnboarding = {
                  currentScreen = AppScreen.ONBOARDING
                },
                modifier = Modifier.fillMaxSize()
              )
            }
            AppScreen.HOME -> {
              HomeScreen(
                memberName = "Alex",
                onNavigateToCheckIn = {
                  currentScreen = AppScreen.CHECK_IN
                },
                onNavigateToGymDetail = { gym ->
                  selectedGymForDetail = gym
                  currentScreen = AppScreen.GYM_DETAIL
                },
                onNavigateToDiscover = {
                  currentScreen = AppScreen.DISCOVER
                },
                onNavigateToGymMap = {
                  currentScreen = AppScreen.GYM_MAP
                },
                onNavigateToMembership = {
                  currentScreen = AppScreen.MEMBERSHIP
                },
                onNavigateToProfile = {
                  currentScreen = AppScreen.PROFILE
                },
                onNavigateToHistory = {
                  currentScreen = AppScreen.VISIT_HISTORY
                },
                onNavigateToBrowsePlans = {
                  currentScreen = AppScreen.MEMBERSHIP
                },
                modifier = Modifier.fillMaxSize()
              )
            }
            AppScreen.CHECK_IN -> {
              CheckInScreen(
                memberName = "Alex",
                gymName = selectedGymForDetail?.name ?: "Pulse Fitness",
                onBackClick = {
                  currentScreen = AppScreen.HOME
                },
                modifier = Modifier.fillMaxSize()
              )
            }
            AppScreen.GYM_DETAIL -> {
              GymDetailScreen(
                gym = selectedGymForDetail ?: SAMPLE_GYMS.first(),
                onBackClick = {
                  currentScreen = AppScreen.HOME
                },
                onCheckInClick = {
                  currentScreen = AppScreen.CHECK_IN
                },
                modifier = Modifier.fillMaxSize()
              )
            }
            AppScreen.DISCOVER -> {
              DiscoverScreen(
                onGymSelect = { gym ->
                  selectedGymForDetail = gym
                  currentScreen = AppScreen.GYM_DETAIL
                },
                onBackClick = {
                  currentScreen = AppScreen.HOME
                },
                onTabSelected = { tab ->
                  when (tab) {
                    "Home" -> currentScreen = AppScreen.HOME
                    "Discover" -> currentScreen = AppScreen.DISCOVER
                    "Gym Map" -> currentScreen = AppScreen.GYM_MAP
                    "Membership" -> currentScreen = AppScreen.MEMBERSHIP
                    "Profile" -> currentScreen = AppScreen.PROFILE
                  }
                },
                modifier = Modifier.fillMaxSize()
              )
            }
            AppScreen.GYM_MAP -> {
              GymMapScreen(
                onGymSelect = { gym ->
                  selectedGymForDetail = gym
                  currentScreen = AppScreen.GYM_DETAIL
                },
                onBackClick = {
                  currentScreen = AppScreen.HOME
                },
                onTabSelected = { tab ->
                  when (tab) {
                    "Home" -> currentScreen = AppScreen.HOME
                    "Discover" -> currentScreen = AppScreen.DISCOVER
                    "Gym Map" -> currentScreen = AppScreen.GYM_MAP
                    "Membership" -> currentScreen = AppScreen.MEMBERSHIP
                    "Profile" -> currentScreen = AppScreen.PROFILE
                  }
                },
                modifier = Modifier.fillMaxSize()
              )
            }
            AppScreen.MEMBERSHIP -> {
              MembershipScreen(
                onBackClick = {
                  currentScreen = AppScreen.HOME
                },
                onTabSelected = { tab ->
                  when (tab) {
                    "Home" -> currentScreen = AppScreen.HOME
                    "Discover" -> currentScreen = AppScreen.DISCOVER
                    "Gym Map" -> currentScreen = AppScreen.GYM_MAP
                    "Membership" -> currentScreen = AppScreen.MEMBERSHIP
                    "Profile" -> currentScreen = AppScreen.PROFILE
                  }
                },
                modifier = Modifier.fillMaxSize()
              )
            }
            AppScreen.PROFILE -> {
              ProfileScreen(
                onBackClick = {
                  currentScreen = AppScreen.HOME
                },
                onLogOutClick = {
                  currentScreen = AppScreen.AUTH
                },
                onTabSelected = { tab ->
                  when (tab) {
                    "Home" -> currentScreen = AppScreen.HOME
                    "Discover" -> currentScreen = AppScreen.DISCOVER
                    "Gym Map" -> currentScreen = AppScreen.GYM_MAP
                    "Membership" -> currentScreen = AppScreen.MEMBERSHIP
                    "Profile" -> currentScreen = AppScreen.PROFILE
                  }
                },
                onNavigateToAccountSettings = {
                  currentScreen = AppScreen.ACCOUNT_SETTINGS
                },
                onNavigateToPaymentMethods = {
                  currentScreen = AppScreen.PAYMENT_METHODS
                },
                onNavigateToNotifications = {
                  currentScreen = AppScreen.NOTIFICATION_SETTINGS
                },
                onNavigateToHelpCenter = {
                  currentScreen = AppScreen.HELP_CENTER
                },
                onNavigateToMembershipHistory = {
                  currentScreen = AppScreen.MEMBERSHIP_HISTORY
                },
                onNavigateToPrivacySecurity = {
                  currentScreen = AppScreen.PRIVACY_SECURITY,
    EDIT_PROFILE,
    APPEARANCE
                },
                modifier = Modifier.fillMaxSize()
              )
            }
            AppScreen.EDIT_PROFILE -> {
              EditProfileScreen(
                onBackClick = {
                  currentScreen = AppScreen.PROFILE
                },
                onSaveClick = {
                  currentScreen = AppScreen.PROFILE
                }
              )
            }
            AppScreen.APPEARANCE -> {
              AppearanceScreen(
                onBackClick = {
                  currentScreen = AppScreen.PROFILE
                }
              )
            }
            AppScreen.VISIT_HISTORY -> {
              VisitHistoryScreen(
                onBackClick = {
                  currentScreen = AppScreen.HOME
                },
                modifier = Modifier.fillMaxSize()
              )
            }
            AppScreen.ACCOUNT_SETTINGS -> {
              AccountSettingsScreen(
                onBackClick = {
                  currentScreen = AppScreen.PROFILE
                }
              )
            }
            AppScreen.PAYMENT_METHODS -> {
              PaymentMethodsScreen(
                onBackClick = {
                  currentScreen = AppScreen.PROFILE
                }
              )
            }
            AppScreen.NOTIFICATION_SETTINGS -> {
              NotificationSettingsScreen(
                onBackClick = {
                  currentScreen = AppScreen.PROFILE
                }
              )
            }
            AppScreen.HELP_CENTER -> {
              HelpCenterScreen(
                onBackClick = {
                  currentScreen = AppScreen.PROFILE
                }
              )
            }
            AppScreen.MEMBERSHIP_HISTORY -> {
              MembershipHistoryScreen(
                onBackClick = {
                  currentScreen = AppScreen.PROFILE
                }
              )
            }
            AppScreen.PRIVACY_SECURITY,
    EDIT_PROFILE,
    APPEARANCE -> {
              PrivacySecurityScreen(
                onBackClick = {
                  currentScreen = AppScreen.PROFILE
                }
              )
            }
            AppScreen.EDIT_PROFILE -> {
              EditProfileScreen(
                onBackClick = {
                  currentScreen = AppScreen.PROFILE
                },
                onSaveClick = {
                  currentScreen = AppScreen.PROFILE
                }
              )
            }
            AppScreen.APPEARANCE -> {
              AppearanceScreen(
                onBackClick = {
                  currentScreen = AppScreen.PROFILE
                }
              )
            }
          }
        }
      }
    }
  }
}


