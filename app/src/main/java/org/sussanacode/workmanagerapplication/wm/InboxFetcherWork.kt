package org.sussanacode.workmanagerapplication.wm

import android.content.Context
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

class InboxFetcherWork(context: Context, params: WorkerParameters) : Worker(context, params) {


    override fun doWork(): Result {
        //user id as input data to this doWork()

        val userID = inputData.getInt("user_id", -1)
        if(userID == -1){
            val data = Data.Builder()
                .putString("message", "User id is required")
                .build()


            return Result.failure(data)
        }


        for (i in 1..10 ){
            Thread.sleep(1000)

            val progressData = Data.Builder()
                .putInt("total_messages", 10)
                .putInt("loaded_messages", i).build()

            setProgressAsync(progressData)
        }


        val data = Data.Builder()
            .putString("message", "Inbox has synced with server successfully.")
            .build()


        return Result.success(data)

    }
}