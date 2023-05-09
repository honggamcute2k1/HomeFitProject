package com.example.fitnessproject.domain.model

enum class LevelBMI(val level: Int, val detail: String) {
    GAY_1(1, "Gầy độ 1"),
    GAY_2(2, "Gầy độ 2"),
    GAY_3(3, "Gầy độ 3"),
    BINH_THUONG(4, "Bình thường"),
    THUA_CAN(5, "Thừa cân"),
    BEO_1(6, "Béo phì độ 1"),
    BEO_2(7, "Béo phì độ 2"),
    BEO_3(8, "Béo phì độ 3");

    companion object {
        fun getLevel(level: Int): LevelBMI {
            return values().firstOrNull { it.level == level } ?: BINH_THUONG
        }
    }
}