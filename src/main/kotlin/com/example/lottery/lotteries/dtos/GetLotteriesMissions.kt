package com.example.lottery.lotteries.dtos

import com.example.lottery.lotteries.entities.LotteryMission
import java.time.Instant


data class LotteriesMission(
    val id: Long,
    val name: String,
    val maxCoinAmount: Int,
    val dailyRepeatableCount: Int,
    val type: LotteryMission.Type,
    val enabled: Boolean,
    val createdAt: Instant,
    val remainCount: Int
)

data class GetLotteriesMissionsResponse(
    val list: List<LotteriesMission>
)
