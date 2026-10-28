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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ProductItem
import com.example.ui.theme.*
import com.example.viewmodel.CartItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketplaceScreen(
    products: List<ProductItem>,
    cart: List<CartItem>,
    walletBalance: Double,
    onAddToCart: (ProductItem) -> Unit,
    onCheckout: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf("ALL") }
    var searchQuery by remember { mutableStateOf("") }
    var showCartSheet by remember { mutableStateOf(false) }

    val categories = listOf("ALL", "Audio", "Wearables", "Power", "Office", "Fashion")

    val filteredProducts = remember(selectedCategory, searchQuery, products) {
        products.filter { p ->
            (selectedCategory == "ALL" || p.category.equals(selectedCategory, ignoreCase = true)) &&
            (searchQuery.isBlank() || p.title.contains(searchQuery, ignoreCase = true))
        }
    }

    val cartTotal = cart.sumOf { it.product.price * it.quantity }

    Scaffold(
        floatingActionButton = {
            if (cart.isNotEmpty()) {
                ExtendedFloatingActionButton(
                    onClick = { showCartSheet = true },
                    containerColor = NovaCyan,
                    contentColor = Color.Black,
                    modifier = Modifier.testTag("floating_cart_fab")
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "${cart.sumOf { it.quantity }} Items • $${"%.2f".format(cartTotal)}",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .testTag("marketplace_screen")
        ) {
            // Search Input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search products, electronics, brands...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = NovaTextSecondary) },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("market_search_input"),
                singleLine = true
            )

            // Category Chips Row
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat, fontSize = 12.sp) },
                        modifier = Modifier.testTag("cat_chip_$cat")
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Products Grid / List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(filteredProducts) { item ->
                    ProductCard(item = item, onAddToCart = { onAddToCart(item) })
                }
            }
        }
    }

    if (showCartSheet) {
        ModalBottomSheet(onDismissRequest = { showCartSheet = false }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("NovaPay Marketplace Cart", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("${cart.sumOf { it.quantity }} items", color = NovaTextSecondary)
                }

                cart.forEach { cItem ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(cItem.product.title, fontWeight = FontWeight.SemiBold)
                            Text("Qty: ${cItem.quantity} × $${cItem.product.price}", style = MaterialTheme.typography.bodySmall, color = NovaTextSecondary)
                        }
                        Text("$${"%.2f".format(cItem.product.price * cItem.quantity)}", fontWeight = FontWeight.Bold)
                    }
                }

                HorizontalDivider()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total Order Amount:", style = MaterialTheme.typography.titleMedium)
                    Text("$${"%.2f".format(cartTotal)}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = NovaCyan)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("NovaPay Balance Available:", style = MaterialTheme.typography.bodySmall)
                    Text("$${"%.2f".format(walletBalance)}", style = MaterialTheme.typography.bodySmall, color = NovaEmerald)
                }

                Button(
                    onClick = {
                        onCheckout()
                        showCartSheet = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("checkout_cart_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = NovaCyan, contentColor = Color.Black)
                ) {
                    Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Pay with NovaPay (Instant)", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun ProductCard(
    item: ProductItem,
    onAddToCart: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().testTag("product_card_${item.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = NovaIndigo.copy(alpha = 0.2f),
                    contentColor = NovaIndigo
                ) {
                    Text(
                        item.category.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = NovaAmber, modifier = Modifier.size(16.dp))
                    Text("${item.rating} (${item.reviewsCount})", fontSize = 12.sp, color = NovaTextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(item.description, style = MaterialTheme.typography.bodySmall, color = NovaTextSecondary, maxLines = 2)

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("$${item.price}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = NovaCyan)
                        Text("$${item.originalPrice}", style = MaterialTheme.typography.bodySmall, textDecoration = TextDecoration.LineThrough, color = NovaTextMuted)
                    }
                    Text("Sold by ${item.merchantName}", fontSize = 11.sp, color = NovaTextSecondary)
                }

                Button(
                    onClick = onAddToCart,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NovaCyan, contentColor = Color.Black),
                    modifier = Modifier.testTag("add_to_cart_btn_${item.id}")
                ) {
                    Icon(Icons.Default.AddShoppingCart, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Add to Cart", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
