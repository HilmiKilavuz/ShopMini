package com.example.shopmini.ui.screens.profile.address

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.shopmini.data.model.address.DistrictDto
import com.example.shopmini.data.model.address.ProvinceDto
import com.example.shopmini.data.remote.TurkiyeApiService
import com.example.shopmini.domain.model.Address
import com.example.shopmini.domain.usecase.address.AddAddressUseCase
import com.example.shopmini.domain.usecase.address.GetAddressesUseCase
import com.example.shopmini.domain.usecase.address.UpdateAddressUseCase
import com.example.shopmini.ui.navigation.Screen
import com.example.shopmini.ui.screens.profile.ProfileUiState
import com.example.shopmini.ui.util.Validators
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Yeni adres ekleme veya mevcut adresi düzenleme işlemlerini (Form) yöneten ViewModel.
 *
 * Eğer [SavedStateHandle] üzerinden bir `addressId` gelirse, bu ViewModel "Düzenleme (Edit)"
 * modunda çalışır. Eğer gelmezse "Yeni Ekleme (Add)" modunda çalışır. Formdaki tüm alanlar
 * (il, ilçe, adres başlığı vs.) ayrı ayrı `mutableStateOf` değişkenlerinde tutulur ve
 * Compose UI tarafında iki yönlü (two-way) bağlanır.
 */
@HiltViewModel
class AddEditAddressViewModel
@Inject constructor(
    private val addAddressUseCase: AddAddressUseCase,
    private val updateAddressUseCase: UpdateAddressUseCase,
    private val turkiyeApiService: TurkiyeApiService,
    private val getAddressesUseCase: GetAddressesUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    // Navigation üzerinden gelen adres ID'sini yakalar.
    val addressId: String? = savedStateHandle.toRoute<Screen.AddEditAddress>().addressId
    
    // Eğer addressId null değilse, sayfanın "Düzenleme Modunda" olduğunu belirtir.
    var isEditing by mutableStateOf(addressId != null)

    // Sayfanın genel yüklenme durumunu kontrol eder (Özellikle eski verileri çekerken)
    private val _uiState = MutableStateFlow<AddEditAddressUiState>(
        if (isEditing) AddEditAddressUiState.Loading else AddEditAddressUiState.Success
    )
    val uiState: StateFlow<AddEditAddressUiState> = _uiState.asStateFlow()


    var provinces by mutableStateOf<List<ProvinceDto>>(emptyList())
    var districts by mutableStateOf<List<DistrictDto>>(emptyList())
    var selectedProvinceId by mutableStateOf<Int?>(null)

    // --- FORM ALANLARI ---
    var title by mutableStateOf("")
    var fullName by mutableStateOf("")
    var phone by mutableStateOf("")
    var city by mutableStateOf("")
    var district by mutableStateOf("")
    var neighborhood by mutableStateOf("")
    var fullAddress by mutableStateOf("")
    var addressType by mutableStateOf("")
    var isDefault by mutableStateOf(false)

    var isProvincesLoading by mutableStateOf(false)
    var isDistrictsLoading by mutableStateOf(false)


    private val _isSaved = MutableSharedFlow<Boolean>()
    val isSaved = _isSaved.asSharedFlow()

    init {
        loadProvinces()
        // Sayfa açıldığında eğer düzenleme modundaysak, eski verileri getirip formu doldurur.
        if (isEditing && addressId != null) {
            loadAddressForEdit(addressId)
        }
    }

    /**
     * Kullanıcının formda doldurduğu verileri bir [Address] nesnesine dönüştürür.
     * 
     * Eğer [isEditing] true ise, var olan kaydı [UpdateAddressUseCase] ile günceller.
     * Eğer false ise, [AddAddressUseCase] ile yepyeni bir adres olarak veritabanına kaydeder.
     * İşlem bitince UI katmanına (Screen'e) sayfayı kapatması için [isSaved] üzerinden sinyal gönderir.
     */
    fun saveAddress() {

        viewModelScope.launch {
            try {

                val newAddress = Address(
                    id = addressId ?:"",
                    title = title,
                    fullName = fullName,
                    phone = phone,
                    city = city,
                    district = district,
                    neighborhood = neighborhood,
                    fullAddress = fullAddress,
                    addressType = if (addressType.isBlank()) null else addressType,
                    isDefault = isDefault
                )

                if (isEditing) {
                    updateAddressUseCase(newAddress)
                } else {
                    addAddressUseCase(newAddress)
                }

                _isSaved.emit(true)

            } catch (e: Exception) {
                Log.e("AddressDebug", "HATA OLUŞTU: ${e.message}", e)
            }
        }
    }

    private fun loadProvinces() {
        viewModelScope.launch {
            provinces = turkiyeApiService.getProvinces().data
        }
    }

    fun onCitySelected(province: ProvinceDto) {
        city = province.name
        selectedProvinceId = province.id
        district = ""
        viewModelScope.launch {
            districts = turkiyeApiService.getDistricts(province.id).data
        }
    }

    /**
     * Tüm form alanlarının (il, ilçe, regex ile uyumlu telefon/ad) dolu ve geçerli olup
     * olmadığını kontrol eder. Bu fonksiyon "Kaydet" butonunun tıklanabilirliğini
     * (enabled state) belirler.
     */
    fun isFormValid(): Boolean {
        return title.isNotBlank() &&
                Validators.isValidFullName(fullName) &&
                Validators.isValidPhone(phone) &&
                city.isNotBlank() &&
                district.isNotBlank() &&
                neighborhood.isNotBlank() &&
                fullAddress.isNotBlank()
    }
    
    /**
     * "Düzenle" butonuna tıklandığında çalışır.
     * Veritabanındaki tüm adresleri çekerek içinden tıklanılan ID'yi bulur, 
     * ve ViewModel içindeki form değişkenlerine doldurur.
     * Böylece kullanıcı sayfaya girdiğinde boşluklar dolu halde karşısına gelir.
     */
    private fun loadAddressForEdit(id: String) {
        viewModelScope.launch {
            _uiState.value = AddEditAddressUiState.Loading
            try {
                getAddressesUseCase().collect { addresses ->
                    val addressToEdit = addresses.find { it.id == id }
                    if (addressToEdit != null) {
                        title = addressToEdit.title
                        fullName = addressToEdit.fullName
                        phone = addressToEdit.phone
                        city = addressToEdit.city
                        district = addressToEdit.district
                        neighborhood = addressToEdit.neighborhood
                        fullAddress = addressToEdit.fullAddress
                        addressType = addressToEdit.addressType ?: ""
                        isDefault = addressToEdit.isDefault
                        
                        // Veriler başarıyla dolduktan sonra formu göster
                        _uiState.value = AddEditAddressUiState.Success
                    } else {
                        _uiState.value = AddEditAddressUiState.Error("Adres bulunamadı")
                    }
                }
            } catch (e: Exception) {
                _uiState.value = AddEditAddressUiState.Error(e.message ?: "Bilinmeyen bir hata oluştu")
            }
        }
    }
}

// Sealed class'ı dosyanın en altına (class'ın dışına) ekliyoruz
