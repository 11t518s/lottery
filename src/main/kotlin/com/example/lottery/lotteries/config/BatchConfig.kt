package com.example.lottery.lotteries.config

import com.example.lottery.lotteries.domain.getCurrentLottoRound
import com.example.lottery.lotteries.entities.LotteryRound
import com.example.lottery.lotteries.outSideClient.LotteriesAPIClient
import com.example.lottery.lotteries.repository.LotteryRoundRepository
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.batch.core.Job
import org.springframework.batch.repeat.RepeatStatus

@Configuration
@EnableBatchProcessing
class BatchConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val lotteryRoundRepository: LotteryRoundRepository,
    private val lotteriesAPIClient: LotteriesAPIClient
) {

    @Bean
    fun lotteryJob(): Job {
        return jobBuilderFactory.get("lotteryJob")
            .start(lotteryStep())
            .build()
    }

    @Bean
    fun lotteryStep(): Step {
        return stepBuilderFactory.get("lotteryStep")
            .tasklet(lotteryTasklet())
            .allowStartIfComplete(true)
            .build()
    }

    @Bean
    fun lotteryTasklet(): Tasklet {
        return Tasklet { contribution, chunkContext ->
            val currentRound = getCurrentLottoRound()
            val existingLottoRound = lotteryRoundRepository.findById(currentRound)

            if (existingLottoRound.isPresent) {
                println("Lotto round ${currentRound} already exists, stopping batch.")
                return@Tasklet RepeatStatus.FINISHED
            }

            val response = lotteriesAPIClient.getLottoNumber(currentRound).execute()

            if (response.isSuccessful) {
                val responseBody = response.body()

                when (responseBody?.returnValue) {
                    "fail" -> {
                        println("API returned failure")
                    }
                    "success" -> {
                        val lottoResult = responseBody.let {
                            LotteryRound(
                                round = it.drwNo,
                                numbers = setOf(it.drwtNo1, it.drwtNo2, it.drwtNo3, it.drwtNo4, it.drwtNo5, it.drwtNo6).sorted().toSet(),
                                bonus = it.bnusNo
                            )
                        }

                        lottoResult.let {
                            lotteryRoundRepository.save(it)
                            println("Lotto round 1141 saved successfully.")
                        }
                    }
                    else -> {
                        println("Unexpected returnValue: ${responseBody?.returnValue}")
                    }
                }
            }

            RepeatStatus.FINISHED
        }
    }
}
