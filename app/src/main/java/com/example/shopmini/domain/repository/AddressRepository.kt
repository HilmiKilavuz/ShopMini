package com.example.shopmini.domain.repository

import com.example.shopmini.domain.model.Address
import kotlinx.coroutines.flow.Flow
//Address işlemleri için bir interface
interface AddressRepository {
    fun getAddresses(): Flow<List<Address>>
    suspend fun addAddress(address: Address)
    suspend fun updateAddress(address: Address)
    suspend fun deleteAddress(addressId: String)
}