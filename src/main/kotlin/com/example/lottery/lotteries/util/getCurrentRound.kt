package com.example.lottery.lotteries.util

import java.time.*
import java.time.temporal.ChronoUnit

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


fun getCurrentKoreanDate(): Instant {
    // 한국 표준시 (KST) 시간대
    val zoneId = ZoneId.of("Asia/Seoul")

    // Instant.now()로 현재 UTC 시간을 가져오고, 한국 시간으로 변환
    val koreanTime = ZonedDateTime.ofInstant(Instant.now(), zoneId).toInstant()

    return koreanTime
}
