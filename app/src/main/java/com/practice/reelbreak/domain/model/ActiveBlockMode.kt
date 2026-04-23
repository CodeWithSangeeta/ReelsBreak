package com.practice.reelbreak.domain.model


enum class ActiveBlockMode(val value: Int) {
    STRICT(0),
    LIMIT(1),
    SMART(2);

    companion object {
        fun fromValue(value: Int): ActiveBlockMode {
            return values().firstOrNull { it.value == value } ?: STRICT
        }
    }
}
