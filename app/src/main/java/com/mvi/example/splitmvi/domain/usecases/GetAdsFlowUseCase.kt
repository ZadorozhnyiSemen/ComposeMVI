package com.mvi.example.splitmvi.domain.usecases

import com.mvi.example.splitmvi.data.AdsRepository
import com.mvi.example.splitmvi.domain.entity.Advertisement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

internal class GetAdsFlowUseCase {

    suspend operator fun invoke(): Flow<Advertisement> {
        return AdsRepository.getAdsFlow()
            .distinctUntilChanged()
    }
}