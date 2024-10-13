package com.example.lottery.lotteries.service

import kotlin.random.Random

import com.example.lottery.lotteries.entities.LotteryMission
import com.example.lottery.lotteries.entities.UserLotteryInfo
import com.example.lottery.lotteries.entities.UserLotteryMission
import com.example.lottery.lotteries.repository.LotteryMissionRepository
import com.example.lottery.lotteries.repository.UserLotteryInfoRepository
import com.example.lottery.lotteries.repository.UserLotteryMissionRepository
import com.example.lottery.lotteries.util.getCurrentKoreanDate
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class LotteriesService(
    private val userLotteryInfoRepository: UserLotteryInfoRepository,
    private val lotteriesMissionRepository: LotteryMissionRepository,
    private val userLotteryMissionRepository: UserLotteryMissionRepository
) {

    @Transactional(readOnly = true)
    fun getUserLotteryInfo(uid: Long): UserLotteryInfo {
        val userLotteryInfo = userLotteryInfoRepository.findByIdOrNull(id = uid)

        if (userLotteryInfo != null) {
            return userLotteryInfo
        } else {
            val newUser = userLotteryInfoRepository.save(UserLotteryInfo(uid = uid))
            return newUser
        }
    }

    @Transactional(readOnly = true)
    fun getLotteriesMissions(): List<LotteryMission> {
        return lotteriesMissionRepository.findAllByEnabledTrue()
    }

    @Transactional(readOnly = true)
    fun getUserClearedMissions(uid: Long?): List<UserLotteryMission> {
        return if(uid != null) {
            userLotteryMissionRepository.findAllByUidAndCreatedAt(uid = uid, createdAt = getCurrentKoreanDate()) ?: emptyList()
        } else {
            emptyList()
        }
    }

    @Transactional
    fun saveUserMission(missionId: Long, uid: Long): UserLotteryMission {
        val targetMission = lotteriesMissionRepository.findByIdOrNull(missionId) ?: throw NoSuchElementException("Mission with ID $missionId not found")
        val userLotteryMissions = userLotteryMissionRepository.findAllByUidAndCreatedAt(uid = uid, createdAt = getCurrentKoreanDate()) ?: emptyList()

        val todayUserLotteryRepeatCount = userLotteryMissions.count { it.missionId == targetMission.id }

        if (todayUserLotteryRepeatCount >= targetMission.dailyRepeatableCount) {
            throw IllegalStateException("You have already completed the maximum number of missions for today.")
        }

        val missionRewardCoinAmount = Random.nextInt(1, targetMission.maxCoinAmount + 1)
        val userMission = userLotteryMissionRepository.save(
            UserLotteryMission(
                missionId = missionId,
                amount = missionRewardCoinAmount,
                uid = uid
            )
        )
        userLotteryInfoRepository.incrementTotalScore(
            uid = uid,
            scoreToAdd = missionRewardCoinAmount
        )
        return userMission
    }
}