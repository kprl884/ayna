package com.techtactoe.ayna.domain.model

sealed interface BottomSheetType {
    data object None : BottomSheetType
    data object Filters : BottomSheetType
    data object Sort : BottomSheetType
    data object Price : BottomSheetType
    data object VenueType : BottomSheetType
}