package org.sussanacode.workmanagerapplication.team_wm.api

import org.sussanacode.workmanagerapplication.team_wm.entity.LiveScore
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

   // @Headers("Content-type: application/json")
    @FormUrlEncoded
    @POST("live_score.php")
    suspend fun getLiveScore(@Field("match_id") matchID: String): Response<LiveScore>

}