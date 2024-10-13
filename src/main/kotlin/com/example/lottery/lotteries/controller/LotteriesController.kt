package com.example.lottery.lotteries.controller

import com.example.lottery.lotteries.dtos.GetLotteriesMissionsResponse
import com.example.lottery.lotteries.dtos.GetLotteriesUseMeResponse
import com.example.lottery.lotteries.dtos.LotteriesMission
import com.example.lottery.lotteries.service.LotteriesService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v2/lotteries")
class LotteriesController(
    private val lotteriesService: LotteriesService

) {
    @GetMapping("/users/me")
    fun getLotteriesUserMe(
        @RequestHeader("uid") uid: Long
    ): ResponseEntity<GetLotteriesUseMeResponse> {
        return ResponseEntity(GetLotteriesUseMeResponse(data = lotteriesService.getUserLotteryInfo(uid = uid)), HttpStatus.OK)
    }


    @GetMapping("/missions")
    fun getLotteriesMissions(
        @RequestHeader("uid") uid: Long?
    ): ResponseEntity<GetLotteriesMissionsResponse> {

        val lotteriesMissions = lotteriesService.getLotteriesMissions()
        val userLotteriesClearMissions = lotteriesService.getUserClearedMissions(uid = uid)

        val userLotteriesMissions = lotteriesMissions.map { mission ->
            val clearedCount = userLotteriesClearMissions.count { it.missionId == mission.id }
            val remainCount = (mission.dailyRepeatableCount - clearedCount).coerceAtLeast(0)
            val isEnabled = mission.enabled && remainCount > 0

            LotteriesMission(
                id = mission.id,
                name = mission.name,
                maxCoinAmount = mission.maxCoinAmount,
                dailyRepeatableCount = mission.dailyRepeatableCount,
                type = mission.type,
                enabled = isEnabled,
                createdAt = mission.createdAt,
                remainCount = remainCount
            )
        }


        return ResponseEntity(GetLotteriesMissionsResponse(list = userLotteriesMissions), HttpStatus.OK)
    }



}