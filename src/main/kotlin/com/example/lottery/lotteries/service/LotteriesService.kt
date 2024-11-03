package com.example.lottery.lotteries.service

import com.example.lottery.lotteries.domain.LotteryNumbers
import com.example.lottery.lotteries.dtos.*
import com.example.lottery.lotteries.entities.*
import kotlin.random.Random

import com.example.lottery.lotteries.repository.*
import com.example.lottery.lotteries.domain.generateLottoNumbers
import com.example.lottery.lotteries.domain.getCurrentKoreanDate
import com.example.lottery.lotteries.domain.getCurrentLottoRound
import com.example.lottery.user.repository.UserPointRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import kotlin.NoSuchElementException

@Service
class LotteriesService(
    private val userLotteryInfoRepository: UserLotteryInfoRepository,
    private val lotteriesMissionRepository: LotteryMissionRepository,
    private val userLotteryMissionRepository: UserLotteryMissionRepository,
    private val userLotteryDrawTicketRepository: UserLotteryDrawTicketRepository,
    private val lotteryRoundRepository: LotteryRoundRepository,

    private val userPointRepository: UserPointRepository,
) {

    @Transactional
    fun getOrCreateUserLotteryInfo(uid: Long): UserLotteryInfo {
        val userLotteryInfo = userLotteryInfoRepository.findByIdOrNull(id = uid)

        if (userLotteryInfo != null) {
            return userLotteryInfo
        } else {
            val newUser = userLotteryInfoRepository.save(
                UserLotteryInfo(uid = uid)
            )
            return newUser
        }
    }

    @Transactional(readOnly = true)
    fun getUserLotteryMissions(uid: Long?): List<LotteriesMission> {
        val lotteriesMissions = getLotteriesMissions()
        val userLotteriesClearMissions = getUserClearedMissions(uid = uid)

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

        return userLotteriesMissions
    }

    @Transactional(readOnly = true)
    fun getLotteriesMissions(): List<LotteryMission> {
        return lotteriesMissionRepository.findAllByEnabledTrue()
    }

    @Transactional(readOnly = true)
    fun getUserClearedMissions(uid: Long?): List<UserLotteryMission> {
        return if (uid != null) {
            userLotteryMissionRepository.findAllByUidAndCreatedAt(uid, getCurrentKoreanDate()) ?: emptyList()
        } else {
            emptyList()
        }
    }

    @Transactional
    fun rewardUserForMission(missionId: Long, uid: Long): UserLotteryMission {
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
        val userLotteryInfo = this.getOrCreateUserLotteryInfo(uid)

        if (userLotteryInfo.totalCoin < TICKET_DRAW_COIN) {
            throw IllegalStateException("not enough coins")
        }

        val randomLotteryNumbers = LotteryNumbers(generateLottoNumbers())
        val currentRound = getCurrentLottoRound()

        userLotteryInfoRepository.decrementTotalCoin(
            uid = uid,
            coinToSubtract = TICKET_DRAW_COIN
        )
        userLotteryDrawTicketRepository.save(
            UserLotteryDrawTicket(
                round = currentRound,
                numbers = randomLotteryNumbers,
                uid = uid
            )
        )

        return PostUserTicketDrawsResponse(
            numbers = randomLotteryNumbers,
            round = currentRound,
        )
    }

    @Transactional(readOnly = true)
    fun getCurrentLotteryResultResponse(uid: Long, round: Int): LotteryResultResponseDto {
        val currentMyLotteries = getMyLotteriesByRound(uid = uid, round = round)
        val currentLotteryRound = getLotteryRoundByRound(round = round)
        val (prevRound, nextRound) = getPrevAndNextRounds(round = round)

        return LotteryResultResponseDto(
            lotteryRound = LotteryRoundDto(
                round = round,
                winAnnounceAtMillis = Instant.now().toEpochMilli(),
                numbers = currentLotteryRound?.let { NumbersDto(it.numbers) } ?: NumbersDto(emptySet()),
                bonus = currentLotteryRound?.bonus ?: 0
            ),
            userDraws = currentMyLotteries.map { ticket ->
                val isLotterySuccess = ticket.ranking != null

                UserDrawDto(
                    id = ticket.id,
                    uid = ticket.uid,
                    numbers = NumbersDto(ticket.numbers),
                    canReward = ticket.isReceiveReward,
                    drawnAtMillis = ticket.createdAt.toEpochMilli(),
                    isWin = isLotterySuccess,
                    winPlace = ticket.ranking
                )
            },
            prevRound = prevRound,
            nextRound = nextRound,
        )
    }


    @Transactional
    fun saveLotteryResult(round: Int, uid: Long) {
        // 1. 해당 회차의 로또 결과 가져오기
        val lotteryRound = getLotteryRoundByRound(round)
            ?: throw NoSuchElementException("해당 회차의 로또 정보를 찾을 수 없습니다.")

        // 2. 사용자의 로또 티켓 가져오기 (이미 처리된 티켓도 포함)
        val userTickets = getMyLotteriesByRound(uid, round)

        // 3. 처리되지 않은 티켓만 필터링 (ranking이 null인 티켓)
        val newResults = userTickets.filter { it.ranking == null }
            .mapNotNull { ticket ->
                // 순위 계산
                val rankingResult = UserLotteryDrawTicket.RankingResult.calculateRanking(
                    userNumbers = ticket.numbers,
                    winningNumbers = lotteryRound.numbers,
                    bonusNumber = lotteryRound.bonus
                )

                // 순위가 있다면 로또 티켓에 순위 정보 업데이트
                if (rankingResult.isWin) {
                    ticket.ranking = rankingResult.ranking
                    ticket.isReceiveReward = false
                    ticket
                } else {
                    null
                }
            }

        // 4. 새로운 결과를 저장
        if (newResults.isNotEmpty()) {
            userLotteryDrawTicketRepository.saveAll(newResults)
        }
    }


    @Transactional
    fun confirmLotteryResult(round: Int, uid: Long, drawId: Long) {
        // 1. 해당 유저의 로또 티켓을 가져옴
        val ticket = userLotteryDrawTicketRepository.findByUidAndRoundAndId(
            uid = uid,
            round = round,
            id = drawId
        ) ?: throw NoSuchElementException("No lottery ticket found for drawId $drawId, round $round, and uid $uid")

        // 2. 보상 수령 여부 확인
        if (ticket.isReceiveReward) {
            throw IllegalStateException("uid: $uid drawId: $drawId, 보상은 이미 수령했습니다")
        }

        // 3. 티켓의 순위 확인
        val rankingResult = ticket.ranking?.let {
            UserLotteryDrawTicket.RankingResult.of(it)
        } ?: throw IllegalStateException("uid: $uid drawId: $drawId, 해당 티켓의 순위 정보가 없습니다.")

        // 4. 순위에 따른 보상 처리
        when (rankingResult.rewardType) {
            UserLotteryDrawTicket.RewardType.POINT -> {
                // 사용자의 포인트에 보상 추가
                userPointRepository.addPoints(uid, rankingResult.rewardAmount)
            }
            UserLotteryDrawTicket.RewardType.LOTTERY_COIN -> {
                // 사용자의 로또 코인에 보상 추가
                userLotteryInfoRepository.incrementTotalCoin(uid, rankingResult.rewardAmount)
            }
            UserLotteryDrawTicket.RewardType.NONE -> {
                // 1등, 2등의 경우 (제세공과금 등의 이유로 자동 지급되지 않음)
                throw IllegalStateException("1등 또는 2등 보상은 자동으로 지급되지 않으며, 개별 연락이 필요합니다.")
            }
        }

        // 5. 보상 수령 처리 (isReceiveReward 업데이트)
        ticket.isReceiveReward = true
        userLotteryDrawTicketRepository.save(ticket)
    }


    @Transactional(readOnly = true)
    fun getLotteryRoundByRound(round: Int): LotteryRound? {
        val currentLottery = lotteryRoundRepository.findByRound(round = round)

        return currentLottery
    }

    @Transactional(readOnly = true)
   fun getMyLotteriesByRound(uid: Long, round: Int): List<UserLotteryDrawTicket> {
        val userLotteryInfo = userLotteryDrawTicketRepository.findAllByUidAndRound(
            uid = uid,
            round = round
        ) ?: emptyList()

        return userLotteryInfo
    }


    private fun getPrevAndNextRounds(round: Int): Pair<Int?, Int?> {
        val prevRound = if (lotteryRoundRepository.existsByRound(round - 1)) {
            round - 1
        } else {
            null
        }

        val nextRound = if (lotteryRoundRepository.existsByRound(round + 1)) {
            round + 1
        } else {
            null
        }

        return Pair(prevRound, nextRound)
    }

    companion object {
        const val TICKET_DRAW_COIN = 1000
    }
}