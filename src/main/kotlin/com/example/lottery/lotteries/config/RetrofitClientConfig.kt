package com.example.lottery.lotteries.config

import com.example.lottery.lotteries.outSideClient.LotteriesAPIClient
import okhttp3.OkHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

@Configuration
class RetrofitClientConfig {

    @Bean
    fun retrofit(): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .build()

        return Retrofit.Builder()
            .baseUrl(DH_LOTTERY_URL)
            .client(okHttpClient)
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
    }

    @Bean
    fun lotteriesAPIClient(retrofit: Retrofit): LotteriesAPIClient {
        return retrofit.create(LotteriesAPIClient::class.java)
    }

    companion object {
        val DH_LOTTERY_URL = "https://www.dhlottery.co.kr"
    }
}
