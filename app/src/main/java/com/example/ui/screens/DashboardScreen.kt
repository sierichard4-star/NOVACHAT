package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PaymentProviderType
import com.example.ui.theme.*
import com.example.viewmodel.AppTab
import com.example.viewmodel.SuperAppUiState

@Composable
fun DashboardScreen(
    state: SuperAppUiState,
    onNavigateTab: (AppTab) -> Unit,
    onSendMoneyClick: () -> Unit,
    onQrClick: () -> Unit,
    onDocSignClick: () -> Unit,
    onCancelRide: () -> Unit
) {
    var isBalanceVisible by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("dashboard_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Welcome & Super App Wallet Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("wallet_balance_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF0F172A), Color(0xFF1E1B4B), Color(0xFF083344))
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(NovaCyan),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AccountBalanceWallet,
                                        contentDescription = null,
                                        tint = Color.Black,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Text(
                                    text = "NovaPay Multi-Currency Vault",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = NovaCyan
                                )
                            }
                            IconButton(onClick = { isBalanceVisible = !isBalanceVisible }) {
                                Icon(
                                    imageVector = if (isBalanceVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle Balance",
                                    tint = NovaTextSecondary
                                )
                            }
                        }

                        Column {
                            Text(
                                text = "Total Net Balance",
                                style = MaterialTheme.typography.bodySmall,
                                color = NovaTextSecondary
                            )
                            Text(
                                text = if (isBalanceVisible) "$${"%,.2f".format(state.walletBalance)}" else "••••••••",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        // Connected payment methods indicators
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            PaymentBadge(label = "MTN MoMo", color = NovaAmber)
                            PaymentBadge(label = "Ecobank", color = NovaCyan)
                            PaymentBadge(label = "OPay", color = NovaEmerald)
                            PaymentBadge(label = "GCB", color = NovaPurple)
                        }

                        HorizontalDivider(color = Color.White.copy(alpha = 0.15f))

                        // Quick Financial Actions
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            QuickActionPill(
                                label = "Send Money",
                                icon = Icons.Default.Send,
                                color = NovaCyan,
                                onClick = onSendMoneyClick,
                                tag = "quick_send_momo"
                            )
                            QuickActionPill(
                                label = "QR Pay",
                                icon = Icons.Default.QrCodeScanner,
                                color = NovaEmerald,
                                onClick = onQrClick,
                                tag = "quick_qr_pay"
                            )
                            QuickActionPill(
                                label = "Sign Doc",
                                icon = Icons.Default.Draw,
                                color = NovaPurple,
                                onClick = onDocSignClick,
                                tag = "quick_sign_doc"
                            )
                            QuickActionPill(
                                label = "Ride City",
                                icon = Icons.Default.DirectionsCar,
                                color = NovaAmber,
                                onClick = { onNavigateTab(AppTab.RIDE) },
                                tag = "quick_book_ride"
                            )
                        }
                    }
                }
            }
        }

        // Active Ride Banner (if booked)
        state.bookedRide?.let { ride ->
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("active_ride_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Color(ride.providerColor)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DirectionsCar,
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                }
                                Column {
                                    Text(
                                        text = "Nova Ride • ${ride.providerName} (${ride.carType})",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = state.rideStatus ?: "Driver en route",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = NovaEmerald
                                    )
                                }
                            }
                            TextButton(onClick = onCancelRide) {
                                Text("Cancel", color = Color(0xFFEF4444))
                            }
                        }
                    }
                }
            }
        }

        // Super App Hub Grid
        item {
            Text(
                text = "Super App Services",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                HubCategoryCard(
                    title = "TikTok Shorts",
                    subtitle = "Algorithmic video stream",
                    icon = Icons.Default.PlayCircle,
                    gradient = listOf(Color(0xFFEC4899), Color(0xFF8B5CF6)),
                    modifier = Modifier.weight(1f),
                    tag = "hub_card_shorts",
                    onClick = { onNavigateTab(AppTab.SHORTS) }
                )
                HubCategoryCard(
                    title = "E-Commerce",
                    subtitle = "Marketplace & MoMo Pay",
                    icon = Icons.Default.ShoppingBag,
                    gradient = listOf(Color(0xFF06B6D4), Color(0xFF3B82F6)),
                    modifier = Modifier.weight(1f),
                    tag = "hub_card_marketplace",
                    onClick = { onNavigateTab(AppTab.MARKETPLACE) }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                HubCategoryCard(
                    title = "WhatsApp Chat",
                    subtitle = "E2EE Voice & Calls",
                    icon = Icons.Default.Chat,
                    gradient = listOf(Color(0xFF10B981), Color(0xFF059669)),
                    modifier = Modifier.weight(1f),
                    tag = "hub_card_chats",
                    onClick = { onNavigateTab(AppTab.CHATS) }
                )
                HubCategoryCard(
                    title = "Nova AI Brain",
                    subtitle = "Automated Super Assistant",
                    icon = Icons.Default.AutoAwesome,
                    gradient = listOf(Color(0xFF6366F1), Color(0xFFA855F7)),
                    modifier = Modifier.weight(1f),
                    tag = "hub_card_ai",
                    onClick = { onNavigateTab(AppTab.AI_ASSISTANT) }
                )
            }
        }

        // WhatsApp Stories / Moments Highlight Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Moments & Status Updates",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = { onNavigateTab(AppTab.FEED) }) {
                    Text("View All", color = NovaCyan)
                }
            }
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(state.stories) { story ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(72.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.sweepGradient(
                                        listOf(NovaCyan, NovaPurple, NovaPink, NovaCyan)
                                    )
                                )
                                .padding(2.5.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surface),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = story.userName.take(2).uppercase(),
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 16.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = story.userName,
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        // Financial Analytics & Spending Overview
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("spending_analytics_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Financial Activity Analytics",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "This Month",
                            style = MaterialTheme.typography.bodySmall,
                            color = NovaTextSecondary
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        AnalyticsStat(label = "Income", value = "+$3,300.00", color = NovaEmerald)
                        AnalyticsStat(label = "Expenses", value = "-$842.10", color = Color(0xFFEF4444))
                        AnalyticsStat(label = "Saved", value = "$1,450.00", color = NovaCyan)
                    }
                }
            }
        }
    }
}

@Composable
private fun PaymentBadge(label: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.2f),
        contentColor = color
    ) {
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun QuickActionPill(
    label: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    tag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .testTag(tag)
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = Color.White)
    }
}

@Composable
private fun HubCategoryCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    gradient: List<Color>,
    modifier: Modifier = Modifier,
    tag: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(115.dp)
            .clickable(onClick = onClick)
            .testTag(tag),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(gradient))
                .padding(14.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun AnalyticsStat(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = NovaTextSecondary)
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}
