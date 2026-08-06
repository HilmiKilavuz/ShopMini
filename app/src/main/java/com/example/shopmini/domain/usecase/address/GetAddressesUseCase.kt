package com.example.shopmini.domain.usecase.address

import com.example.shopmini.domain.model.Address
import com.example.shopmini.domain.repository.AddressRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAddressesUseCase @Inject constructor(
    private val addressRepository: AddressRepository
) {
    operator fun invoke(): Flow<List<Address>> {
       return addressRepository.getAddresses()
    }

}