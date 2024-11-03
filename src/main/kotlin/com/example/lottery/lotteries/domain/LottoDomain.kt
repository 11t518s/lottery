package com.example.lottery.lotteries.domain

import java.time.*
import java.time.temporal.ChronoUnit
import kotlin.random.Random

fun generateLottoNumbers(): Set<Int> {
    val numbers = (1..45).toMutableList()
    val lottoNumbers = mutableSetOf<Int>()
    repeat(6) {
        val randomIndex = Random.nextInt(numbers.size)
        lottoNumbers.add(numbers.removeAt(randomIndex))
    }
    return lottoNumbers.sorted().toSet()
}

fun getCurrentLottoRound(): Int {
    val start = ZonedDateTime.parse("2002-12-07T21:00:00+09:00")
    val now = Instant.now().atZone(ZoneId.of("Asia/Seoul"))
    val weeksBetween = ChronoUnit.WEEKS.between(start, now)
    return (weeksBetween + 2).toInt()
}


fun getCurrentKoreanDate(): LocalDate {
    val zoneId = ZoneId.of("Asia/Seoul")
    return LocalDate.now(zoneId)
}