package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MockDataProvider
import com.example.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AppTab {
    DASHBOARD, CHATS, SHORTS, NOVAPAY, MARKETPLACE, RIDE, FEED, SECURITY, AI_ASSISTANT
}

data class CartItem(
    val product: ProductItem,
    val quantity: Int = 1
)

data class ActiveCallState(
    val peerName: String,
    val isVideo: Boolean,
    val durationSeconds: Int = 0,
    val isConnected: Boolean = false,
    val isMuted: Boolean = false,
    val isCameraOn: Boolean = true
)

data class SuperAppUiState(
    val currentTab: AppTab = AppTab.DASHBOARD,
    val selectedChatId: String? = null,
    val activeCall: ActiveCallState? = null,
    val userProfile: UserProfile = MockDataProvider.currentUser,
    val chats: List<ChatThread> = MockDataProvider.sampleChats,
    val stories: List<StatusStory> = MockDataProvider.sampleStories,
    val shorts: List<ShortVideo> = MockDataProvider.sampleShorts,
    val products: List<ProductItem> = MockDataProvider.sampleProducts,
    val rides: List<RideProviderOption> = MockDataProvider.sampleRides,
    val transactions: List<WalletTransaction> = MockDataProvider.sampleTransactions,
    val socialPosts: List<SocialPost> = MockDataProvider.sampleSocialPosts,
    val signedDocuments: List<SignedDocument> = MockDataProvider.sampleSignedDocuments,
    val cart: List<CartItem> = emptyList(),
    val walletBalance: Double = 4285.50,
    val selectedCurrency: String = "USD", // USD, GHS, NGN, KES
    val isBiometricUnlocked: Boolean = true,
    val isBiometricScanning: Boolean = false,
    val isOfflineMode: Boolean = false,
    val cachedDataBytes: Long = 48_520_100L, // 48.5 MB offline cached
    val bookedRide: RideProviderOption? = null,
    val rideStatus: String? = null,
    val notificationMessage: String? = null,
    val showQrModal: Boolean = false,
    val showSendMoneyModal: Boolean = false,
    val showDocSignModal: Boolean = false,
    val deepLinkNotification: String? = null
)

class SuperAppViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SuperAppUiState())
    val uiState: StateFlow<SuperAppUiState> = _uiState.asStateFlow()

    fun switchTab(tab: AppTab) {
        _uiState.update { it.copy(currentTab = tab, selectedChatId = null) }
    }

    fun openChat(chatId: String) {
        _uiState.update { it.copy(selectedChatId = chatId) }
    }

    fun closeChat() {
        _uiState.update { it.copy(selectedChatId = null) }
    }

    fun sendMessage(chatId: String, text: String) {
        if (text.isBlank()) return
        val newMsg = ChatMessage(
            id = "msg_${System.currentTimeMillis()}",
            senderId = "me",
            text = text,
            timestamp = "Just now",
            isFromMe = true,
            type = MessageType.TEXT
        )
        _uiState.update { state ->
            val updatedChats = state.chats.map { chat ->
                if (chat.id == chatId) {
                    chat.copy(
                        lastMessage = text,
                        lastTimestamp = "Just now",
                        messages = chat.messages + newMsg
                    )
                } else chat
            }
            state.copy(chats = updatedChats)
        }

        // Automated AI reply if messaging Nova AI Assistant
        if (chatId == "chat_3") {
            viewModelScope.launch {
                delay(1200)
                val replyText = generateAiResponse(text)
                val aiMsg = ChatMessage(
                    id = "msg_ai_${System.currentTimeMillis()}",
                    senderId = "ai",
                    text = replyText,
                    timestamp = "Just now",
                    isFromMe = false,
                    type = MessageType.TEXT
                )
                _uiState.update { state ->
                    val updatedChats = state.chats.map { chat ->
                        if (chat.id == chatId) {
                            chat.copy(
                                lastMessage = replyText,
                                lastTimestamp = "Just now",
                                messages = chat.messages + aiMsg
                            )
                        } else chat
                    }
                    state.copy(chats = updatedChats)
                }
            }
        }
    }

    fun sendVoiceMessage(chatId: String, durationSeconds: Int = 12) {
        val newVoiceMsg = ChatMessage(
            id = "voice_${System.currentTimeMillis()}",
            senderId = "me",
            text = "Voice message ($durationSeconds s)",
            timestamp = "Just now",
            isFromMe = true,
            type = MessageType.VOICE,
            voiceDurationSeconds = durationSeconds
        )
        _uiState.update { state ->
            val updatedChats = state.chats.map { chat ->
                if (chat.id == chatId) {
                    chat.copy(
                        lastMessage = "Voice message ($durationSeconds s)",
                        lastTimestamp = "Just now",
                        messages = chat.messages + newVoiceMsg
                    )
                } else chat
            }
            state.copy(chats = updatedChats)
        }
    }

    fun startCall(peerName: String, isVideo: Boolean) {
        _uiState.update {
            it.copy(
                activeCall = ActiveCallState(
                    peerName = peerName,
                    isVideo = isVideo,
                    durationSeconds = 0,
                    isConnected = false
                )
            )
        }
        viewModelScope.launch {
            delay(1500)
            _uiState.update { state ->
                state.activeCall?.let { call ->
                    state.copy(activeCall = call.copy(isConnected = true))
                } ?: state
            }
        }
    }

    fun endCall() {
        _uiState.update { it.copy(activeCall = null) }
    }

    fun toggleCallMute() {
        _uiState.update { state ->
            state.activeCall?.let { call ->
                state.copy(activeCall = call.copy(isMuted = !call.isMuted))
            } ?: state
        }
    }

    fun toggleCallCamera() {
        _uiState.update { state ->
            state.activeCall?.let { call ->
                state.copy(activeCall = call.copy(isCameraOn = !call.isCameraOn))
            } ?: state
        }
    }

    fun sendMoney(
        recipientName: String,
        amount: Double,
        provider: PaymentProviderType,
        note: String
    ) {
        if (amount <= 0 || amount > _uiState.value.walletBalance) {
            showNotification("Insufficient wallet balance for this transfer")
            return
        }

        val ref = "REF-${provider.name}-${(100000..999999).random()}"
        val newTx = WalletTransaction(
            id = "tx_${System.currentTimeMillis()}",
            title = "${provider.name} - Sent to $recipientName",
            provider = provider,
            amount = amount,
            isCredit = false,
            timestamp = "Just now",
            referenceCode = ref
        )

        _uiState.update { state ->
            state.copy(
                walletBalance = state.walletBalance - amount,
                transactions = listOf(newTx) + state.transactions,
                showSendMoneyModal = false,
                notificationMessage = "Successfully transferred $$amount via ${provider.name} to $recipientName! Ref: $ref"
            )
        }
    }

    fun toggleShortLike(videoId: String) {
        _uiState.update { state ->
            val updatedShorts = state.shorts.map { vid ->
                if (vid.id == videoId) {
                    val newLiked = !vid.isLiked
                    vid.copy(
                        isLiked = newLiked,
                        likesCount = if (newLiked) vid.likesCount + 1 else vid.likesCount - 1
                    )
                } else vid
            }
            state.copy(shorts = updatedShorts)
        }
    }

    fun togglePostLike(postId: String) {
        _uiState.update { state ->
            val updatedPosts = state.socialPosts.map { post ->
                if (post.id == postId) {
                    val newLiked = !post.isLiked
                    post.copy(
                        isLiked = newLiked,
                        likesCount = if (newLiked) post.likesCount + 1 else post.likesCount - 1
                    )
                } else post
            }
            state.copy(socialPosts = updatedPosts)
        }
    }

    fun addToCart(product: ProductItem) {
        _uiState.update { state ->
            val existing = state.cart.find { it.product.id == product.id }
            val newCart = if (existing != null) {
                state.cart.map {
                    if (it.product.id == product.id) it.copy(quantity = it.quantity + 1)
                    else it
                }
            } else {
                state.cart + CartItem(product = product, quantity = 1)
            }
            state.copy(cart = newCart, notificationMessage = "Added ${product.title} to cart 🛒")
        }
    }

    fun checkoutCart() {
        val total = _uiState.value.cart.sumOf { it.product.price * it.quantity }
        if (total > _uiState.value.walletBalance) {
            showNotification("Insufficient NovaPay balance to checkout")
            return
        }
        val ref = "REF-MKT-${(100000..999999).random()}"
        val tx = WalletTransaction(
            id = "tx_${System.currentTimeMillis()}",
            title = "Nova Marketplace - Order Checkout",
            provider = PaymentProviderType.MOMO,
            amount = total,
            isCredit = false,
            timestamp = "Just now",
            referenceCode = ref
        )
        _uiState.update { state ->
            state.copy(
                walletBalance = state.walletBalance - total,
                transactions = listOf(tx) + state.transactions,
                cart = emptyList(),
                notificationMessage = "Order placed! Paid $$total with NovaPay (Ref: $ref)"
            )
        }
    }

    fun bookRide(ride: RideProviderOption) {
        _uiState.update {
            it.copy(
                bookedRide = ride,
                rideStatus = "Connecting with nearest ${ride.providerName} driver..."
            )
        }
        viewModelScope.launch {
            delay(2000)
            _uiState.update {
                it.copy(rideStatus = "Driver matched! Toyota Corolla (GN 4821-25) arriving in ${ride.etaMinutes} mins")
            }
            delay(3000)
            _uiState.update {
                it.copy(rideStatus = "Trip in progress with ${ride.providerName}. Live GPS tracking active.")
            }
        }
    }

    fun cancelRide() {
        _uiState.update { it.copy(bookedRide = null, rideStatus = null) }
        showNotification("Ride cancelled")
    }

    fun signDocument(title: String, signerName: String) {
        val hash = "sha256_" + System.currentTimeMillis().toString(16) + "e481b990"
        val doc = SignedDocument(
            id = "doc_${System.currentTimeMillis()}",
            title = title,
            hashSha256 = hash,
            signedAt = "Just now",
            signerName = signerName
        )
        _uiState.update { state ->
            state.copy(
                signedDocuments = listOf(doc) + state.signedDocuments,
                showDocSignModal = false,
                notificationMessage = "Document '$title' cryptographically signed with biometric verification!"
            )
        }
    }

    fun authenticateBiometrics() {
        _uiState.update { it.copy(isBiometricScanning = true) }
        viewModelScope.launch {
            delay(1500)
            _uiState.update {
                it.copy(
                    isBiometricScanning = false,
                    isBiometricUnlocked = true,
                    notificationMessage = "Facial recognition & biometric ID verified ✓"
                )
            }
        }
    }

    fun lockApp() {
        _uiState.update { it.copy(isBiometricUnlocked = false) }
    }

    fun toggleOfflineMode() {
        _uiState.update {
            val newState = !it.isOfflineMode
            it.copy(
                isOfflineMode = newState,
                notificationMessage = if (newState) "Offline mode active. Using encrypted local caches." else "Online mode connected to Nova Super Cloud."
            )
        }
    }

    fun clearCache() {
        _uiState.update {
            it.copy(
                cachedDataBytes = 1_200_000L,
                notificationMessage = "Cleaned 47.3 MB of temporary media cache. Offline database optimized."
            )
        }
    }

    fun setShowQrModal(show: Boolean) {
        _uiState.update { it.copy(showQrModal = show) }
    }

    fun setShowSendMoneyModal(show: Boolean) {
        _uiState.update { it.copy(showSendMoneyModal = show) }
    }

    fun setShowDocSignModal(show: Boolean) {
        _uiState.update { it.copy(showDocSignModal = show) }
    }

    fun dismissNotification() {
        _uiState.update { it.copy(notificationMessage = null) }
    }

    fun showNotification(msg: String) {
        _uiState.update { it.copy(notificationMessage = msg) }
    }

    fun handleDeepLink(uri: String) {
        _uiState.update { it.copy(deepLinkNotification = "Navigated via Deep Link: $uri") }
        when {
            uri.contains("pay") || uri.contains("momo") -> switchTab(AppTab.NOVAPAY)
            uri.contains("ride") -> switchTab(AppTab.RIDE)
            uri.contains("shorts") || uri.contains("tiktok") -> switchTab(AppTab.SHORTS)
            uri.contains("shop") -> switchTab(AppTab.MARKETPLACE)
            uri.contains("ai") -> switchTab(AppTab.AI_ASSISTANT)
            else -> switchTab(AppTab.DASHBOARD)
        }
    }

    private fun generateAiResponse(query: String): String {
        val q = query.lowercase()
        return when {
            q.contains("ride") || q.contains("uber") || q.contains("bolt") ->
                "I analyzed city traffic and provider rates! Bolt is currently your best deal at $14.20 (3 min ETA), while Local Taxi is $11.00. Would you like me to book one?"
            q.contains("momo") || q.contains("money") || q.contains("pay") ->
                "NovaPay is ready. You have $4,285.50 across your MTN MoMo and Ecobank vaults. You can transfer instantly with zero platform fee."
            q.contains("video") || q.contains("tiktok") ->
                "Trending now on Nova Shorts: Afrobeat recipes by Chef Amara and futuristic holographic tech unboxings. Tap the Shorts tab to watch!"
            q.contains("security") || q.contains("sign") ->
                "All Nova communications are end-to-end encrypted with AES-256 and Signal Protocol. Document signing uses SHA-256 facial biometrics."
            else ->
                "Nova AI has processed your request: '$query'. I can initiate transactions, book rides, translate chats, or search the marketplace for you!"
        }
    }
}
