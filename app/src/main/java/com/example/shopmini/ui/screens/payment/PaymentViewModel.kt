package com.example.shopmini.ui.screens.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopmini.domain.usecase.cart.ClearCartUseCase
import com.example.shopmini.domain.usecase.cart.GetCartItemsUseCase
import com.example.shopmini.domain.usecase.order.SaveOrderUseCase
import com.example.shopmini.ui.util.AnalyticsManager
import com.example.shopmini.ui.util.Validators
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val getCartItemsUseCase: GetCartItemsUseCase, // Sepeti okumak için
    private val saveOrderUseCase: SaveOrderUseCase,       // Siparişi kaydetmek için
    private val clearCartUseCase: ClearCartUseCase, // Sepeti temizlemek için
    private val analyticsManager: AnalyticsManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(PaymentUiState())
    val uiState: StateFlow<PaymentUiState> = _uiState.asStateFlow()

    fun onCardNumberChange(cardNumber: String) {
        if (cardNumber.length > 16) return
        _uiState.value = _uiState.value.copy(cardNumber = cardNumber)

    }

    fun onCardHolderNameChange(holderName: String) {
        _uiState.value = _uiState.value.copy(cardHolderName = holderName)

    }

    fun onExpiryDateChange(expiryDate: String) {
        if (expiryDate.length > 5) return
            _uiState.value = _uiState.value.copy(expiryDate = expiryDate)


    }

    fun onCvvChange(cvv: String) {
        if (cvv.length > 3) return
        _uiState.value = _uiState.value.copy(cvv = cvv)

    }

    fun onPayClicked() {
        val cardNumberError = validateCardNumber(_uiState.value.cardNumber)
        val nameError = validateCardHolderName(_uiState.value.cardHolderName)
        val expiryDateError = validateExpiryDate(_uiState.value.expiryDate)
        val cvvError = validateCvv(_uiState.value.cvv)
        _uiState.update {
            it.copy(
                cardNumberError = cardNumberError,
                cardHolderNameError = nameError,
                expiryDateError = expiryDateError,
                cvvError = cvvError
            )
        }

        if (listOf(cardNumberError, nameError, expiryDateError, cvvError).any { it != null }) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(2000L)
            val cartItems = getCartItemsUseCase().first()
            val subtotal = cartItems.sumOf { it.price * it.quantity }
            val discountTotal =
                cartItems.sumOf { it.price * it.quantity * (it.discountPercentage / 100) }
            val totalAmount = subtotal - discountTotal
            saveOrderUseCase(cartItems, totalAmount)
            analyticsManager.logPurchase(totalAmount, "")
            clearCartUseCase()

            _uiState.update { it.copy(isLoading = false, isPaymentSuccessful = true) }
        }


    }


    private fun luhnCheck(cardNumber: String): Boolean {
        val digits = cardNumber.filter { it.isDigit() }
        if (digits.length < 13 || digits.length > 19) return false
        var sum = 0
        var isEven = false  // sağdan başlıyoruz
        for (i in digits.indices.reversed()) {
            var digit = digits[i].digitToInt()
            if (isEven) {
                digit *= 2
                if (digit > 9) digit -= 9
            }
            sum += digit
            isEven = !isEven
        }
        return sum % 10 == 0

    }

    private fun validateCardNumber(number: String): String? {
        val cleaned = number.filter { it.isDigit() }
        return when {
            cleaned.isEmpty() -> "Kart numarası boş olamaz"
            cleaned.length != 16 -> "Kart numarası 16 haneli olmalıdır"
            !luhnCheck(cleaned) -> "Kart numarası geçersiz"
            else -> null
        }
    }

    private fun validateCardHolderName(name: String): String? {
        return when {
            name.isEmpty() -> "Kart sahibinin adı boş olamaz"
            !Validators.isValidFullName(name) -> "Kart sahibinin adı geçersiz"
            else -> null
        }

    }


    private fun validateExpiryDate(date: String): String? {

        if (!Validators.isValidValidateExpiry(date)) return "Geçersiz format (AA/YY)"
        val parts = date.split("/")
        val month = parts[0].toInt()
        val year = parts[1].toInt() + 2000
        val now = java.util.Calendar.getInstance()
        val currentYear = now.get(java.util.Calendar.YEAR)
        val currentMonth = now.get(java.util.Calendar.MONTH) + 1
        return when {
            year < currentYear -> "Kartın süresi dolmuş"
            year == currentYear && month < currentMonth -> "Kartın süresi dolmuş"
            else -> null
        }


    }

    private fun validateCvv(cvv: String): String? {
        return when {
            cvv.length != 3 -> "CVV 3 haneli olmalıdır"
            !cvv.all { it.isDigit() } -> "CVV sadece rakam içermelidir"
            else -> null
        }


    }

}