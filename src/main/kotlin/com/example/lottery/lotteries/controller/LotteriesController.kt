package com.example.lottery.lotteries.controller

import com.example.lottery.lotteries.domain.getCurrentLottoRound
import com.example.lottery.lotteries.dtos.*
import com.example.lottery.lotteries.service.LotteriesService
import com.example.lottery.redis.RedisLockService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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
        val userLotteryInfo = lotteriesService.getOrCreateUserLotteryInfo(uid = uid)

        return ResponseEntity(GetLotteriesUseMeResponse(data = userLotteryInfo), HttpStatus.OK)
    }


    @GetMapping("/missions")
    fun getLotteriesMissions(
        @RequestHeader("uid") uid: Long?
    ): ResponseEntity<GetLotteriesMissionsResponse> {
        val userLotteriesMissions = lotteriesService.getUserLotteryMissions(uid = uid)

        return ResponseEntity(GetLotteriesMissionsResponse(list = userLotteriesMissions), HttpStatus.OK)
    }

    @PostMapping("/missions/{missionId}/complete")
    fun postMissionComplete(
        @PathVariable missionId: Long,
        @RequestHeader("uid") uid: Long
    ): ResponseEntity<PostMissionCompleteResponse> {
        val lockKey = "/missions/${missionId}/complete:${uid}"
        val lockAcquired = redisLockService.tryLock(lockKey, 10)
        return if (lockAcquired) {
            try {
                val result = lotteriesService.rewardUserForMission(missionId = missionId, uid = uid)
                redisLockService.releaseLock(lockKey)

                ResponseEntity(PostMissionCompleteResponse(
                    isSuccess = true,
                    rewardAmount = result.amount
                ), HttpStatus.OK)
            } finally {
                redisLockService.releaseLock(lockKey)
            }
        } else {
            ResponseEntity(HttpStatus.CONFLICT)
        }
    }


    @PostMapping("/current/users/me/draws")
    fun createLotteryDrawTicket(
        @RequestHeader("uid") uid: Long
    ): ResponseEntity<PostUserTicketDrawsResponse> {
        val lockKey = "/current/users/me/draws:${uid}"
        val lockAcquired = redisLockService.tryLock(lockKey, 10)
        return if (lockAcquired) {
            try {
                val result = lotteriesService.saveUserLotteryDrawTicket(uid)
                redisLockService.releaseLock(lockKey)

                ResponseEntity(result, HttpStatus.OK)
            } finally {
                redisLockService.releaseLock(lockKey)
            }
        } else {
            ResponseEntity(HttpStatus.CONFLICT)
        }
    }


    @GetMapping("/current/users/me/draws")
    fun getMyCurrentLotteryResult(
        @RequestHeader("uid") uid: Long
    ): ResponseEntity<LotteryResultResponseDto> {
        val currentRound = getCurrentLottoRound()

        val response = lotteriesService.getCurrentLotteryResultResponse(uid, currentRound)

        return ResponseEntity(response, HttpStatus.OK)
    }


    @GetMapping("/{lotteryRound}/users/me/draws")
    fun getMyLotteryRoundResult(
        @PathVariable lotteryRound: Int,
        @RequestHeader("uid") uid: Long
    ): ResponseEntity<LotteryResultResponseDto> {

        val response = lotteriesService.getCurrentLotteryResultResponse(uid, lotteryRound)

        return ResponseEntity(response, HttpStatus.OK)
    }

    @PostMapping("/{lotteryRound}/users/me/draws/confirm")
    fun confirmUserDraw(
        @PathVariable lotteryRound: Int,
        @RequestHeader("uid") uid: Long
    ): ResponseEntity<Void> {
        lotteriesService.saveLotteryResult(round = lotteryRound, uid = uid)

        return ResponseEntity(HttpStatus.OK)
    }


    @PostMapping("/{lotteryRound}/users/me/draws/{drawId}/reward")
    fun receiveReward(
        @PathVariable lotteryRound: Int,
        @PathVariable drawId: Long,
        @RequestHeader("uid") uid: Long
    ): ResponseEntity<Void> {
        val lockKey = "/${lotteryRound}/users/me/draws/${drawId}/reward:${uid}"
        val lockAcquired = redisLockService.tryLock(lockKey, 10)

        if (lockAcquired) {
            lotteriesService.confirmLotteryResult(
                round = lotteryRound,
                drawId = drawId,
                uid = uid
            )
        } else {
            return ResponseEntity(HttpStatus.CONFLICT)
        }
        return ResponseEntity(HttpStatus.OK)
    }
}