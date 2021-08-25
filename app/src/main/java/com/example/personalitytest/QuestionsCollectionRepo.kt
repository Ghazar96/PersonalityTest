package com.example.personalitytest

import org.json.JSONObject

interface QuestionsCollectionRepo<T> {
    fun addQuestion(question: String, answer: T)

    fun createJson(): JSONObject
}

class SingleChoiceQuestionsCollectionRepo : QuestionsCollectionRepo<String> {
    private val questions = mutableMapOf<String, String>()

    override fun addQuestion(question: String, answer: String) {
        questions[question] = answer
    }

    override fun createJson(): JSONObject {
        val json = JSONObject()
        questions.forEach { (first, second) ->
            json.put(first, second)
        }

        return json
    }
}

class SingleChoiceConditionalQuestionRepo : QuestionsCollectionRepo<String> {
    private val questions = mutableMapOf<String, String>()

    override fun addQuestion(question: String, answer: String) {
        questions[question] = answer
    }

    override fun createJson(): JSONObject {
        val json = JSONObject()
        questions.forEach { (first, second) ->
            json.put(first, second)
        }

        return json
    }
}

class NumberRangeQuestionRepo : QuestionsCollectionRepo<Int> {
    private val questions = mutableMapOf<String, Int>()

    override fun addQuestion(question: String, answer: Int) {
        questions[question] = answer
    }

    override fun createJson(): JSONObject {
        val json = JSONObject()
        questions.forEach { (first, second) ->
            json.put(first, second)
        }

        return json
    }
}