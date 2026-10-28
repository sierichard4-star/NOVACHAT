package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.model.RideProviderOption
import com.example.ui.theme.*

@Composable
fun NovaRideScreen(
    rides: List<RideProviderOption>,
    bookedRide: RideProviderOption?,
    rideStatus: String?,
    onBookRide: (RideProviderOption) -> Unit,
    onCancelRide: () -> Unit
) {
    var pickupLocation by remember { mutableStateOf("Kotoka Intl Airport, Terminal 3") }
    var destinationLocation by remember { mutableStateOf("Accra Financial District / Ridge") }
    var selectedRideId by remember { mutableStateOf(rides.firstOrNull()?.id ?: "") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("nova_ride_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Location Selector Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("ride_route_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "Nova Ride Aggregator",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = NovaCyan
                    )

                    OutlinedTextField(
                        value = pickupLocation,
                        onValueChange = { pickupLocation = it },
                        label = { Text("Pickup Location") },
                        leadingIcon = { Icon(Icons.Default.MyLocation, contentDescription = null, tint = NovaEmerald) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = destinationLocation,
                        onValueChange = { destinationLocation = it },
                        label = { Text("Destination") },
                        leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFFEF4444)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }
        }

        // Active Booking Status (if booked)
        if (bookedRide != null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("active_ride_status_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Live Trip Tracker",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = NovaEmerald
                            )
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = NovaCyan.copy(alpha = 0.2f),
                                contentColor = NovaCyan
                            ) {
                                Text(
                                    "GPS CONNECTED",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        // Simulated Map Visual Box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Brush.linearGradient(listOf(Color(0xFF0F172A), Color(0xFF1E293B)))),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.DirectionsCar, contentDescription = null, tint = NovaCyan, modifier = Modifier.size(36.dp))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    rideStatus ?: "Driver en route",
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 13.sp
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    "${bookedRide.providerName} • ${bookedRide.carType}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text("Fare: $${"%.2f".format(bookedRide.price)} via NovaPay", color = NovaTextSecondary, fontSize = 12.sp)
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = onCancelRide,
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                                    modifier = Modifier.testTag("cancel_ride_btn")
                                ) {
                                    Text("Cancel Trip")
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Provider Comparison List
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("All City Ride Providers Compared", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Fastest & Cheapest", style = MaterialTheme.typography.labelSmall, color = NovaEmerald)
                }
            }

            items(rides) { option ->
                val isSelected = option.id == selectedRideId
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedRideId = option.id }
                        .border(
                            width = if (isSelected) 2.dp else 0.dp,
                            color = if (isSelected) NovaCyan else Color.Transparent,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .testTag("ride_option_${option.id}"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color(option.providerColor)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.DirectionsCar, contentDescription = null, tint = Color.White)
                            }
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(option.providerName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                    Text("• ${option.carType}", style = MaterialTheme.typography.bodySmall, color = NovaTextSecondary)
                                }
                                Text("${option.etaMinutes} mins away • ⭐ ${option.rating}", style = MaterialTheme.typography.labelSmall, color = NovaTextSecondary)
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                "$${"%.2f".format(option.price)}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = NovaCyan
                            )
                            Text("NovaPay Instant", fontSize = 10.sp, color = NovaEmerald)
                        }
                    }
                }
            }

            item {
                val selectedRide = rides.find { it.id == selectedRideId } ?: rides.first()
                Button(
                    onClick = { onBookRide(selectedRide) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("confirm_book_ride_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = NovaCyan, contentColor = Color.Black),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.DirectionsCar, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Book ${selectedRide.providerName} for $${"%.2f".format(selectedRide.price)}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}
