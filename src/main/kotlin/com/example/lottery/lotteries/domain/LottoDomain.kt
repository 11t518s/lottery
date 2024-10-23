package com.example.lottery.lotteries.domain

import java.time.*
import java.time.temporal.ChronoUnit
import kotlin.random.Random

object LottoDomain {
    data class LottoNumbersResult(val numbers: List<Int>)

    data class RankingResult private constructor(
        val isWin: Boolean,
        val place: Int,
        val rewardType: RewardType,
        val rewardAmount: Int,
    ) {
        companion object {
            private const val NO_WIN_PLACE = -1 // 미당첨시 순위

            fun lose() = of(NO_WIN_PLACE)

            fun of(place: Int): RankingResult {
                val (rewardType, amount) = when (place) {
                    1 -> RewardType.NONE to 10_000_000
                    2 -> RewardType.NONE to 1_000_000
                    3 -> RewardType.POINT to 50_000
                    4 -> RewardType.POINT to 1_000
                    5 -> RewardType.LOTTERY_COIN to 1_000
                    else -> RewardType.NONE to 0
                }
                return RankingResult(place != NO_WIN_PLACE, place, rewardType, amount)
            }
        }
    }

    enum class RewardType {
        NONE,
        POINT,
        LOTTERY_COIN,
    }

    fun generateLottoNumbers(): LottoNumbersResult {
        val numbers = (1..45).toMutableList()
        val lottoNumbers = mutableListOf<Int>()
        repeat(6) {
            val randomIndex = Random.nextInt(numbers.size)
            lottoNumbers.add(numbers.removeAt(randomIndex))
        }
        return LottoNumbersResult(lottoNumbers.sorted())
    }

    fun getCurrentLottoRound(): Int {
        val start = ZonedDateTime.parse("2002-12-07T21:00:00+09:00")
        val now = Instant.now().atZone(ZoneId.of("Asia/Seoul"))
        val weeksBetween = ChronoUnit.WEEKS.between(start, now)
        return (weeksBetween + 2).toInt()
    }

    fun calculateRanking(userNumbers: List<Int>, winningNumbers: List<Int>, bonusNumber: Int?): RankingResult {
        val matchCount = userNumbers.toSet().count { it in winningNumbers.toSet() }
        val place = when (matchCount) {
            6 -> 1
            5 -> if (bonusNumber != null && bonusNumber in userNumbers) 2 else 3
            4 -> 4
            3 -> 5
            else -> null
        }

        return place?.let { RankingResult.of(it) } ?: RankingResult.lose()
    }


    fun getCurrentKoreanDate(): LocalDate {
        val zoneId = ZoneId.of("Asia/Seoul")
        return LocalDate.now(zoneId)
    }
}