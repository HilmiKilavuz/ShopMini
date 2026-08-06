package com.example.shopmini.data.repository

import com.example.shopmini.data.mapper.toDomain
import com.example.shopmini.data.mapper.toDto
import com.example.shopmini.data.model.address.AddressDto
import com.example.shopmini.domain.model.Address
import com.example.shopmini.domain.repository.AddressRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

//Adres işlemleri için kullanılacak repository sınıfı
class AddressRepositoryImpl @Inject constructor(
    private val supaBase: SupabaseClient
) : AddressRepository {
    // Adres bilgilerini Supabase'den çeker
    override fun getAddresses(): Flow<List<Address>> = flow {
        val userId =
            supaBase.auth.currentSessionOrNull()?.user?.id ?: throw Exception("User not logged in")
        val dtoList = supaBase.postgrest["addresses"]
            .select {
                filter { eq("user_id", userId) }
            }.decodeList<AddressDto>()
        emit(dtoList.map { it.toDomain() })
    }

    // Yeni bir adres ekler
    override suspend fun addAddress(address: Address) {
        val userId =
            supaBase.auth.currentSessionOrNull()?.user?.id ?: throw Exception("User not logged in")
        val dto = address.toDto(userId)
        supaBase.postgrest["addresses"].insert(dto)
    }

    //Adresi günceller
    override suspend fun updateAddress(address: Address) {
        val userId =
            supaBase.auth.currentSessionOrNull()?.user?.id ?: throw Exception("User not logged in")
        val dto = address.toDto(userId)
        supaBase.postgrest["addresses"].update(dto) {
            filter { eq("id", address.id) }
        }
    }

    //Adresi siler
    override suspend fun deleteAddress(addressId: String) {
        supaBase.postgrest["addresses"].delete {
            filter { eq("id", addressId) }
        }

    }
}