package com.example.lottery.lotteries.entities

import com.example.lottery.lotteries.domain.LotteryNumbers
import com.vladmihalcea.hibernate.type.json.JsonStringType
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import java.time.Instant
import javax.persistence.*


@Entity
@Table(name = "user_lottery_draw_ticket")
@TypeDef(name = "json", typeClass = JsonStringType::class)
class UserLotteryDrawTicket(
    numbers: LotteryNumbers,
    val round: Int,
    val uid: Long,

    ) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @Type(type = "json")
    @Column(columnDefinition = "text", name = "numbers")
    val numbers: Set<Int> = numbers.numbers

    @CreationTimestamp
    val createdAt: Instant = Instant.now()

    var ranking: Int? = null
    var isReceiveReward: Boolean = false

    enum class RewardType {
        NONE,
        POINT,
        LOTTERY_COIN,
    }

    data class RankingResult private constructor(
        val isWin: Boolean,
        val ranking: Int?,
        val rewardType: RewardType,
        val rewardAmount: Int,
    ) {
        companion object {
            private val NO_WIN_PLACE = null // 미당첨시 순위

            fun lose() = of(NO_WIN_PLACE)

            fun of(ranking: Int?): RankingResult {
                val (rewardType, amount) = when (ranking) {
                    1 -> RewardType.NONE to 10_000_000
                    2 -> RewardType.NONE to 1_000_000
                    3 -> RewardType.POINT to 50_000
                    4 -> RewardType.POINT to 1_000
                    5 -> RewardType.LOTTERY_COIN to 1_000
                    else -> RewardType.NONE to 0
                }
                return RankingResult(ranking != NO_WIN_PLACE, ranking, rewardType, amount)
            }

            fun calculateRanking(userNumbers: Set<Int>, winningNumbers: Set<Int>, bonusNumber: Int?): RankingResult {
                val matchCount = userNumbers.toSet().count { it in winningNumbers.toSet() }
                val place = when (matchCount) {
                    6 -> 1
                    5 -> if (bonusNumber != null && bonusNumber in userNumbers) 2 else 3
                    4 -> 4
                    3 -> 5
                    else -> null
                }

                return place?.let { of(it) } ?: lose()
            }
        }
    }
}