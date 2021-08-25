package com.example.personalitytest.network

sealed class QuestionsResponse {
    data class Success(val categories: Categories, val questions: List<Question>) :
        QuestionsResponse()

    data class Cancelled(val error: String) : QuestionsResponse()
}

data class Categories(val categories: List<String>)

data class Question(
    val question: String,
    val categoryType: CategoryType,
    val questionType: QuestionType
)

sealed class QuestionType {
    data class NumberRange(val range: Range) : QuestionType()
    data class SingleChoice(val options: List<String>) : QuestionType()
    data class SingleChoiceConditional(val options: List<String>, val condition: Condition) :
        QuestionType()
}

data class Condition(val predicate: Predicate, val ifPositive: IfPositive)

data class Predicate(val exactEquals: List<String>)

data class IfPositive(val question: Question)

data class Range(val from: Int, val to: Int)

enum class CategoryType(val type: String) {
    HARD_FACT("hard_fact"),
    LIFESTYLE("lifestyle"),
    INTROVERSION("introversion"),
    PASSION("passion")
}
