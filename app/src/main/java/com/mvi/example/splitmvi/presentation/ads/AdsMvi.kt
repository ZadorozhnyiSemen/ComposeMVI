package com.mvi.example.splitmvi.presentation.ads

import com.mvi.example.splitmvi.domain.usecases.GetAdsFlowUseCase
import com.mvi.mvi.mvi.Mvi

internal class AdsMvi(
    private val getAdsFlowUseCase: GetAdsFlowUseCase = GetAdsFlowUseCase(),
) : Mvi<AdsState, AdsIntent, AdsSingleEvent>() {

    init {
        sendIntent(AdsIntent.LoadAds)
    }

    override fun initialState() = AdsState()

    override fun reduce(
        intent: AdsIntent,
        prevState: AdsState
    ): AdsState = when(intent) {
        AdsIntent.LoadAds -> prevState
        is AdsIntent.UpdateAd -> prevState.copy(currentAd = intent.newAdvertisement)
    }

    override suspend fun performSideEffects(
        intent: AdsIntent,
        state: AdsState
    ): AdsIntent? = when (intent) {
        AdsIntent.LoadAds -> {
            observeFlow(intent) {
                getAdsFlowUseCase()
                    .collect {
                        sendIntent(AdsIntent.UpdateAd(it))
                    }
            }
            null
        }
        is AdsIntent.UpdateAd -> null
    }
}