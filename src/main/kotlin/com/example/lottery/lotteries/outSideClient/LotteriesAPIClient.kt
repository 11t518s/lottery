package com.example.lottery.lotteries.outSideClient

import com.example.lottery.lotteries.dtos.GetLottoAPIResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface LotteriesAPIClient {
    @GET("/common.do?method=getLottoNumber")
    fun getLottoNumber(@Query("drwNo") drwNo: Int): Call<GetLottoAPIResponse>
}
