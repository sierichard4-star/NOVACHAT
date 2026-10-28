package com.example.data

import com.example.model.*

object MockDataProvider {

    val currentUser = UserProfile(
        id = "usr_nova_001",
        name = "Alex Mercer",
        handle = "@alexm",
        avatarUrl = "",
        phone = "+233 55 123 4567",
        isVerified = true,
        status = "Supercharged by Nova Super App 🚀",
        biometricEnabled = true,
        twoFactorEnabled = true
    )

    val sampleStories = listOf(
        StatusStory("st_1", "Sarah K.", "", "", "Weekend getaway in Costa Rica! 🌊🌴", "Just now", false),
        StatusStory("st_2", "Dev Club", "", "", "Nova Hackathon starts in 2 hours! 💻🔥", "15m ago", false),
        StatusStory("st_3", "Elena Rostova", "", "", "Recorded a new podcast on SuperApps!", "1h ago", false),
        StatusStory("st_4", "Kwame Asante", "", "", "New marketplace products listed! 📦", "3h ago", true),
        StatusStory("st_5", "Chloe Zhao", "", "", "Tokyo sunset from Shibuya sky 🌇", "6h ago", true)
    )

    val sampleChats = listOf(
        ChatThread(
            id = "chat_1",
            peerName = "Sarah K.",
            peerAvatar = "",
            lastMessage = "Thanks for sending the MoMo transfer! 💸",
            lastTimestamp = "10:42 AM",
            unreadCount = 2,
            isOnline = true,
            isPinned = true,
            isEndToEndEncrypted = true,
            messages = listOf(
                ChatMessage("m1", "sarah", "Hey Alex! Can you split the lunch bill with MTN MoMo?", "10:30 AM", false, MessageType.TEXT),
                ChatMessage("m2", "me", "Sure thing! Sending 45 GHS now.", "10:35 AM", true, MessageType.TEXT),
                ChatMessage("m3", "me", "Transferred 45.00 via MTN MoMo", "10:36 AM", true, MessageType.PAYMENT, paymentAmount = 45.0, paymentProvider = "MTN MoMo"),
                ChatMessage("m4", "sarah", "Voice Note (0:14)", "10:40 AM", false, MessageType.VOICE, voiceDurationSeconds = 14),
                ChatMessage("m5", "sarah", "Thanks for sending the MoMo transfer! 💸", "10:42 AM", false, MessageType.TEXT)
            )
        ),
        ChatThread(
            id = "chat_2",
            peerName = "Kwame Asante",
            peerAvatar = "",
            lastMessage = "Contract document signed & encrypted with SHA-256",
            lastTimestamp = "Yesterday",
            unreadCount = 0,
            isOnline = false,
            isPinned = true,
            isEndToEndEncrypted = true,
            messages = listOf(
                ChatMessage("m21", "kwame", "Hey Alex, please review the vendor agreement.", "Yesterday", false, MessageType.TEXT),
                ChatMessage("m22", "me", "Here is the cryptographically signed PDF.", "Yesterday", true, MessageType.DOCUMENT, documentName = "Nova_Supplier_Agreement_2026.pdf"),
                ChatMessage("m23", "kwame", "Contract document signed & encrypted with SHA-256", "Yesterday", false, MessageType.TEXT)
            )
        ),
        ChatThread(
            id = "chat_3",
            peerName = "Nova AI Assistant",
            peerAvatar = "",
            lastMessage = "Your ride with Bolt has been compared. 15% cheaper than Uber!",
            lastTimestamp = "9:15 AM",
            unreadCount = 0,
            isOnline = true,
            isPinned = false,
            isEndToEndEncrypted = true,
            messages = listOf(
                ChatMessage("m31", "ai", "Hello Alex! I am your Nova Super App Intelligence. I can book rides, transfer funds via MoMo, track orders, or answer questions.", "9:00 AM", false, MessageType.TEXT),
                ChatMessage("m32", "me", "Compare rides to Kotoka Airport", "9:14 AM", true, MessageType.TEXT),
                ChatMessage("m33", "ai", "Your ride with Bolt has been compared. 15% cheaper than Uber!", "9:15 AM", false, MessageType.TEXT)
            )
        ),
        ChatThread(
            id = "chat_4",
            peerName = "Ecobank Alerts",
            peerAvatar = "",
            lastMessage = "NovaPay Linked Account Deposit confirmed: $1,250.00",
            lastTimestamp = "Oct 26",
            unreadCount = 0,
            isOnline = true,
            isPinned = false,
            isEndToEndEncrypted = true,
            messages = listOf(
                ChatMessage("m41", "eco", "NovaPay Linked Account Deposit confirmed: $1,250.00", "Oct 26", false, MessageType.TEXT)
            )
        ),
        ChatThread(
            id = "chat_5",
            peerName = "Tech Innovators Group (48)",
            peerAvatar = "",
            lastMessage = "Dave: Who checked out the new TikTok feed in Nova?",
            lastTimestamp = "Oct 25",
            unreadCount = 5,
            isOnline = false,
            isPinned = false,
            isEndToEndEncrypted = true,
            messages = listOf(
                ChatMessage("m51", "dave", "Who checked out the new TikTok feed in Nova?", "Oct 25", false, MessageType.TEXT)
            )
        )
    )

