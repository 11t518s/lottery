package com.example.lottery.lotteries.dtos

import com.example.lottery.lotteries.domain.LotteryNumbers

data class PostUserTicketDrawsResponse(
    val numbers: LotteryNumbers,
    val round: Int,
)
