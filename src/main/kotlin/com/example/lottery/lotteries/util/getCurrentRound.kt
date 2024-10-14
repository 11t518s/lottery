package com.example.lottery.lotteries.util

import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.random.Random

fun getCurrentLottoRound(): Int {
    // 로또 1회차 추첨일
    val firstLottoDate = LocalDate.of(2002, 12, 7)

    // 현재 시간을 UTC 기준 Instant로 가져온 후 한국 시간대로 변환
    val zoneId = ZoneId.of("Asia/Seoul")
    val now = ZonedDateTime.ofInstant(Instant.now(), zoneId)

    // 로또 발표 시간은 매주 토요일 20:45이기 때문에 21시를 기준으로 나누기로함
    val todayLottoTime = ZonedDateTime.of(now.toLocalDate(), LocalTime.of(21, 0,0), zoneId)

    // 만약 발표 시간이 지나지 않았다면, 오늘 발표는 아직 이뤄지지 않았으므로 이전 주 기준으로 계산
    val effectiveNow = if (now.isBefore(todayLottoTime)) now.minusDays(1) else now

    // 로또는 매주 토요일에 추첨되므로 첫 로또 날짜부터 현재까지 몇 주가 지났는지 계산
    val weeksBetween = ChronoUnit.WEEKS.between(firstLottoDate, effectiveNow.toLocalDate())

    // 라운드는 1부터 시작하므로 첫 로또 이후 지난 주 차 + 1을 반환
    return (weeksBetween + 1).toInt()
}


fun getCurrentKoreanDate(): LocalDate {
    val zoneId = ZoneId.of("Asia/Seoul")
    return LocalDate.now(zoneId)
}


data class LottoNumbersResult(val numbers: List<Int>, val bonus: Int)

fun generateLottoNumbers(): LottoNumbersResult {
    // 1부터 45까지의 숫자 리스트
    val numbers = (1..45).toMutableList()

    // 6개의 로또 번호 뽑기
    val lottoNumbers = mutableListOf<Int>()
    repeat(6) {
        val randomIndex = Random.nextInt(numbers.size)
        lottoNumbers.add(numbers.removeAt(randomIndex))
    }

    // 보너스 번호 뽑기
    val bonus = numbers.random()

    // LottoNumbersResult data class로 반환
    return LottoNumbersResult(lottoNumbers.sorted(), bonus)
}