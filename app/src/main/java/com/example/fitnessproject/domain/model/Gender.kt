package com.example.fitnessproject.domain.model

enum class Gender(val gender: Int) {
    MALE(0),
    FEMALE(1);

    companion object {
        fun valueOf(gender: Int): Gender {
            return values().firstOrNull { it.gender == gender } ?: MALE
        }
    }
}