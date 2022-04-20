package com.cd.musicplayerapp.ui.details

data class BottomSheetState(
    val sheetState: BottomSheetStateValue = BottomSheetStateValue.COLLAPSED,
    val progress: Float = 0f,
)

enum class BottomSheetStateValue {
    COLLAPSED, EXPANDED
}