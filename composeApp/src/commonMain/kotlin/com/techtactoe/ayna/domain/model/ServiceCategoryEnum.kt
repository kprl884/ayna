package com.techtactoe.ayna.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class ServiceCategoryEnum {
    FEATURED,
    CONSULTATION,
    MENS_CUT,
    WOMENS_HAIRCUT,
    STYLE,
    COLOR_APPLICATION,
    QIQI_STRAIGHTENING,
    KIDS
}