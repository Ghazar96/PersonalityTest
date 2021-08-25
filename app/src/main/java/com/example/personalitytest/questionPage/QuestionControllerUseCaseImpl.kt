package com.example.personalitytest.questionPage

import com.example.personalitytest.QuestionsCollectionRepo
import com.example.personalitytest.QuestionsCollectionRepoFactory
import com.example.personalitytest.network.Question
import com.example.personalitytest.network.QuestionType
import com.google.gson.JsonObject
import org.json.JSONObject

class QuestionControllerUseCaseImpl(private val questionsCollectionRepoFactory: QuestionsCollectionRepoFactory) :
    QuestionControllerUseCase {
    override fun checkSingleChoiceConditional(question: Question, selectedItem: String): Boolean {
        return (question.questionType as? QuestionType.SingleChoiceConditional)?.let { singleChoiceConditional ->
            val firstItem = singleChoiceConditional.condition.predicate.exactEquals[0]
            val secondItem = singleChoiceConditional.condition.predicate.exactEquals[1]

            return handleConditionalItem(firstItem, selectedItem) == handleConditionalItem(
                secondItem,
                selectedItem
            )
        } ?: false
    }

    private fun handleConditionalItem(item: String, selectedItem: String): String {
        return if (item.contains('$')) {
            val firstIndex = item.indexOf('{')
            val secondIndex = item.indexOf('}')
            val value = item.substring(firstIndex + 1, secondIndex)
            if (value == "selection") {
                selectedItem
            } else {
                value
            }
        } else {
            item
        }
    }

    override fun updateSingleChoiceConditionalQuestions(question: Question, selectedItem: String) {
        questionsCollectionRepoFactory.getSingleChoiceConditionalQuestionsCollectionRepo()
            .addQuestion(question.question, selectedItem)
    }

    override fun updateSingleChoiceQuestions(question: Question, selectedItem: String) {
        questionsCollectionRepoFactory.getSingleChoiceQuestionsCollectionRepo()
            .addQuestion(question.question, selectedItem)
    }

    override fun updateNumberRangeQuestions(question: Question, value: Int) {
        questionsCollectionRepoFactory.getNumberRangeQuestionsCollectionRepo()
            .addQuestion(question.question, value)
    }

    override fun generateJson(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("Name", "Ghazar Ter-Khachatryan")
        jsonObject.put(
            "",
            questionsCollectionRepoFactory.getNumberRangeQuestionsCollectionRepo().createJson()
        )
        jsonObject.put(
            "",
            questionsCollectionRepoFactory.getSingleChoiceConditionalQuestionsCollectionRepo()
                .createJson()
        )
        jsonObject.put(
            "",
            questionsCollectionRepoFactory.getSingleChoiceQuestionsCollectionRepo().createJson()
        )

        return jsonObject
    }
}