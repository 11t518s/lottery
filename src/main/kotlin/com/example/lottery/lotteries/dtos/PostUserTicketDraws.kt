package com.example.lottery.lotteries.dtos

data class PostUserTicketDrawsResponse(
    val numbers: List<Int>,
    val bonus: Int,
    val round: Int,
)
