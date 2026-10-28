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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SignedDocument
import com.example.model.UserProfile
import com.example.ui.theme.*

@Composable
fun SecurityScreen(
    userProfile: UserProfile,
    isBiometricScanning: Boolean,
    signedDocuments: List<SignedDocument>,
    cachedBytes: Long,
    isOffline: Boolean,
    onAuthenticateBiometrics: () -> Unit,
    onOpenDocSign: () -> Unit,
    onToggleOffline: () -> Unit,
    onClearCache: () -> Unit,
    onTestDeepLink: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("security_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Biometric Security Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("biometrics_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(NovaEmerald.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Fingerprint, contentDescription = null, tint = NovaEmerald, modifier = Modifier.size(24.dp))
                            }
                            Column {
                                Text("Facial Recognition & Biometrics", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text("Multi-Factor Verification Active", style = MaterialTheme.typography.bodySmall, color = NovaEmerald)
                            }
                        }
                    }

                    Text(
                        "Hardware-backed enclave with zero-trust tokenization for all NovaPay transactions, document signing, and app entry.",
                        style = MaterialTheme.typography.bodySmall,
                        color = NovaTextSecondary
                    )

                    Button(
                        onClick = onAuthenticateBiometrics,
                        enabled = !isBiometricScanning,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("verify_biometrics_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = NovaCyan, contentColor = Color.Black)
                    ) {
                        if (isBiometricScanning) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.Black)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Scanning Face & Secure Enclave...")
                        } else {
                            Icon(Icons.Default.Face, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Simulate Facial ID Scan", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Offline Caching & Performance Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("offline_cache_card"),
                shape = RoundedCornerShape(20.dp),
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(NovaAmber.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.CloudSync, contentDescription = null, tint = NovaAmber, modifier = Modifier.size(24.dp))
                            }
                            Column {
                                Text("Offline Caches & Synced Storage", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text("High performance in low-connectivity areas", style = MaterialTheme.typography.bodySmall, color = NovaTextSecondary)
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Current Encrypted Cache:", style = MaterialTheme.typography.bodyMedium)
                        Text(
                            "${"%.1f".format(cachedBytes / (1024.0 * 1024.0))} MB",
                            fontWeight = FontWeight.Bold,
                            color = NovaCyan
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = onToggleOffline,
                            modifier = Modifier.weight(1f).testTag("offline_toggle_btn")
                        ) {
                            Text(if (isOffline) "Go Online" else "Go Offline")
                        }
                        Button(
                            onClick = onClearCache,
                            modifier = Modifier.weight(1f).testTag("clear_cache_btn"),
                            colors = ButtonDefaults.buttonColors(containerColor = NovaDarkSurfaceVariant)
                        ) {
                            Text("Optimize Cache")
                        }
                    }
                }
            }
        }

        // X Features: Document Signing
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("X Cryptographic Signed Documents", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                IconButton(onClick = onOpenDocSign, modifier = Modifier.testTag("new_doc_sign_btn")) {
                    Icon(Icons.Default.AddCircle, contentDescription = "Sign New Doc", tint = NovaCyan)
                }
            }
        }

        items(signedDocuments) { doc ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = NovaEmerald, modifier = Modifier.size(32.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(doc.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Text("Hash: ${doc.hashSha256.take(24)}...", style = MaterialTheme.typography.labelSmall, color = NovaCyan)
                        Text("Signed by ${doc.signerName} • ${doc.signedAt}", fontSize = 11.sp, color = NovaTextSecondary)
                    }
                }
            }
        }

        // Deep Link Navigation Testing
        item {
            Text("Deep Link Navigation Routing", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SuggestionChip(
                    onClick = { onTestDeepLink("novachat://pay?amount=100&to=Sarah") },
                    label = { Text("novachat://pay", fontSize = 11.sp) }
                )
                SuggestionChip(
                    onClick = { onTestDeepLink("novachat://ride?to=Airport") },
                    label = { Text("novachat://ride", fontSize = 11.sp) }
                )
                SuggestionChip(
                    onClick = { onTestDeepLink("novachat://shorts/trending") },
                    label = { Text("novachat://shorts", fontSize = 11.sp) }
                )
            }
        }
    }
}
