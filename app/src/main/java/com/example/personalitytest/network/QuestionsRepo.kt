package com.example.personalitytest.network

import com.example.personalitytest.UploadEventResponse
import org.json.JSONObject

interface QuestionsRepo {
    suspend fun getQuestions(): QuestionsResponse

    suspend fun uploadQuestions(json: JSONObject): UploadEventResponse
}
