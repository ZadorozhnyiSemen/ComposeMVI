package com.mvi.example.splitmvi.presentation.ads

import com.mvi.example.splitmvi.domain.entity.Advertisement
import com.mvi.mvi.contract.Intent
import com.mvi.mvi.contract.SingleEvent
import com.mvi.mvi.contract.State

data class AdsState(
    val currentAd: Advertisement? = null
) : State

sealed interface AdsIntent : Intent {
    object LoadAds : AdsIntent
    data class UpdateAd(val newAdvertisement: Advertisement) : AdsIntent
}

sealed interface AdsSingleEvent : SingleEvent