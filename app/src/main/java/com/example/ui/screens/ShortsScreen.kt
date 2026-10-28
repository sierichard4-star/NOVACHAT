package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ProductItem
import com.example.model.ShortVideo
import com.example.ui.theme.*

@Composable
fun ShortsScreen(
    shorts: List<ShortVideo>,
    onToggleLike: (String) -> Unit,
    onAddToCart: (ProductItem) -> Unit,
    onShowNotification: (String) -> Unit
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedFeedTab by remember { mutableIntStateOf(0) } // 0: For You (Algorithmic), 1: Trending
    var isFollowing by remember { mutableStateOf(false) }

    val currentVideo = shorts.getOrElse(currentIndex % shorts.size) { shorts.first() }

    // Vinyl spinning rotation
    val infiniteTransition = rememberInfiniteTransition(label = "vinyl_spin")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing)
        ),
        label = "spin"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("shorts_screen")
            .pointerInput(Unit) {
                detectVerticalDragGestures { _, dragAmount ->
                    if (dragAmount < -40) { // Swipe up
                        if (currentIndex < shorts.size - 1) currentIndex++
                        else currentIndex = 0
                    } else if (dragAmount > 40) { // Swipe down
                        if (currentIndex > 0) currentIndex--
                        else currentIndex = shorts.size - 1
                    }
                }
            }
            .background(
                Brush.verticalGradient(
                    currentVideo.videoGradientColors.map { Color(it) }
                )
            )
    ) {
        // Top Feed Toggle (For You / Trending)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Trending",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = if (selectedFeedTab == 1) FontWeight.Bold else FontWeight.Normal,
                color = if (selectedFeedTab == 1) Color.White else Color.White.copy(alpha = 0.6f),
                modifier = Modifier
                    .clickable { selectedFeedTab = 1 }
                    .padding(8.dp)
                    .testTag("shorts_tab_trending")
            )
            Text(
                text = "|",
                color = Color.White.copy(alpha = 0.4f),
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            Text(
                text = "For You (AI)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = if (selectedFeedTab == 0) FontWeight.Bold else FontWeight.Normal,
                color = if (selectedFeedTab == 0) Color.White else Color.White.copy(alpha = 0.6f),
                modifier = Modifier
                    .clickable { selectedFeedTab = 0 }
                    .padding(8.dp)
                    .testTag("shorts_tab_foryou")
            )
        }

        // Bottom-Left Overlay: Creator Info, Caption, Hashtags, and Sound
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth(0.78f)
                .padding(start = 16.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // In-video E-Commerce shopping tag (WeChat / TikTok Shop feature)
            currentVideo.attachedProduct?.let { product ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.Black.copy(alpha = 0.7f),
                    modifier = Modifier.clickable { onAddToCart(product) }.testTag("shorts_shop_tag")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.ShoppingBag, contentDescription = null, tint = NovaAmber, modifier = Modifier.size(16.dp))
                        Text(
                            "${product.title} • $${product.price}",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                        Icon(Icons.Default.AddShoppingCart, contentDescription = null, tint = NovaCyan, modifier = Modifier.size(14.dp))
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = currentVideo.creatorHandle,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (isFollowing) Color.White.copy(alpha = 0.2f) else NovaPink,
                    modifier = Modifier.clickable { isFollowing = !isFollowing }
                ) {
                    Text(
                        text = if (isFollowing) "Following" else "Follow",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Text(
                text = currentVideo.title,
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 2
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = currentVideo.soundTrack,
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 12.sp,
                    maxLines = 1
                )
            }
        }

        // Right-Side Interaction Column (Like, Comment, Share, Sound Disc)
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Creator Avatar with follow plus badge
            Box(contentAlignment = Alignment.BottomCenter) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(NovaPurple)
                        .border(2.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentVideo.creatorName.take(1),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }

            // Like Button
            ShortActionItem(
                icon = if (currentVideo.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                count = "${currentVideo.likesCount / 1000}k",
                color = if (currentVideo.isLiked) NovaPink else Color.White,
                tag = "shorts_like_btn",
                onClick = { onToggleLike(currentVideo.id) }
            )

            // Comments
            ShortActionItem(
                icon = Icons.Default.Comment,
                count = "${currentVideo.commentsCount}",
                color = Color.White,
                tag = "shorts_comment_btn",
                onClick = { onShowNotification("Viewing ${currentVideo.commentsCount} comments on ${currentVideo.creatorHandle}'s clip") }
            )

            // Share
            ShortActionItem(
                icon = Icons.Default.Share,
                count = "${currentVideo.sharesCount}",
                color = Color.White,
                tag = "shorts_share_btn",
                onClick = { onShowNotification("Video clip link copied with deep link novachat://shorts/${currentVideo.id}") }
            )

            // Spinning Vinyl record
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
                    .border(2.dp, Color.White.copy(alpha = 0.6f), CircleShape)
                    .rotate(rotation),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(NovaCyan)
                )
            }
        }

        // Animated video progress line at very bottom
        LinearProgressIndicator(
            progress = { 0.45f },
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .align(Alignment.BottomCenter),
            color = NovaCyan,
            trackColor = Color.White.copy(alpha = 0.2f),
        )
    }
}

@Composable
private fun ShortActionItem(
    icon: ImageVector,
    count: String,
    color: Color,
    tag: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .testTag(tag)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(34.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = count,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
