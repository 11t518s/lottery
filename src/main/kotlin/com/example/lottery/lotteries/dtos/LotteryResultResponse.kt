package com.example.lottery.lotteries.dtos

data class LotteryResultResponse(
    val lotteryRound: LotteryRound,
    val userDraws: List<UserDraw>,
    val prevRound: Int,
    val nextRound: Int
)

data class LotteryRound(
    val round: Int,
    val winAnnounceAtMillis: Long,
    val numbers: Numbers,
    val bonus: Int
)

data class Numbers(
    val numbers: List<Int>
)

data class UserDraw(
    val id: Long,
    val uid: Long,
    val numbers: Numbers,
    val canReward: Boolean,
    val drawnAtMillis: Long,
    val isWin: Boolean,
    val winPlace: Int?
)
