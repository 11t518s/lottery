package com.example.lottery.lotteries.dtos

data class PostUserTicketDrawsResponse(
    val numbers: List<Int>,
    val round: Int,
)