    val sampleShorts = listOf(
        ShortVideo(
            id = "vid_1",
            creatorName = "TechPulse Daily",
            creatorHandle = "@techpulse",
            creatorAvatar = "",
            title = "Unboxing the futuristic holographic display phone! 📱✨ #futuretech #superapp #gadgets",
            tags = listOf("#gadgets", "#tech", "#ai"),
            soundTrack = "Original Sound - TechPulse Chill Lo-Fi Beats",
            videoGradientColors = listOf(0xFF0F2027, 0xFF203A43, 0xFF2C5364),
            likesCount = 84200,
            commentsCount = 1240,
            sharesCount = 6500,
            attachedProduct = ProductItem(
                id = "p_holo",
                title = "HoloGlow 3D Mobile Projector",
                category = "Electronics",
                price = 149.99,
                originalPrice = 199.99,
                rating = 4.8,
                reviewsCount = 310,
                merchantName = "Apex Gadgets",
                description = "High resolution holographic projector attachment for modern smartphones."
            )
        ),
        ShortVideo(
            id = "vid_2",
            creatorName = "Chef Amara",
            creatorHandle = "@amaracooks",
            creatorAvatar = "",
            title = "60-second Authentic Ghanaian Jollof rice secret technique! 🍛🔥 #cooking #africanfood #jollof",
            tags = listOf("#foodie", "#ghana", "#recipe"),
            soundTrack = "Afrobeat Vibes 2026 - Burna & Wizkid Mix",
            videoGradientColors = listOf(0xFF8A2387, 0xFFE94057, 0xFFF27121),
            likesCount = 142500,
            commentsCount = 3890,
            sharesCount = 28100,
            attachedProduct = null
        ),
        ShortVideo(
            id = "vid_3",
            creatorName = "Nova Financial",
            creatorHandle = "@novapay",
            creatorAvatar = "",
            title = "How to automate your savings & send instant MoMo with 0 fee! 💰💳 #fintech #momo #novapay",
            tags = listOf("#finance", "#momo", "#moneytips"),
            soundTrack = "Nova Synthwave Theme - Electronic Bliss",
            videoGradientColors = listOf(0xFF11998E, 0xFF38EF7D),
            likesCount = 49100,
            commentsCount = 812,
            sharesCount = 4300,
            attachedProduct = null
        ),
        ShortVideo(
            id = "vid_4",
            creatorName = "Kojo Velocity",
            creatorHandle = "@kojovibes",
            creatorAvatar = "",
            title = "Testing Nova Ride: Bolt vs Uber vs Yango in rush hour traffic! 🚕⏱️",
            tags = listOf("#travel", "#rides", "#accra"),
            soundTrack = "High Energy Speed - Urban Bass",
            videoGradientColors = listOf(0xFF4A00E0, 0xFF8E2DE2),
            likesCount = 73200,
            commentsCount = 1920,
            sharesCount = 8800,
            attachedProduct = null
        )
    )

