package org.sussanacode.workmanagerapplication.team_wm.bgwm

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import org.sussanacode.workmanagerapplication.team_wm.api.ApiClient
import java.lang.Exception

class LiveScoreWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {


    override suspend fun doWork(): Result {

        try{

            val matchID = inputData.getString("match_id")?:""

            val response = ApiClient.apiService.getLiveScore(matchID)

            if(response.isSuccessful){

                response.body()?.let {
                    val data = Data.Builder()
                        .putString("team1", it.team1.name)
                        .putInt("goals1", it.team1.goals)
                        .putString("team2", it.team2.name)
                        .putInt("goals2", it.team2.goals)
                        .build()

                    setProgress(data)

//                    return Result.success(data)
                    return Result.retry()
                }

            }else{
                val data = Data.Builder()
                    .putString("onFailure", "Failed to load score data. Error code: ${response.code()}")
                    .build()

                return Result.failure(data)

            }


        }catch (ex: Exception){
            val failureData = Data.Builder().putString("error", ex.toString()).build()
            return  Result.failure(failureData)
        }


//        return Result.success()
        return Result.retry()
    }


}