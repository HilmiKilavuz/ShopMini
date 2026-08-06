package com.example.shopmini.ui.screens.profile.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopmini.domain.usecase.address.DeleteAddressUseCase
import com.example.shopmini.domain.usecase.address.GetAddressesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Kullanıcının kayıtlı adreslerini listeleyen ve silme işlemlerini yöneten ViewModel.
 *
 * Bu ViewModel, adreslerin veritabanından getirilmesi (GetAddressesUseCase) ve
 * silinmesi (DeleteAddressUseCase) işlemlerinden sorumludur. UI katmanı (AddressesScreen)
 * bu ViewModel'in sağladığı `uiState` (StateFlow) üzerinden güncellemeleri dinler.
 */
@HiltViewModel
class AddressesViewModel @Inject constructor(
    private val getAddressesUseCase: GetAddressesUseCase,
    private val deleteAddressUseCase: DeleteAddressUseCase

) : ViewModel() {
    
    // Ekranın anlık durumunu (Yükleniyor, Başarılı, Hata) tutan StateFlow
    private val _uiState = MutableStateFlow<AddressesUiState>(AddressesUiState.Loading)
    val uiState: StateFlow<AddressesUiState> = _uiState.asStateFlow()

    init {
        // ViewModel ilk oluşturulduğunda adresleri yükle
        loadAddresses()
    }

    /**
     * Kullanıcıya ait olan tüm adresleri veritabanından çeker.
     * 
     * İşlem başladığında UiState'i [AddressesUiState.Loading] yapar.
     * Veriler başarıyla gelirse [AddressesUiState.Success] ile adresleri listeler.
     * Hata oluşursa [AddressesUiState.Error] olarak günceller.
     * 
     * (Not: Sayfaya geri dönüldüğünde listenin güncellenmesi için public yapılmıştır ve
     * AddressesScreen içerisindeki LaunchedEffect tarafından da tetiklenir.)
     */
    fun loadAddresses() {
        viewModelScope.launch {
            _uiState.value = AddressesUiState.Loading
            getAddressesUseCase()
                .catch { e ->
                    _uiState.value =
                        AddressesUiState.Error(e.message ?: "Bilinmeyen bir hata oluştu")
                }
                .collect { addressList ->
                    _uiState.value = AddressesUiState.Success(addressList)
                }
        }
    }

    /**
     * Verilen ID'ye sahip adresi veritabanından (Supabase) siler.
     * 
     * Silme işlemi başarıyla tamamlandıktan sonra, listenin arayüzde (UI) de
     * güncellenmesi için otomatik olarak [loadAddresses] metodunu çağırır.
     *
     * @param addressId Silinmek istenen adresin benzersiz kimliği (ID)
     */
    fun deleteAddress(addressId: String) {
        viewModelScope.launch {
            try {
                deleteAddressUseCase(addressId)
                loadAddresses()

            } catch (e: Exception) {
                _uiState.value = AddressesUiState.Error(e.message ?: "Bilinmeyen bir hata oluştu")
            }

        }
    }


}