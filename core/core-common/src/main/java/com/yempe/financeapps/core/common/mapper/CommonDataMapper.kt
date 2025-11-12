package com.yempe.financeapps.core.common.mapper

import jakarta.inject.Inject
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class CommonDataMapper @Inject constructor() {

    fun stringToBigDecimal(value: String?): BigDecimal {
        return value?.toBigDecimalOrNull() ?: BigDecimal.ZERO
    }

    fun bigDecimalToString(value: BigDecimal): String {
        return value.toPlainString()
    }

    fun longToLocalDateTime(timestamp: Long): LocalDateTime {
        return LocalDateTime.ofInstant(
            Instant.ofEpochMilli(timestamp),
            ZoneId.systemDefault()
        )
    }

    fun localDateTimeToLong(dateTime: LocalDateTime): Long {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

}