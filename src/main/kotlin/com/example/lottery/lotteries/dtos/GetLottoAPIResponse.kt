package com.example.lottery.lotteries.dtos

data class GetLottoAPIResult(
    val drwNoDate: String,
    val drwNo: Int,
    val drwtNo1: Int,
    val drwtNo2: Int,
    val drwtNo3: Int,
    val drwtNo4: Int,
    val drwtNo5: Int,
    val drwtNo6: Int,
    val bnusNo: Int,
    val totSellamnt: Long,
    val firstAccumamnt: Long,
    val firstWinamnt: Long,
    val firstPrzwnerCo: Int,
)
