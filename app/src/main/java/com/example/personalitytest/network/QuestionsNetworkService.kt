package com.example.personalitytest.network

import com.example.personalitytest.UploadEventResponse
import org.json.JSONObject
import java.io.File

interface QuestionsNetworkService {
    suspend fun getQuestions(): File?

    suspend fun uploadResult(json: JSONObject): UploadEventResponse
}
