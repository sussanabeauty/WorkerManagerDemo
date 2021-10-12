package org.sussanacode.workmanagerapplication.team_wm

import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.work.*
import org.sussanacode.workmanagerapplication.databinding.ActivityLiveScoreBinding
import org.sussanacode.workmanagerapplication.team_wm.bgwm.LiveScoreWorker
import java.util.concurrent.TimeUnit

class LiveScoreActivity : AppCompatActivity() {
    lateinit var binding: ActivityLiveScoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLiveScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startLoadingLiveScore()
    }

    private fun startLoadingLiveScore() {

        val timeInterval: Long= 30000
        val inputData = Data.Builder().putString("match_id", "team_a_vs_team_b").build()


        val liveScoreReq = PeriodicWorkRequestBuilder<LiveScoreWorker>(timeInterval, TimeUnit.SECONDS)
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .setInitialDelay(5, TimeUnit.SECONDS)
            .setInputData(inputData)
            .build()


        val wm = WorkManager.getInstance(this)

        wm.enqueue(liveScoreReq)

        wm.getWorkInfoByIdLiveData(liveScoreReq.id).observe(this){
            info: WorkInfo ->

            when(info.state){
                WorkInfo.State.RUNNING -> {
                    val firstTeamName = info.progress.getString("team1")
                    val firstTeamGoals = info.progress.getInt("goals1", 0)
                    val secondTeamName = info.progress.getString("team2")
                    val secondTeamGoals = info.progress.getInt("goals2", 0)
                    binding.tvscores.text = "$firstTeamName [$firstTeamGoals ] vs $secondTeamName [$secondTeamGoals]"
                }

                WorkInfo.State.FAILED -> {
                    val error = info.outputData.getString("onFailure")
                    Toast.makeText(baseContext, error, Toast.LENGTH_LONG).show()

                    binding.tvscores.text = error
                }

                else -> {Toast.makeText(baseContext, "Some thing went wrong. CHECK CODE", Toast.LENGTH_LONG).show()}
            }
        }
    }
}