package org.sussanacode.workmanagerapplication.team_wm.entity

import com.google.gson.annotations.SerializedName

data class LiveScore(

    @SerializedName("team1")
    val team1: Team,

    @SerializedName("team2")
    val team2: Team,

)


data class Team(
    @SerializedName("name")
    val name: String,

    @SerializedName("goals")
    val goals: Int,
)

