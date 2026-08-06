package com.example.shopmini.data.mapper

import com.example.shopmini.data.model.address.AddressDto
import com.example.shopmini.domain.model.Address

// Supabase'den gelen DTO'yu, UI'ın anladığı saf Address'e çevirir
fun AddressDto.toDomain(): Address {
    return Address(
        id = this.id ?: "",
        title = this.title,
        fullName = this.fullName,
        phone = this.phone,
        city = this.city,
        district = this.district,
        neighborhood = this.neighborhood,
        fullAddress = this.fullAddress,
        addressType = this.addressType,
        isDefault = this.isDefault
    )
}

// Yeni adres kaydederken veya güncellerken, saf Address'i DTO'ya çevirir (ve user_id'yi içine zerk eder)
fun Address.toDto(userId: String): AddressDto {
    return AddressDto(
        id = if (this.id.isEmpty()) null else this.id,
        userId = userId,
        title = this.title,
        fullName = this.fullName,
        phone = this.phone,
        city = this.city,
        district = this.district,
        neighborhood = this.neighborhood,
        fullAddress = this.fullAddress,
        addressType = this.addressType,
        isDefault = this.isDefault
    )
}
