package com.example.shopmini.domain.usecase.address

import com.example.shopmini.domain.model.Address
import com.example.shopmini.domain.repository.AddressRepository
import javax.inject.Inject

class UpdateAddressUseCase @Inject constructor(
    private  val addressRepository: AddressRepository
) {
    suspend operator fun invoke(address: Address) {
        addressRepository.updateAddress(address)
    }

}