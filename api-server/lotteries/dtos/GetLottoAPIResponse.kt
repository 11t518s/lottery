package com.example.lottery.lotteries.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class GetLottoAPIResponse(
    @JsonProperty("returnValue") val returnValue: String,
    @JsonProperty("drwNoDate") val drwNoDate: String?,
    @JsonProperty("drwNo") val drwNo: Int,
    @JsonProperty("drwtNo1") val drwtNo1: Int,
    @JsonProperty("drwtNo2") val drwtNo2: Int,
    @JsonProperty("drwtNo3") val drwtNo3: Int,
    @JsonProperty("drwtNo4") val drwtNo4: Int,
    @JsonProperty("drwtNo5") val drwtNo5: Int,
    @JsonProperty("drwtNo6") val drwtNo6: Int,
    @JsonProperty("bnusNo") val bnusNo: Int,
    @JsonProperty("totSellamnt") val totSellamnt: Long,
    @JsonProperty("firstAccumamnt") val firstAccumamnt: Long,
    @JsonProperty("firstWinamnt") val firstWinamnt: Long,
    @JsonProperty("firstPrzwnerCo") val firstPrzwnerCo: Int
)