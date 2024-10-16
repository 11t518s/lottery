package com.example.lottery.lotteries.controller

import com.example.lottery.lotteries.dtos.*
import com.example.lottery.lotteries.service.LotteriesService
import com.example.lottery.lotteries.util.getCurrentLottoRound
import com.example.lottery.redis.RedisLockService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono.delay
import java.time.Instant

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
        val lockKey = "/missions/${missionId}/complete:${uid}"
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


    @PostMapping("/current/users/me/draws")
    fun createLotteryDrawTicket(
        @RequestHeader("uid") uid: Long
    ): ResponseEntity<PostUserTicketDrawsResponse> {
        val lockKey = "/current/users/me/draws:${uid}"
        val lockAcquired = redisLockService.tryLock(lockKey, 10) // 10초 동안 락을 유지
        return if (lockAcquired) {
            try {
                val result = lotteriesService.saveUserLotteryDrawTicket(uid)

                ResponseEntity(result, HttpStatus.OK)
            } finally {
                redisLockService.releaseLock(lockKey) // 락 해제
            }
        } else {
            ResponseEntity(PostUserTicketDrawsResponse(
                numbers = emptyList(),
                bonus = 0,
                round = 0,
            ), HttpStatus.CONFLICT)
        }
    }


    @GetMapping("/current/users/me/draws")
    fun getMyLottery(
        @RequestHeader("uid") uid: Long
    ): ResponseEntity<LotteryResultResponse> {
        val currentRound = getCurrentLottoRound()

        val currentMyLotteries = lotteriesService.getMyLotteriesByRound(
            uid = uid,
            round = currentRound
        )

        val currentLotteryResult = lotteriesService.getLotteryResultByRound(
            uid = uid,
            round = currentRound,
        )

        val currentLotteryRound = lotteriesService.getLotteryRoundByRound(
            round = currentRound
        )

        return ResponseEntity(LotteryResultResponse(
            lotteryRound = LotteryRound(
                round = currentRound,
                winAnnounceAtMillis = Instant.now().toEpochMilli(),
                numbers = Numbers(currentLotteryRound.numbers),
                bonus = currentLotteryRound.bonus
            ),
            userDraws = currentMyLotteries.map { it ->
                val isLotterySuccess = it.id == currentLotteryResult?.drawTicketId

                UserDraw(
                    id = it.id,
                    uid = it.uid,
                    numbers = Numbers(it.numbers),
                    canReward = if (isLotterySuccess) currentLotteryResult?.isReceiveReward else false,
                    drawnAtMillis = it.createdAt.toEpochMilli(),
                    isWin = isLotterySuccess,
                    winPlace = if (isLotterySuccess) currentLotteryResult?.ranking else null
                )
           },
            prevRound = currentRound - 1,
            nextRound = currentRound + 1,
        ), HttpStatus.OK)
    }
}