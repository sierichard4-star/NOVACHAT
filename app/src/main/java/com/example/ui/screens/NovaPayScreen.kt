package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PaymentProviderType
import com.example.model.WalletTransaction
import com.example.ui.theme.*

@Composable
fun NovaPayScreen(
    walletBalance: Double,
    transactions: List<WalletTransaction>,
    onSendMoneyClick: () -> Unit,
    onQrClick: () -> Unit,
    onShowNotification: (String) -> Unit
) {
    var selectedFilter by remember { mutableStateOf("ALL") }

    val filteredTransactions = remember(selectedFilter, transactions) {
        when (selectedFilter) {
            "MOMO" -> transactions.filter { it.provider == PaymentProviderType.MOMO }
            "BANK" -> transactions.filter { it.provider == PaymentProviderType.ECOBANK || it.provider == PaymentProviderType.GCB }
            "OPAY" -> transactions.filter { it.provider == PaymentProviderType.OPAY }
            else -> transactions
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("novapay_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Futuristic NovaPay Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("novapay_header_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF0F172A), Color(0xFF1E3A8A), Color(0xFF0D9488))
                            )
                        )
                        .padding(22.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "NovaPay Digital Vault",
                                style = MaterialTheme.typography.titleMedium,
                                color = NovaCyan,
                                fontWeight = FontWeight.Bold
                            )
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = NovaEmerald.copy(alpha = 0.2f),
                                contentColor = NovaEmerald
                            ) {
                                Text(
                                    "E2EE SECURE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Column {
                            Text("Total Liquid Balance", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.7f))
                            Text(
                                "$${"%,.2f".format(walletBalance)}",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text("≈ GHS 68,568.00 • NGN 6,856,800 • KES 557,115", fontSize = 11.sp, color = NovaCyan.copy(alpha = 0.9f))
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            NovaPayActionButton(
                                label = "Send MoMo",
                                icon = Icons.Default.Send,
                                color = NovaAmber,
                                onClick = onSendMoneyClick,
                                tag = "pay_action_send"
                            )
                            NovaPayActionButton(
                                label = "QR Scan",
                                icon = Icons.Default.QrCodeScanner,
                                color = NovaCyan,
                                onClick = onQrClick,
                                tag = "pay_action_qr"
                            )
                            NovaPayActionButton(
                                label = "Airtime/Bills",
                                icon = Icons.Default.PhoneAndroid,
                                color = NovaEmerald,
                                onClick = { onShowNotification("Airtime & Utilities Bill Pay gateway active") },
                                tag = "pay_action_bills"
                            )
                            NovaPayActionButton(
                                label = "Split Bill",
                                icon = Icons.Default.GroupWork,
                                color = NovaPurple,
                                onClick = { onShowNotification("Group Bill Split calculation ready") },
                                tag = "pay_action_split"
                            )
                        }
                    }
                }
            }
        }

        // Integrated Payment Gateways & Banks row
        item {
            Text("Linked Gateways & Regional Accounts", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GatewayCard(name = "MTN MoMo", detail = "Active • 0% Fee", iconColor = NovaAmber, modifier = Modifier.weight(1f))
                GatewayCard(name = "Ecobank", detail = "OmniPlus Linked", iconColor = NovaCyan, modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GatewayCard(name = "OPay Africa", detail = "Wallet Connected", iconColor = NovaEmerald, modifier = Modifier.weight(1f))
                GatewayCard(name = "GCB Bank", detail = "Direct Deposit", iconColor = NovaPurple, modifier = Modifier.weight(1f))
            }
        }

        // Transactions Filter Tabs & List
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Transaction Ledger", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    listOf("ALL", "MOMO", "BANK", "OPAY").forEach { f ->
                        FilterChip(
                            selected = selectedFilter == f,
                            onClick = { selectedFilter = f },
                            label = { Text(f, fontSize = 10.sp) },
                            modifier = Modifier.testTag("filter_chip_$f")
                        )
                    }
                }
            }
        }

        items(filteredTransactions) { tx ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(if (tx.isCredit) NovaEmerald.copy(alpha = 0.2f) else NovaCyan.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (tx.isCredit) Icons.Default.CallReceived else Icons.Default.CallMade,
                                contentDescription = null,
                                tint = if (tx.isCredit) NovaEmerald else NovaCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(tx.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                            Text("${tx.timestamp} • ${tx.referenceCode}", style = MaterialTheme.typography.labelSmall, color = NovaTextSecondary)
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${if (tx.isCredit) "+" else "-"}$${"%.2f".format(tx.amount)}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (tx.isCredit) NovaEmerald else MaterialTheme.colorScheme.onSurface
                        )
                        Text(tx.status, style = MaterialTheme.typography.labelSmall, color = NovaEmerald)
                    }
                }
            }
        }
    }
}

@Composable
private fun NovaPayActionButton(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit,
    tag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick).testTag(tag)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = label, tint = color, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.White)
    }
}

@Composable
private fun GatewayCard(name: String, detail: String, iconColor: Color, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(iconColor)
            )
            Column {
                Text(name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text(detail, style = MaterialTheme.typography.labelSmall, color = NovaTextSecondary)
            }
        }
    }
}