    val sampleProducts = listOf(
        ProductItem(
            id = "prod_1",
            title = "NovaPro Wireless Noise-Cancelling Earbuds",
            category = "Audio",
            price = 79.99,
            originalPrice = 129.99,
            rating = 4.9,
            reviewsCount = 1420,
            merchantName = "Nova Official Store",
            description = "Active noise cancellation up to 45dB, 36h battery life, and seamless pairing with Nova ecosystem."
        ),
        ProductItem(
            id = "prod_2",
            title = "Smart Fitness Band with Biometric Health Tracking",
            category = "Wearables",
            price = 49.50,
            originalPrice = 75.00,
            rating = 4.7,
            reviewsCount = 890,
            merchantName = "PulseTech Store",
            description = "Continuous SpO2, heart rate, sleep analytics, and direct sync with Nova Health Hub."
        ),
        ProductItem(
            id = "prod_3",
            title = "Solar-Powered 30,000mAh Ultra-Fast Power Bank",
            category = "Power",
            price = 38.00,
            originalPrice = 55.00,
            rating = 4.8,
            reviewsCount = 650,
            merchantName = "VoltGlobal",
            description = "Rugged waterproof power bank with dual USB-C 65W PD fast charging and flashlight."
        ),
        ProductItem(
            id = "prod_4",
            title = "Ergonomic Aluminium Laptop & Tablet Stand",
            category = "Office",
            price = 28.99,
            originalPrice = 45.00,
            rating = 4.6,
            reviewsCount = 412,
            merchantName = "ZenDesk Lifestyle",
            description = "Multi-angle adjustable ventilation stand engineered with aircraft-grade aluminum alloy."
        ),
        ProductItem(
            id = "prod_5",
            title = "Heritage Handwoven Kente Silk Scarf",
            category = "Fashion",
            price = 62.00,
            originalPrice = 85.00,
            rating = 5.0,
            reviewsCount = 210,
            merchantName = "Ashanti Artisans Guild",
            description = "100% authentic handwoven silk kente pattern made with traditional loom craftsmanship."
        )
    )

    val sampleRides = listOf(
        RideProviderOption("ride_bolt_eco", "Bolt", "Bolt Standard", 14.20, 3, 4.8, 0xFF34D399),
        RideProviderOption("ride_uber_x", "Uber", "UberX Saver", 15.80, 4, 4.9, 0xFF101010),
        RideProviderOption("ride_yango_eco", "Yango", "Yango Comfort", 13.50, 6, 4.7, 0xFFEF4444),
        RideProviderOption("ride_taxi_local", "Local Yellow Taxi", "Metered City Cab", 11.00, 2, 4.5, 0xFFF59E0B),
        RideProviderOption("ride_bolt_boda", "Bolt Boda", "Fast Moto Express", 6.50, 1, 4.9, 0xFF10B981)
    )

    val sampleTransactions = listOf(
        WalletTransaction("tx_1", "MTN MoMo - P2P to Sarah K.", PaymentProviderType.MOMO, 45.00, false, "Today, 10:36 AM", "REF-MOMO-882193"),
        WalletTransaction("tx_2", "Salary Deposit via Ecobank", PaymentProviderType.ECOBANK, 2800.00, true, "Oct 25, 2026", "REF-ECO-774102"),
        WalletTransaction("tx_3", "OPay Digital Merchant Payment", PaymentProviderType.OPAY, 24.50, false, "Oct 24, 2026", "REF-OPY-491021"),
        WalletTransaction("tx_4", "GCB Account Direct Top-Up", PaymentProviderType.GCB, 500.00, true, "Oct 22, 2026", "REF-GCB-993214"),
        WalletTransaction("tx_5", "Nova Ride - Bolt Trip fare", PaymentProviderType.MOMO, 14.20, false, "Oct 20, 2026", "REF-RIDE-104928")
    )

    val sampleSocialPosts = listOf(
        SocialPost(
            id = "sp_1",
            authorName = "Tech Innovation Africa",
            authorHandle = "@techafrica",
            authorAvatar = "",
            content = "The future of African fintech and super apps is here! Instant cross-border settlement, digital identity, and integrated creator economies all under one umbrella. 🌍🚀",
            timestamp = "2h ago",
            likesCount = 342,
            commentsCount = 28,
            isLiked = true
        ),
        SocialPost(
            id = "sp_2",
            authorName = "Sarah K.",
            authorHandle = "@sarahk",
            authorAvatar = "",
            content = "Just tested the facial recognition document signing in Nova. Literally signed and sealed an NDA in 5 seconds without leaving my chat! Mind blown 🤯",
            timestamp = "5h ago",
            likesCount = 189,
            commentsCount = 14
        )
    )

    val sampleSignedDocuments = listOf(
        SignedDocument("doc_1", "Nova_Supplier_Agreement_2026.pdf", "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", "Today, 09:40 AM", "Alex Mercer"),
        SignedDocument("doc_2", "Non_Disclosure_Agreement_v3.pdf", "dffd6021bb2bd5b0af676290809ec3a53191dd81c7f70a4b28688a362182986f", "Oct 24, 2026", "Alex Mercer")
    )
}
