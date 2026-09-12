package com.example.bushido.engine

object NumberUtils {
    private val persianDigits = charArrayOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')

    fun toPersianDigits(value: Any?): String {
        if (value == null) return ""
        val str = value.toString()
        val builder = StringBuilder()
        for (ch in str) {
            if (ch in '0'..'9') {
                builder.append(persianDigits[ch - '0'])
            } else {
                builder.append(ch)
            }
        }
        return builder.toString()
    }
}
