package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
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
import com.example.model.ChatMessage
import com.example.model.ChatThread
import com.example.model.MessageType
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreen(
    chat: ChatThread,
    onBack: () -> Unit,
    onSendMessage: (String) -> Unit,
    onSendVoiceMessage: () -> Unit,
    onQuickMoMo: () -> Unit,
    onStartCall: (isVideo: Boolean) -> Unit
) {
    BackHandler { onBack() }

    var inputText by remember { mutableStateOf("") }
    var isVoicePlaying by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("chat_back_btn")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(NovaIndigo),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(chat.peerName.take(1), color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Column {
                            Text(chat.peerName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = NovaEmerald, modifier = Modifier.size(11.dp))
                                Text("End-to-End Encrypted", style = MaterialTheme.typography.labelSmall, color = NovaEmerald)
                            }
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { onStartCall(false) }, modifier = Modifier.testTag("chat_voice_call_btn")) {
                        Icon(Icons.Default.Call, contentDescription = "Voice Call", tint = NovaCyan)
                    }
                    IconButton(onClick = { onStartCall(true) }, modifier = Modifier.testTag("chat_video_call_btn")) {
                        Icon(Icons.Default.Videocam, contentDescription = "Video Call", tint = NovaCyan)
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    IconButton(
                        onClick = onQuickMoMo,
                        modifier = Modifier.testTag("quick_momo_in_chat_btn")
                    ) {
                        Icon(Icons.Default.Payments, contentDescription = "Send MoMo", tint = NovaAmber)
                    }

                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = { Text("Encrypted message...", fontSize = 14.sp) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chat_message_input"),
                        shape = RoundedCornerShape(20.dp),
                        singleLine = true
                    )

                    if (inputText.isBlank()) {
                        IconButton(
                            onClick = onSendVoiceMessage,
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(NovaCyan.copy(alpha = 0.2f))
                                .testTag("record_voice_btn")
                        ) {
                            Icon(Icons.Default.Mic, contentDescription = "Voice Message", tint = NovaCyan)
                        }
                    } else {
                        IconButton(
                            onClick = {
                                onSendMessage(inputText)
                                inputText = ""
                            },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(NovaCyan)
                                .testTag("send_message_btn")
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = Color.Black)
                        }
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = NovaEmerald, modifier = Modifier.size(14.dp))
                            Text(
                                "Messages are end-to-end encrypted with Signal Protocol.",
                                fontSize = 11.sp,
                                color = NovaTextSecondary
                            )
                        }
                    }
                }
            }

            items(chat.messages) { msg ->
                ChatBubble(
                    message = msg,
                    isVoicePlaying = isVoicePlaying,
                    onToggleVoice = { isVoicePlaying = !isVoicePlaying }
                )
            }
        }
    }
}

@Composable
private fun ChatBubble(
    message: ChatMessage,
    isVoicePlaying: Boolean,
    onToggleVoice: () -> Unit
) {
    val isMe = message.isFromMe
    val bubbleColor = if (isMe) NovaCyanDark else MaterialTheme.colorScheme.surfaceVariant
    val textColor = if (isMe) Color.White else MaterialTheme.colorScheme.onSurface

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isMe) 16.dp else 4.dp,
                bottomEnd = if (isMe) 4.dp else 16.dp
            ),
            color = bubbleColor,
            tonalElevation = 2.dp,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                when (message.type) {
                    MessageType.TEXT -> {
                        Text(text = message.text, color = textColor, fontSize = 15.sp)
                    }

                    MessageType.VOICE -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            IconButton(
                                onClick = onToggleVoice,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.2f))
                            ) {
                                Icon(
                                    imageVector = if (isVoicePlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = "Play/Pause",
                                    tint = textColor
                                )
                            }
                            Column {
                                Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                                    val barHeights = listOf(14, 22, 10, 26, 18, 28, 12, 20, 24, 16)
                                    barHeights.forEach { h ->
                                        Box(
                                            modifier = Modifier
                                                .width(3.dp)
                                                .height(h.dp)
                                                .clip(RoundedCornerShape(2.dp))
                                                .background(if (isVoicePlaying) NovaCyan else textColor.copy(alpha = 0.5f))
                                        )
                                    }
                                }
                                Text("0:${message.voiceDurationSeconds.toString().padStart(2, '0')}", fontSize = 11.sp, color = textColor.copy(alpha = 0.7f))
                            }
                        }
                    }

                    MessageType.PAYMENT -> {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = NovaEmerald, modifier = Modifier.size(18.dp))
                                Text(message.paymentProvider ?: "MoMo Transfer", fontWeight = FontWeight.Bold, color = NovaEmerald)
                            }
                            Text(
                                text = "$${"%.2f".format(message.paymentAmount ?: 0.0)}",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )
                            Text("NovaPay Instant Settlement ✓", fontSize = 11.sp, color = textColor.copy(alpha = 0.7f))
                        }
                    }

                    MessageType.DOCUMENT -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Description, contentDescription = null, tint = NovaAmber, modifier = Modifier.size(32.dp))
                            Column {
                                Text(message.documentName ?: "Document.pdf", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = textColor)
                                Text("SHA-256 Digitally Signed", fontSize = 11.sp, color = NovaEmerald)
                            }
                        }
                    }

                    else -> {
                        Text(message.text, color = textColor)
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.align(Alignment.End),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(message.timestamp, fontSize = 10.sp, color = textColor.copy(alpha = 0.6f))
                    if (isMe) {
                        Icon(
                            imageVector = Icons.Default.DoneAll,
                            contentDescription = "Read",
                            tint = NovaCyan,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }
        }
    }
}
