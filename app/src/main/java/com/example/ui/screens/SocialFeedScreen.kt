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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SocialPost
import com.example.model.StatusStory
import com.example.ui.theme.*

@Composable
fun SocialFeedScreen(
    posts: List<SocialPost>,
    stories: List<StatusStory>,
    onToggleLike: (String) -> Unit,
    onStoryClick: (StatusStory) -> Unit,
    onShowNotification: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("social_feed_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Status Stories Bar
        item {
            Text("Moments & Status Updates", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(stories) { story ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .width(68.dp)
                            .clickable { onStoryClick(story) }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(58.dp)
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
                            Text(story.userName.take(2).uppercase(), fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(story.userName, style = MaterialTheme.typography.labelSmall, maxLines = 1)
                    }
                }
            }
        }

        items(posts) { post ->
            Card(
                modifier = Modifier.fillMaxWidth().testTag("social_post_${post.id}"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
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
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(NovaIndigo),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(post.authorName.take(1), color = Color.White, fontWeight = FontWeight.Bold)
                            }
                            Column {
                                Text(post.authorName, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                                Text("${post.authorHandle} • ${post.timestamp}", style = MaterialTheme.typography.labelSmall, color = NovaTextSecondary)
                            }
                        }
                        Icon(Icons.Default.MoreHoriz, contentDescription = "Options", tint = NovaTextSecondary)
                    }

                    Text(post.content, style = MaterialTheme.typography.bodyMedium)

                    // Post interactions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.clickable { onToggleLike(post.id) }
                            ) {
                                Icon(
                                    imageVector = if (post.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Like",
                                    tint = if (post.isLiked) NovaPink else NovaTextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text("${post.likesCount}", fontSize = 12.sp, color = NovaTextSecondary)
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.clickable { onShowNotification("Opening comments for post ${post.id}") }
                            ) {
                                Icon(Icons.Default.Comment, contentDescription = "Comments", tint = NovaTextSecondary, modifier = Modifier.size(20.dp))
                                Text("${post.commentsCount}", fontSize = 12.sp, color = NovaTextSecondary)
                            }

                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share",
                                tint = NovaTextSecondary,
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable { onShowNotification("Post link shared to encrypted clipboard") }
                            )
                        }

                        Icon(Icons.Default.BookmarkBorder, contentDescription = "Save", tint = NovaTextSecondary)
                    }
                }
            }
        }
    }
}
