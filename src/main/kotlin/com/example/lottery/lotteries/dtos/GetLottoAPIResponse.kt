package com.example.lottery.lotteries.dtos

data class GetLottoAPIResponse(
    val returnValue: String = "",  // "fail" 또는 "success"
    val drwNoDate: String = "",
    val drwNo: Int = -1,
    val drwtNo1: Int = -1,
    val drwtNo2: Int = -1,
    val drwtNo3: Int = -1,
    val drwtNo4: Int = -1,
    val drwtNo5: Int = -1,
    val drwtNo6: Int = -1,
    val bnusNo: Int = -1,
    val totSellamnt: Long = -1L,
    val firstAccumamnt: Long = -1L,
    val firstWinamnt: Long = -1L,
    val firstPrzwnerCo: Int = -1,
)
