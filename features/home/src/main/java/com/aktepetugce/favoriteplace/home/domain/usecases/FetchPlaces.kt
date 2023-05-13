package com.aktepetugce.favoriteplace.home.domain.usecases

import com.aktepetugce.favoriteplace.common.data.repo.AuthRepository
import com.aktepetugce.favoriteplace.common.data.repo.PlaceRepository
import com.aktepetugce.favoriteplace.common.domain.mapper.PlaceMapper
import com.aktepetugce.favoriteplace.common.domain.model.Place
import com.aktepetugce.favoriteplace.common.extension.mapResult
import com.aktepetugce.favoriteplace.common.extension.toResult
import com.aktepetugce.favoriteplace.common.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FetchPlaces @Inject constructor(
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
