package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.PaymentProviderType
import com.example.viewmodel.AppTab
import com.example.viewmodel.SuperAppViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("NovaChat", appName)
  }

  @Test
  fun `super app viewmodel wallet transfer test`() {
    val viewModel = SuperAppViewModel()
    val initialBalance = viewModel.uiState.value.walletBalance

    viewModel.sendMoney(
      recipientName = "Sarah K.",
      amount = 50.0,
      provider = PaymentProviderType.MOMO,
      note = "Lunch"
    )

    assertEquals(initialBalance - 50.0, viewModel.uiState.value.walletBalance, 0.001)
    assertTrue(viewModel.uiState.value.transactions.any { it.amount == 50.0 })
  }

  @Test
  fun `super app tab navigation and deep link test`() {
    val viewModel = SuperAppViewModel()
    viewModel.switchTab(AppTab.SHORTS)
    assertEquals(AppTab.SHORTS, viewModel.uiState.value.currentTab)

    viewModel.handleDeepLink("novachat://ride?to=Airport")
    assertEquals(AppTab.RIDE, viewModel.uiState.value.currentTab)

    viewModel.handleDeepLink("novachat://pay?amount=25")
    assertEquals(AppTab.NOVAPAY, viewModel.uiState.value.currentTab)

    viewModel.handleDeepLink("novachat://shop")
    assertEquals(AppTab.MARKETPLACE, viewModel.uiState.value.currentTab)
  }

  @Test
  fun `marketplace add to cart and checkout test`() {
    val viewModel = SuperAppViewModel()
    val initialBalance = viewModel.uiState.value.walletBalance
    val product = viewModel.uiState.value.products.first()

    viewModel.addToCart(product)
    assertEquals(1, viewModel.uiState.value.cart.size)
    assertEquals(product.id, viewModel.uiState.value.cart.first().product.id)

    viewModel.checkoutCart()
    assertTrue(viewModel.uiState.value.cart.isEmpty())
    assertEquals(initialBalance - product.price, viewModel.uiState.value.walletBalance, 0.001)
  }

  @Test
  fun `ride booking lifecycle test`() {
    val viewModel = SuperAppViewModel()
    val ride = viewModel.uiState.value.rides.first()

    viewModel.bookRide(ride)
    assertNotNull(viewModel.uiState.value.bookedRide)
    assertEquals(ride.id, viewModel.uiState.value.bookedRide?.id)

    viewModel.cancelRide()
    assertNull(viewModel.uiState.value.bookedRide)
  }

  @Test
  fun `document signing with cryptographic sha256 hash test`() {
    val viewModel = SuperAppViewModel()
    val initialDocCount = viewModel.uiState.value.signedDocuments.size

    viewModel.signDocument("Vendor_Contract_2026.pdf", "Alex Mercer")
    assertEquals(initialDocCount + 1, viewModel.uiState.value.signedDocuments.size)
    val latestDoc = viewModel.uiState.value.signedDocuments.first()
    assertTrue(latestDoc.hashSha256.startsWith("sha256_"))
    assertEquals("Vendor_Contract_2026.pdf", latestDoc.title)
  }

  @Test
  fun `chat messaging and voice note test`() {
    val viewModel = SuperAppViewModel()
    val chatId = "chat_1"
    val initialMsgCount = viewModel.uiState.value.chats.find { it.id == chatId }?.messages?.size ?: 0

    viewModel.sendMessage(chatId, "Hello from automated test")
    val updatedChat = viewModel.uiState.value.chats.find { it.id == chatId }
    assertEquals(initialMsgCount + 1, updatedChat?.messages?.size)
    assertEquals("Hello from automated test", updatedChat?.lastMessage)

    viewModel.sendVoiceMessage(chatId, 15)
    val chatAfterVoice = viewModel.uiState.value.chats.find { it.id == chatId }
    assertEquals(initialMsgCount + 2, chatAfterVoice?.messages?.size)
    assertTrue(chatAfterVoice?.lastMessage?.contains("Voice message") == true)
  }

  @Test
  fun `offline caching toggle and cache optimization test`() {
    val viewModel = SuperAppViewModel()
    assertFalse(viewModel.uiState.value.isOfflineMode)

    viewModel.toggleOfflineMode()
    assertTrue(viewModel.uiState.value.isOfflineMode)

    viewModel.clearCache()
    assertEquals(1_200_000L, viewModel.uiState.value.cachedDataBytes)
  }
}
