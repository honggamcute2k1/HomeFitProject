package com.example.fitnessproject.domain.model

enum class State(val state: Int) {
    NOT_STARTED(-1),
    DONE(1);

    companion object {
        fun valueOf(state: Int): State? {
            return values().firstOrNull { it.state == state }
        }
    }
}