package com.example.lottery.lotteries.scheduler

import com.example.lottery.lotteries.launcher.LotteryJobLauncher
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled

@Configuration
@EnableScheduling
class LotteryBatchScheduler(
    private val lotteryJobLauncher: LotteryJobLauncher
) {
    // cron 표현식을 필드로 주입받음
    @Value("\${batch-cron.lotteryJob.cron}")
    private lateinit var cronExpression: String

    @Scheduled(cron = "\${batch-cron.lotteryJob.cron}", zone = "Asia/Seoul")
    fun runLotteryJob() {
        lotteryJobLauncher.runLotteryJob()
        println("Running lottery job with cron: $cronExpression")
    }
}
