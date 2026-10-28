package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.PaymentProviderType
import com.example.ui.theme.*
import com.example.viewmodel.ActiveCallState

@Composable
fun SendMoneyDialog(
    currentBalance: Double,
    onDismiss: () -> Unit,
    onSend: (recipient: String, amount: Double, provider: PaymentProviderType, note: String) -> Unit
) {
    var recipient by remember { mutableStateOf("Sarah K. (+233 24 555 0192)") }
    var amountText by remember { mutableStateOf("50.00") }
    var note by remember { mutableStateOf("Lunch split & coffee ☕") }
    var selectedProvider by remember { mutableStateOf(PaymentProviderType.MOMO) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = null,
                    tint = NovaCyan
                )
                Text("NovaPay Instant Transfer", style = MaterialTheme.typography.titleLarge)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Available Balance: $${"%.2f".format(currentBalance)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = NovaEmerald
                )

                Text("Select Provider:", style = MaterialTheme.typography.labelMedium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val providers = listOf(
                        PaymentProviderType.MOMO to "MoMo",
                        PaymentProviderType.ECOBANK to "Ecobank",
                        PaymentProviderType.OPAY to "OPay",
                        PaymentProviderType.GCB to "GCB Bank"
                    )
                    providers.forEach { (prov, label) ->
                        FilterChip(
                            selected = selectedProvider == prov,
                            onClick = { selectedProvider = prov },
                            label = { Text(label, fontSize = 11.sp) },
                            modifier = Modifier.testTag("provider_chip_${prov.name}")
                        )
                    }
                }

                OutlinedTextField(
                    value = recipient,
                    onValueChange = { recipient = it },
                    label = { Text("Recipient Phone / Handle") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("send_money_recipient"),
                    singleLine = true
                )

                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("Amount ($)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("send_money_amount"),
                    singleLine = true
                )

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Reference Note") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("send_money_note"),
                    singleLine = true
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Fingerprint,
                        contentDescription = null,
                        tint = NovaCyan,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        "Biometric authorization active (Zero Fee)",
                        style = MaterialTheme.typography.labelSmall,
                        color = NovaTextSecondary
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amt = amountText.toDoubleOrNull() ?: 0.0
                    onSend(recipient, amt, selectedProvider, note)
                },
                colors = ButtonDefaults.buttonColors(containerColor = NovaCyan, contentColor = Color.Black),
                modifier = Modifier.testTag("confirm_send_money_btn")
            ) {
                Text("Send with Biometrics", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun QrModal(
    userHandle: String,
    onDismiss: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: My QR, 1: Scan

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.Transparent
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("My Nova QR") },
                        modifier = Modifier.testTag("tab_my_qr")
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Scan QR Code") },
                        modifier = Modifier.testTag("tab_scan_qr")
                    )
                }

                if (selectedTab == 0) {
                    Text(
                        "Show to receive MoMo, payments, or add contacts",
                        style = MaterialTheme.typography.bodySmall,
                        color = NovaTextSecondary,
                        textAlign = TextAlign.Center
                    )
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Simulated QR code matrix
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Box(modifier = Modifier.size(40.dp).background(Color.Black))
                                Box(modifier = Modifier.size(40.dp).background(Color.Black))
                            }
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(NovaIndigo)
                                    .align(Alignment.CenterHorizontally),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("N", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Box(modifier = Modifier.size(40.dp).background(Color.Black))
                                Box(modifier = Modifier.size(24.dp).background(Color.Black))
                            }
                        }
                    }
                    Text(
                        text = userHandle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = NovaCyan
                    )
                } else {
                    Text(
                        "Point camera at any QR code (MoMo, Ecobank, OPay, Web)",
                        style = MaterialTheme.typography.bodySmall,
                        color = NovaTextSecondary,
                        textAlign = TextAlign.Center
                    )
                    Box(
                        modifier = Modifier
                            .size(220.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.Black)
                            .border(2.dp, NovaCyan, RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        val infiniteTransition = rememberInfiniteTransition(label = "scan_laser")
                        val laserOffset by infiniteTransition.animateFloat(
                            initialValue = 0.1f,
                            targetValue = 0.9f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(1500, easing = LinearEasing),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "laser"
                        )
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val y = size.height * laserOffset
                            drawLine(
                                color = NovaCyan,
                                start = Offset(20f, y),
                                end = Offset(size.width - 20f, y),
                                strokeWidth = 6f
                            )
                        }
                        Text("Searching for QR Code...", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                    }
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().testTag("close_qr_modal_btn")
                ) {
                    Text("Close")
                }
            }
        }
    }
}

@Composable
fun VideoCallDialog(
    callState: ActiveCallState,
    onEndCall: () -> Unit,
    onToggleMute: () -> Unit,
    onToggleCamera: () -> Unit
) {
    Dialog(
        onDismissRequest = onEndCall,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color(0xFF070B14), Color(0xFF131C31))))
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(top = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(NovaCyan, NovaIndigo))),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = callState.peerName.take(1),
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Text(
                    text = callState.peerName,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = NovaEmerald,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = if (callState.isConnected) "Encrypted • 00:42 HD Video" else "Connecting peer-to-peer...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (callState.isConnected) NovaEmerald else NovaCyan
                    )
                }
            }

            // Call actions bar at bottom
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 36.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onToggleMute,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(if (callState.isMuted) Color.Red else Color.White.copy(alpha = 0.2f))
                        .testTag("call_mute_btn")
                ) {
                    Icon(
                        imageVector = if (callState.isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                        contentDescription = "Mute",
                        tint = Color.White
                    )
                }

                IconButton(
                    onClick = onEndCall,
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEF4444))
                        .testTag("call_end_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.CallEnd,
                        contentDescription = "End Call",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                IconButton(
                    onClick = onToggleCamera,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(if (!callState.isCameraOn) Color.Red else Color.White.copy(alpha = 0.2f))
                        .testTag("call_camera_btn")
                ) {
                    Icon(
                        imageVector = if (callState.isCameraOn) Icons.Default.Videocam else Icons.Default.VideocamOff,
                        contentDescription = "Camera Toggle",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun DocumentSigningDialog(
    signerName: String,
    onDismiss: () -> Unit,
    onSign: (title: String) -> Unit
) {
    var docTitle by remember { mutableStateOf("Consulting_Services_Agreement_2026.pdf") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(imageVector = Icons.Default.Draw, contentDescription = null, tint = NovaCyan)
                Text("X Secure Document Signing", style = MaterialTheme.typography.titleLarge)
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "Sign electronic agreements backed by SHA-256 cryptographic verification and facial recognition audit trail.",
                    style = MaterialTheme.typography.bodySmall,
                    color = NovaTextSecondary
                )
                OutlinedTextField(
                    value = docTitle,
                    onValueChange = { docTitle = it },
                    label = { Text("Document File Name") },
                    modifier = Modifier.fillMaxWidth().testTag("doc_title_input")
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(1.dp, NovaCyan.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✍️  $signerName\n[Cryptographically Verified by Nova Super Key]",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        color = NovaCyan
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSign(docTitle) },
                colors = ButtonDefaults.buttonColors(containerColor = NovaCyan, contentColor = Color.Black),
                modifier = Modifier.testTag("sign_doc_confirm_btn")
            ) {
                Text("Sign & Encrypt with SHA-256", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
