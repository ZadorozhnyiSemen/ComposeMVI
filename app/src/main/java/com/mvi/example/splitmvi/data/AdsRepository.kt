package com.mvi.example.splitmvi.data

import com.mvi.example.splitmvi.domain.entity.Advertisement
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object AdsRepository {

    private val ads = listOf(
        Advertisement(1, "CocaCola", "30% off"),
        Advertisement(2, "Pepsi", "1% off"),
        Advertisement(3, "Google", "5% off"),
        Advertisement(4, "Facebook", "15% off"),
        Advertisement(5, "Twitter", "3% off"),
        Advertisement(6, "Spotify", "5% off"),
        Advertisement(7, "Shopify", "10% off"),
    )

    suspend fun getAdsFlow(): Flow<Advertisement> = flow {
        delay(4000)
        while (true) {
            emit(ads.random())
            delay(4000)
        }
    }
}