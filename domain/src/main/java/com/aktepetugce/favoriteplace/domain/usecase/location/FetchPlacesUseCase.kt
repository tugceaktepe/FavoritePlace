package com.aktepetugce.favoriteplace.domain.usecase.location

import com.aktepetugce.favoriteplace.data.repository.AuthRepository
import com.aktepetugce.favoriteplace.data.repository.PlaceRepository
import com.aktepetugce.favoriteplace.domain.mapper.PlaceMapper
import com.aktepetugce.favoriteplace.domain.model.Place
import com.aktepetugce.favoriteplace.domain.model.Result
import com.aktepetugce.favoriteplace.domain.model.mapResult
import com.aktepetugce.favoriteplace.domain.model.toResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FetchPlacesUseCase @Inject constructor(
    private val placeRepository: PlaceRepository,
    private val authRepository: AuthRepository,
    private val placeMapper: PlaceMapper
) {
    operator fun invoke(isLoading: Boolean): Flow<Result<List<Place>>> = flow {
        val userEmail = authRepository.getCurrentUserEmail()
        emit(placeRepository.fetchPlaces(userEmail))
    }.toResult(isLoading = isLoading).mapResult {
        placeMapper.mapFrom(it)
    }.flowOn(Dispatchers.IO)
}
