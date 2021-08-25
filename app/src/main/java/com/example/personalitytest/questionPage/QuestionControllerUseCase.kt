package com.example.personalitytest.questionPage

import com.example.personalitytest.network.Question
import com.google.gson.JsonObject
import org.json.JSONObject

interface QuestionControllerUseCase {
    fun checkSingleChoiceConditional(question: Question, selectedItem: String): Boolean

    fun updateSingleChoiceConditionalQuestions(question: Question, selectedItem: String)

    fun updateSingleChoiceQuestions(question: Question, selectedItem: String)

    fun updateNumberRangeQuestions(question: Question, value: Int)

    fun generateJson(): JSONObject
}