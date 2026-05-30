//package com.practice.reelbreak.domain.model
//
//
//enum class ActiveBlockMode(val value: Int) {
//    STRICT(0),
//    LIMIT(1);
//
//    companion object {
//        fun fromValue(value: Int): ActiveBlockMode {
//            return values().firstOrNull { it.value == value } ?: STRICT
//        }
//    }
//}


package com.practice.reelbreak.domain.model

enum class ActiveBlockMode(val value: Int) {
    STRICT(0),
    LIMIT(1);

    companion object {
        // Using entries to avoid array copy allocations during runtime checks
        fun fromValue(value: Int): ActiveBlockMode {
            return entries.firstOrNull { it.value == value } ?: STRICT
        }
    }
}