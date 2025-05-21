package com.politecnico.beforeafter.data.model

import com.example.apppracticasjc.Data.RoomDB.LimitedAppEntity

data class BeforeAfterSettingsUiState (
    var beforeSeconds : Int = 10,
    var afterMinutes : Int = 60,
    var limitedAppsList : List<LimitedAppEntity> = listOf()
)