package org.sussanacode.workmanagerapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.work.*
import org.sussanacode.workmanagerapplication.databinding.ActivityMainBinding
import org.sussanacode.workmanagerapplication.wm.InboxFetcherWork

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var workManager: WorkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnLoadMessages.setOnClickListener { loadUserMessages() }
    }



    private fun loadUserMessages(){

        val data = Data.Builder()
//            .putInt("user_id", 101).build()
            .putInt("user_id", -1).build()


        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
//            .setRequiresBatteryNotLow(true)
           // .setRequiresCharging(true)
            .build()

        workManager = WorkManager.getInstance(this)
        val inboxWorkRequest = OneTimeWorkRequestBuilder<InboxFetcherWork>()
            .setConstraints(constraints)
            .setInputData(data)
            .build()

        workManager.enqueue(inboxWorkRequest)
        workManager.getWorkInfoByIdLiveData(inboxWorkRequest.id).observe(this){
            info: WorkInfo ->

            Log.d("MainActivity", "LoadMessage: ${info.state}")


            when(info.state){

                WorkInfo.State.SUCCEEDED ->{
                    val message = info.outputData.getString("message")
                    binding.tvmesage.text = message
                    Toast.makeText(baseContext, message, Toast.LENGTH_LONG).show()
                }


                WorkInfo.State.FAILED ->{
                    val message = info.outputData.getString("message")
                    binding.tvmesage.text = message
                    Toast.makeText(baseContext, message, Toast.LENGTH_LONG).show()
                }

                WorkInfo.State.RUNNING -> {
                    val totalMessages = info.progress.getInt("total_messages", -1)
                    val loadedMessages = info.progress.getInt("loaded_messages", -1)

                    if(totalMessages != -1 && loadedMessages != -1){

                        binding.tvmesage.setText("$loadedMessages / $totalMessages loading")

                    }
                }
                else -> {}
            }

        }

    }
}