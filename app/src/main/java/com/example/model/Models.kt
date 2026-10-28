package com.example.model

data class UserProfile(
    val id: String,
    val name: String,
    val handle: String,
    val avatarUrl: String,
    val phone: String,
    val isVerified: Boolean = true,
    val status: String = "Living in the future with NovaChat ✨",
    val biometricEnabled: Boolean = true,
    val twoFactorEnabled: Boolean = true
)

enum class MessageType {
    TEXT, VOICE, IMAGE, PAYMENT, DOCUMENT
}

data class ChatMessage(
    val id: String,
    val senderId: String,
    val text: String = "",
    val timestamp: String,
    val isFromMe: Boolean,
    val type: MessageType = MessageType.TEXT,
    val voiceDurationSeconds: Int = 0,
    val paymentAmount: Double? = null,
    val paymentProvider: String? = null,
    val documentName: String? = null,
    val isEncrypted: Boolean = true,
    val isRead: Boolean = true
)

data class ChatThread(
    val id: String,
    val peerName: String,
    val peerAvatar: String,
    val lastMessage: String,
    val lastTimestamp: String,
    val unreadCount: Int = 0,
    val isOnline: Boolean = false,
    val isPinned: Boolean = false,
    val isEndToEndEncrypted: Boolean = true,
    val messages: List<ChatMessage> = emptyList()
)

data class StatusStory(
    val id: String,
    val userName: String,
    val userAvatar: String,
    val imageUrl: String,
    val caption: String,
    val timeAgo: String,
    val isViewed: Boolean = false
)

data class ShortVideo(
    val id: String,
    val creatorName: String,
    val creatorHandle: String,
    val creatorAvatar: String,
    val title: String,
    val tags: List<String>,
    val soundTrack: String,
    val videoGradientColors: List<Long>,
    val likesCount: Int,
    val commentsCount: Int,
    val sharesCount: Int,
    val isLiked: Boolean = false,
    val isSaved: Boolean = false,
    val attachedProduct: ProductItem? = null
)

data class SocialPost(
    val id: String,
    val authorName: String,
    val authorHandle: String,
    val authorAvatar: String,
    val content: String,
    val timestamp: String,
    val likesCount: Int,
    val commentsCount: Int,
    val isLiked: Boolean = false,
    val imageUrl: String? = null
)

data class ProductItem(
    val id: String,
    val title: String,
    val category: String,
    val price: Double,
    val originalPrice: Double,
    val rating: Double,
    val reviewsCount: Int,
    val merchantName: String,
    val description: String,
    val inStock: Boolean = true
)

data class RideProviderOption(
    val id: String,
    val providerName: String, // Uber, Bolt, Yango, Local Taxi
    val carType: String, // Economy, Comfort, Boda/Moto, XL
    val price: Double,
    val etaMinutes: Int,
    val rating: Double,
    val providerColor: Long
)

enum class PaymentProviderType {
    MOMO, ECOBANK, OPAY, GCB, BANK_TRANSFER, VISA_MASTERCARD
}

data class WalletTransaction(
    val id: String,
    val title: String,
    val provider: PaymentProviderType,
    val amount: Double,
    val isCredit: Boolean,
    val timestamp: String,
    val referenceCode: String,
    val status: String = "Completed"
)

data class SignedDocument(
    val id: String,
    val title: String,
    val hashSha256: String,
    val signedAt: String,
    val signerName: String,
    val status: String = "Cryptographically Verified"
)
