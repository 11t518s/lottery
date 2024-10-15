package com.example.lottery.lotteries.service

import com.example.lottery.lotteries.dtos.PostUserTicketDrawsResponse
import com.example.lottery.lotteries.entities.*
import kotlin.random.Random

import com.example.lottery.lotteries.repository.*
import com.example.lottery.lotteries.util.generateLottoNumbers
import com.example.lottery.lotteries.util.getCurrentKoreanDate
import com.example.lottery.lotteries.util.getCurrentLottoRound
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class LotteriesService(
    private val userLotteryInfoRepository: UserLotteryInfoRepository,
    private val lotteriesMissionRepository: LotteryMissionRepository,
    private val userLotteryMissionRepository: UserLotteryMissionRepository,
    private val userLotteryDrawTicketRepository: UserLotteryDrawTicketRepository,
    private val lotteryRoundRepository: LotteryRoundRepository,
    private val lotteryResultRepository: LotteryResultRepository
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
        userLotteryInfoRepository.incrementTotalCoin(
            uid = uid,
            coinToAdd = missionRewardCoinAmount
        )
        return userMission
    }

    @Transactional
    fun saveUserLotteryDrawTicket(uid: Long): PostUserTicketDrawsResponse {
        val userLotteryInfo = userLotteryInfoRepository.findByIdOrNull(uid) ?:  throw NoSuchElementException("Mission with ID $uid not found")

        if (userLotteryInfo.totalCoin < TICKET_DRAW_COIN) {
            throw IllegalStateException("not enough coins")
        }

        val randomLotteryNumbers = generateLottoNumbers()
        val currentRound = getCurrentLottoRound()

        userLotteryInfoRepository.decrementTotalCoin(
            uid = uid,
            coinToSubtract = TICKET_DRAW_COIN
        )
        userLotteryDrawTicketRepository.save(
            UserLotteryDrawTicket(
                round = currentRound,
                numbers = randomLotteryNumbers.numbers,
                bonus = randomLotteryNumbers.bonus,
                uid = uid
            )
        )

        return PostUserTicketDrawsResponse(
            numbers = randomLotteryNumbers.numbers,
            bonus = randomLotteryNumbers.bonus,
            round = currentRound,
        )
    }

    @Transactional(readOnly = true)
    fun getMyLotteriesByRound(uid: Long, round: Int): List<UserLotteryDrawTicket> {
        val userLotteryInfo = userLotteryDrawTicketRepository.findAllByUidAndRound(
            uid = uid,
            round = round
        )

        return userLotteryInfo
    }

    @Transactional(readOnly = true)
    fun getLotteryResultByRound(uid: Long, round: Int): LotteryResult {
        val currentLotteryResult = lotteryResultRepository.findByRoundAndUid(round = round, uid = uid)

        return currentLotteryResult
    }


    @Transactional(readOnly = true)
    fun getLotteryRoundByRound(round: Int): LotteryRound {
        val currentLottery = lotteryRoundRepository.findByRound(round = round)

        return currentLottery
    }

    companion object {
        const val TICKET_DRAW_COIN = 1000
    }
}