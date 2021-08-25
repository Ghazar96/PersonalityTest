package com.example.personalitytest.network

import com.example.personalitytest.UploadEventResponse
import com.example.personalitytest.mapper.QuestionMapper
import org.json.JSONObject

class QuestionsRepoImpl(
    val questionsNetworkService: QuestionsNetworkService,
    private val mapper: QuestionMapper
) : QuestionsRepo {
    override suspend fun getQuestions(): QuestionsResponse {
        return questionsNetworkService.getQuestions()?.let { file ->
            val text = file.readText(Charsets.UTF_8)
            mapper.questionResponseMapper.map(JSONObject(text))
        } ?: QuestionsResponse.Cancelled("Can not parse json")
    }

    override suspend fun uploadQuestions(json: JSONObject): UploadEventResponse {
        return questionsNetworkService.uploadResult(json)
    }
}

