package com.mvi.example.splitmvi.presentation.ads

import com.mvi.example.splitmvi.domain.usecases.GetAdsFlowUseCase
import com.mvi.mvi.mvi.MviProcessor

private const val AdsSwitchingTaskId = "ads task"

internal class AdsMviProcessor(
    private val getAdsFlowUseCase: GetAdsFlowUseCase = GetAdsFlowUseCase(),
) : MviProcessor<AdsState, AdsIntent, AdsSingleEvent>() {

    override val reducer: Reducer<AdsState, AdsIntent> = AdsReducer()

    init {
        sendIntent(AdsIntent.LoadAds)
    }

    override fun initialState() = AdsState()

    override suspend fun handleIntent(
        intent: AdsIntent,
        state: AdsState
    ): AdsIntent? = when (intent) {
        AdsIntent.LoadAds -> {
            observeFlow(AdsSwitchingTaskId) {
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

internal class AdsReducer : MviProcessor.Reducer<AdsState, AdsIntent> {
    override fun reduce(state: AdsState, intent: AdsIntent): AdsState = when(intent) {
        AdsIntent.LoadAds -> state
        is AdsIntent.UpdateAd -> state.copy(currentAd = intent.newAdvertisement)
    }
}
