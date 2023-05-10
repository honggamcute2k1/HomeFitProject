package com.example.fitnessproject.domain.model

import com.example.fitnessproject.R

enum class TopicType(val type: Int, val resource: Int) {
    TOAN_THAN(1, R.drawable.img_tap_toan_than),
    CO_BUNG(2, R.drawable.img_bung),
    TAP_MONG(3, R.drawable.img_tap_mong),
    TAP_CHAN(4, R.drawable.img_tap_chan),
    TAP_TAY(5, R.drawable.img_tap_tay);


    companion object {
        fun valueOf(type: Int): TopicType {
            return values().first { it.type == type }
        }
    }
}