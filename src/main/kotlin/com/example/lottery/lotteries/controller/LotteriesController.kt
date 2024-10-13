package com.example.lottery.lotteries.controller

import com.example.lottery.lotteries.dtos.GetLotteriesMissionsResponse
import com.example.lottery.lotteries.dtos.GetLotteriesUseMeResponse
import com.example.lottery.lotteries.dtos.LotteriesMission
import com.example.lottery.lotteries.dtos.PostMissionCompleteResponse
import com.example.lottery.lotteries.service.LotteriesService
import com.example.lottery.redis.RedisLockService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono.delay

@RestController
@RequestMapping("/v2/lotteries")
class LotteriesController(
    private val lotteriesService: LotteriesService,
    private val redisLockService: RedisLockService,
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

    @PostMapping("/missions/{missionId}/complete")
    fun postMissionComplete(
        @PathVariable missionId: Long,
        @RequestHeader("uid") uid: Long
    ): ResponseEntity<PostMissionCompleteResponse> {
        val lockKey = "lottery-mission-lock:$uid"
        val lockAcquired = redisLockService.tryLock(lockKey, 10) // 10초 동안 락을 유지
        return if (lockAcquired) {
            try {
                val result = lotteriesService.saveUserMission(missionId = missionId, uid = uid)

                ResponseEntity(PostMissionCompleteResponse(
                    isSuccess = true,
                    rewardAmount = result.amount
                ), HttpStatus.OK)
            } finally {
                redisLockService.releaseLock(lockKey) // 락 해제
            }
        } else {
            ResponseEntity(PostMissionCompleteResponse(
                isSuccess = false,
                rewardAmount = 0
            ), HttpStatus.CONFLICT)
        }
    }
}