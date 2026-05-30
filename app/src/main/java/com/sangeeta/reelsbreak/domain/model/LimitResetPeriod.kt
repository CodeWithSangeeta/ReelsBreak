package com.sangeeta.reelsbreak.domain.model


enum class LimitResetPeriod(val value: Int) {
    HOUR(0),
    DAY(1);

    companion object {
        fun fromValue(value: Int): LimitResetPeriod {
            return entries.firstOrNull { it.value == value } ?: DAY
        }
    }
}