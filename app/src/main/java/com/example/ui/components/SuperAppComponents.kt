package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.viewmodel.AppTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperAppTopBar(
    title: String,
    onQrClick: () -> Unit,
    onSecurityClick: () -> Unit,
    onAiClick: () -> Unit,
    isOffline: Boolean,
    onToggleOffline: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        ),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(listOf(NovaCyan, NovaIndigo))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "N",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(if (isOffline) NovaAmber else NovaEmerald)
                        )
                        Text(
                            text = if (isOffline) "Offline Cache Active" else "Nova Super Network",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isOffline) NovaAmber else NovaTextSecondary
                        )
                    }
                }
            }
        },
        actions = {
            IconButton(
                onClick = onToggleOffline,
                modifier = Modifier.testTag("toggle_offline_button")
            ) {
                Icon(
                    imageVector = if (isOffline) Icons.Default.CloudOff else Icons.Default.CloudQueue,
                    contentDescription = "Toggle Offline",
                    tint = if (isOffline) NovaAmber else MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(
                onClick = onQrClick,
                modifier = Modifier.testTag("qr_scan_button")
            ) {
                Icon(
                    imageVector = Icons.Default.QrCodeScanner,
                    contentDescription = "QR Scanner",
                    tint = NovaCyan
                )
            }
            IconButton(
                onClick = onAiClick,
                modifier = Modifier.testTag("nova_ai_top_button")
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Nova AI Assistant",
                    tint = NovaPurple
                )
            }
            IconButton(
                onClick = onSecurityClick,
                modifier = Modifier.testTag("security_shield_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = "Security & Biometrics",
                    tint = NovaEmerald
                )
            }
        }
    )
}

@Composable
fun SuperAppBottomNav(
    currentTab: AppTab,
    unreadChatsCount: Int,
    cartItemCount: Int,
    onTabSelected: (AppTab) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        windowInsets = WindowInsets.navigationBars
    ) {
        NavigationBarItem(
            selected = currentTab == AppTab.DASHBOARD,
            onClick = { onTabSelected(AppTab.DASHBOARD) },
            icon = {
                Icon(
                    imageVector = if (currentTab == AppTab.DASHBOARD) Icons.Filled.Dashboard else Icons.Outlined.Dashboard,
                    contentDescription = "Dashboard"
                )
            },
            label = { Text("Hub", fontSize = 11.sp) },
            modifier = Modifier.testTag("nav_dashboard")
        )

        NavigationBarItem(
            selected = currentTab == AppTab.CHATS,
            onClick = { onTabSelected(AppTab.CHATS) },
            icon = {
                BadgedBox(
                    badge = {
                        if (unreadChatsCount > 0) {
                            Badge { Text("$unreadChatsCount") }
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (currentTab == AppTab.CHATS) Icons.Filled.Chat else Icons.Outlined.Chat,
                        contentDescription = "Chats"
                    )
                }
            },
            label = { Text("Chats", fontSize = 11.sp) },
            modifier = Modifier.testTag("nav_chats")
        )

        NavigationBarItem(
            selected = currentTab == AppTab.SHORTS,
            onClick = { onTabSelected(AppTab.SHORTS) },
            icon = {
                Icon(
                    imageVector = if (currentTab == AppTab.SHORTS) Icons.Filled.PlayCircle else Icons.Outlined.PlayCircleOutline,
                    contentDescription = "Shorts",
                    tint = if (currentTab == AppTab.SHORTS) NovaPink else MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            label = { Text("Shorts", fontSize = 11.sp) },
            modifier = Modifier.testTag("nav_shorts")
        )

        NavigationBarItem(
            selected = currentTab == AppTab.NOVAPAY,
            onClick = { onTabSelected(AppTab.NOVAPAY) },
            icon = {
                Icon(
                    imageVector = if (currentTab == AppTab.NOVAPAY) Icons.Filled.AccountBalanceWallet else Icons.Outlined.AccountBalanceWallet,
                    contentDescription = "NovaPay",
                    tint = if (currentTab == AppTab.NOVAPAY) NovaCyan else MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            label = { Text("NovaPay", fontSize = 11.sp) },
            modifier = Modifier.testTag("nav_novapay")
        )

        NavigationBarItem(
            selected = currentTab == AppTab.MARKETPLACE,
            onClick = { onTabSelected(AppTab.MARKETPLACE) },
            icon = {
                BadgedBox(
                    badge = {
                        if (cartItemCount > 0) {
                            Badge(containerColor = NovaCyan) { Text("$cartItemCount", color = Color.Black) }
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (currentTab == AppTab.MARKETPLACE) Icons.Filled.Storefront else Icons.Outlined.Storefront,
                        contentDescription = "Marketplace"
                    )
                }
            },
            label = { Text("Market", fontSize = 11.sp) },
            modifier = Modifier.testTag("nav_market")
        )

        NavigationBarItem(
            selected = currentTab == AppTab.RIDE,
            onClick = { onTabSelected(AppTab.RIDE) },
            icon = {
                Icon(
                    imageVector = if (currentTab == AppTab.RIDE) Icons.Filled.DirectionsCar else Icons.Outlined.DirectionsCar,
                    contentDescription = "Rides"
                )
            },
            label = { Text("Rides", fontSize = 11.sp) },
            modifier = Modifier.testTag("nav_ride")
        )
    }
}
