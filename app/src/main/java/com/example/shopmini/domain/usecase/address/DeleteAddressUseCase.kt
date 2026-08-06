package com.example.shopmini.domain.usecase.address

import com.example.shopmini.domain.repository.AddressRepository
import javax.inject.Inject

class DeleteAddressUseCase @Inject constructor(
    private val addressRepository: AddressRepository
) {
    suspend operator fun invoke(addressId: String) {
        addressRepository.deleteAddress(addressId)
    }

}