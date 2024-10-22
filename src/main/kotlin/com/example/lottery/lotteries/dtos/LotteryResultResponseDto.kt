package com.example.lottery.lotteries.dtos

data class LotteryResultResponseDto(
    val lotteryRound: LotteryRoundDto,
    val userDraws: List<UserDrawDto>,
    val prevRound: Int?,
    val nextRound: Int?,
)

data class LotteryRoundDto(
    val round: Int,
    val winAnnounceAtMillis: Long,
    val numbers: NumbersDto,
    val bonus: Int
)

data class NumbersDto(
    val numbers: List<Int>
)

data class UserDrawDto(
    val id: Long,
    val uid: Long,
    val numbers: NumbersDto,
    val canReward: Boolean?,
    val drawnAtMillis: Long,
    val isWin: Boolean?,
    val winPlace: Int?
)
