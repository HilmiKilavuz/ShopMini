package com.example.shopmini.domain.model

data class Address (
    val id: String = "",
    val title: String,
    val fullName: String,
    val phone: String,
    val city: String,
    val district: String,
    val neighborhood: String,
    val fullAddress: String,
    val addressType: String?, // Zorunlu değildi, bu yüzden nullable (?)
    val isDefault: Boolean
)