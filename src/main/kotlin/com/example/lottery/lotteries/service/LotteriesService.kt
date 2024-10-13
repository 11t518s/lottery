package com.example.lottery.lotteries.service

import com.example.lottery.lotteries.entities.LotteryMission
import com.example.lottery.lotteries.entities.UserLotteryInfo
import com.example.lottery.lotteries.entities.UserLotteryMission
import com.example.lottery.lotteries.repository.LotteryMissionRepository
import com.example.lottery.lotteries.repository.UserLotteryInfoRepository
import com.example.lottery.lotteries.repository.UserLotteryMissionRepository
import com.example.lottery.lotteries.util.getCurrentKoreanDate
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class LotteriesService(
    private val userLotteryInfoRepository: UserLotteryInfoRepository,
    private val lotteriesMissionRepository: LotteryMissionRepository,
    private val userLotteryMissionRepository: UserLotteryMissionRepository
) {

    fun getUserLotteryInfo(uid: Long): UserLotteryInfo {
        val userLotteryInfo = userLotteryInfoRepository.findByIdOrNull(id = uid)

        if (userLotteryInfo != null) {
            return userLotteryInfo
        } else {
            val newUser = userLotteryInfoRepository.save(UserLotteryInfo(uid = uid))
            return newUser
        }
    }

    fun getLotteriesMissions(): List<LotteryMission> {
        return lotteriesMissionRepository.findAllByEnabledTrue()
    }

    fun getUserClearedMissions(uid: Long?): List<UserLotteryMission> {
        return if(uid != null) {
            userLotteryMissionRepository.findAllByUidAndCreatedAt(uid = uid, createdAt = getCurrentKoreanDate()) ?: emptyList()
        } else {
            emptyList()
        }
    }
}