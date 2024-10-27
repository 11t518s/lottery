package com.example.lottery.lotteries.dtos

data class PostUserTicketDrawsResponse(
    val numbers: Set<Int>,
    val round: Int,
)
