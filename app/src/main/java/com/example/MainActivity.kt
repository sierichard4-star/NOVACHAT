package com.example

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NovaCyan
import com.example.viewmodel.AppTab
import com.example.viewmodel.SuperAppViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: SuperAppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Handle intent deep links (novachat:// or web links)
        handleIntentData(intent)

        setContent {
            MyApplicationTheme {
                NovaSuperAppMain(viewModel = viewModel)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntentData(intent)
    }

    private fun handleIntentData(intent: Intent?) {
        intent?.data?.let { uri ->
            viewModel.handleDeepLink(uri.toString())
        }
    }
}

@Composable
fun NovaSuperAppMain(viewModel: SuperAppViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Display temporary toast/notification messages
    LaunchedEffect(state.notificationMessage) {
        state.notificationMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg, duration = SnackbarDuration.Short)
            viewModel.dismissNotification()
        }
    }

    LaunchedEffect(state.deepLinkNotification) {
        state.deepLinkNotification?.let { msg ->
            snackbarHostState.showSnackbar(msg, duration = SnackbarDuration.Short)
        }
    }

    // Handle back button when on sub-screens
    if (state.selectedChatId == null && state.currentTab != AppTab.DASHBOARD) {
        BackHandler {
            viewModel.switchTab(AppTab.DASHBOARD)
        }
    }

    // If a chat is selected, show detail view
    val selectedChat = remember(state.selectedChatId, state.chats) {
        state.chats.find { it.id == state.selectedChatId }
    }

    if (selectedChat != null) {
        ChatDetailScreen(
            chat = selectedChat,
            onBack = { viewModel.closeChat() },
            onSendMessage = { text -> viewModel.sendMessage(selectedChat.id, text) },
            onSendVoiceMessage = { viewModel.sendVoiceMessage(selectedChat.id) },
            onQuickMoMo = { viewModel.setShowSendMoneyModal(true) },
            onStartCall = { isVideo -> viewModel.startCall(selectedChat.peerName, isVideo) }
        )
    } else {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                val title = when (state.currentTab) {
                    AppTab.DASHBOARD -> "NovaChat"
                    AppTab.CHATS -> "Encrypted Chats"
                    AppTab.SHORTS -> "Nova Shorts"
                    AppTab.NOVAPAY -> "NovaPay Vault"
                    AppTab.MARKETPLACE -> "Marketplace"
                    AppTab.RIDE -> "Nova Ride"
                    AppTab.FEED -> "Social Moments"
                    AppTab.SECURITY -> "X Biometrics & Enclave"
                    AppTab.AI_ASSISTANT -> "Nova AI Assistant"
                }
                SuperAppTopBar(
                    title = title,
                    onQrClick = { viewModel.setShowQrModal(true) },
                    onSecurityClick = { viewModel.switchTab(AppTab.SECURITY) },
                    onAiClick = { viewModel.switchTab(AppTab.AI_ASSISTANT) },
                    isOffline = state.isOfflineMode,
                    onToggleOffline = { viewModel.toggleOfflineMode() }
                )
            },
            bottomBar = {
                SuperAppBottomNav(
                    currentTab = state.currentTab,
                    unreadChatsCount = state.chats.sumOf { it.unreadCount },
                    cartItemCount = state.cart.sumOf { it.quantity },
                    onTabSelected = { tab -> viewModel.switchTab(tab) }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (state.currentTab) {
                    AppTab.DASHBOARD -> DashboardScreen(
                        state = state,
                        onNavigateTab = { tab -> viewModel.switchTab(tab) },
                        onSendMoneyClick = { viewModel.setShowSendMoneyModal(true) },
                        onQrClick = { viewModel.setShowQrModal(true) },
                        onDocSignClick = { viewModel.setShowDocSignModal(true) },
                        onCancelRide = { viewModel.cancelRide() }
                    )

                    AppTab.CHATS -> ChatsScreen(
                        chats = state.chats,
                        stories = state.stories,
                        onChatClick = { id -> viewModel.openChat(id) },
                        onStoryClick = { story ->
                            viewModel.showNotification("Viewing 24h status of ${story.userName}: '${story.caption}'")
                        }
                    )

                    AppTab.SHORTS -> ShortsScreen(
                        shorts = state.shorts,
                        onToggleLike = { id -> viewModel.toggleShortLike(id) },
                        onAddToCart = { prod -> viewModel.addToCart(prod) },
                        onShowNotification = { msg -> viewModel.showNotification(msg) }
                    )

                    AppTab.NOVAPAY -> NovaPayScreen(
                        walletBalance = state.walletBalance,
                        transactions = state.transactions,
                        onSendMoneyClick = { viewModel.setShowSendMoneyModal(true) },
                        onQrClick = { viewModel.setShowQrModal(true) },
                        onShowNotification = { msg -> viewModel.showNotification(msg) }
                    )

                    AppTab.MARKETPLACE -> MarketplaceScreen(
                        products = state.products,
                        cart = state.cart,
                        walletBalance = state.walletBalance,
                        onAddToCart = { prod -> viewModel.addToCart(prod) },
                        onCheckout = { viewModel.checkoutCart() }
                    )

                    AppTab.RIDE -> NovaRideScreen(
                        rides = state.rides,
                        bookedRide = state.bookedRide,
                        rideStatus = state.rideStatus,
                        onBookRide = { ride -> viewModel.bookRide(ride) },
                        onCancelRide = { viewModel.cancelRide() }
                    )

                    AppTab.FEED -> SocialFeedScreen(
                        posts = state.socialPosts,
                        stories = state.stories,
                        onToggleLike = { id -> viewModel.togglePostLike(id) },
                        onStoryClick = { story ->
                            viewModel.showNotification("Status of ${story.userName}: ${story.caption}")
                        },
                        onShowNotification = { msg -> viewModel.showNotification(msg) }
                    )

                    AppTab.SECURITY -> SecurityScreen(
                        userProfile = state.userProfile,
                        isBiometricScanning = state.isBiometricScanning,
                        signedDocuments = state.signedDocuments,
                        cachedBytes = state.cachedDataBytes,
                        isOffline = state.isOfflineMode,
                        onAuthenticateBiometrics = { viewModel.authenticateBiometrics() },
                        onOpenDocSign = { viewModel.setShowDocSignModal(true) },
                        onToggleOffline = { viewModel.toggleOfflineMode() },
                        onClearCache = { viewModel.clearCache() },
                        onTestDeepLink = { link -> viewModel.handleDeepLink(link) }
                    )

                    AppTab.AI_ASSISTANT -> {
                        val aiChat = state.chats.find { it.id == "chat_3" }
                        NovaAIScreen(
                            aiChatThread = aiChat,
                            onSendMessage = { q -> viewModel.sendMessage("chat_3", q) }
                        )
                    }
                }
            }
        }
    }

    // Interactive Modals
    if (state.showSendMoneyModal) {
        SendMoneyDialog(
            currentBalance = state.walletBalance,
            onDismiss = { viewModel.setShowSendMoneyModal(false) },
            onSend = { recipient, amount, provider, note ->
                viewModel.sendMoney(recipient, amount, provider, note)
            }
        )
    }

    if (state.showQrModal) {
        QrModal(
            userHandle = state.userProfile.handle,
            onDismiss = { viewModel.setShowQrModal(false) }
        )
    }

    if (state.showDocSignModal) {
        DocumentSigningDialog(
            signerName = state.userProfile.name,
            onDismiss = { viewModel.setShowDocSignModal(false) },
            onSign = { docTitle ->
                viewModel.signDocument(docTitle, state.userProfile.name)
            }
        )
    }

    state.activeCall?.let { call ->
        VideoCallDialog(
            callState = call,
            onEndCall = { viewModel.endCall() },
            onToggleMute = { viewModel.toggleCallMute() },
            onToggleCamera = { viewModel.toggleCallCamera() }
        )
    }
}
