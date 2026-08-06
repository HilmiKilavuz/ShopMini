package com.example.shopmini.data.model.address

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//Adres verisini karşılayan veri sınıfı
@Serializable
data class AddressDto(
    @SerialName("id")
    val id: String? = null, // Yeni eklerken null göndeririz, Supabase kendi oluşturur
    @SerialName("user_id")
    val userId: String,
    @SerialName("title")
    val title: String,
    @SerialName("full_name")
    val fullName: String,
    @SerialName("phone")
    val phone: String,
    @SerialName("city")
    val city: String,
    @SerialName("district")
    val district: String,
    @SerialName("neighborhood")
    val neighborhood: String,
    @SerialName("full_address")
    val fullAddress: String,
    @SerialName("address_type")
    val addressType: String?,
    @SerialName("is_default")
    val isDefault: Boolean
)