package com.example.personalitytest

class QuestionsCollectionRepoFactory {
    private var singleChoiceCollectionRepo: SingleChoiceQuestionsCollectionRepo? = null
    private var singleChoiceQuestionsCollectionRepo: SingleChoiceConditionalQuestionRepo? = null
    private var numberRangeQuestionRepo: NumberRangeQuestionRepo? = null

    fun getSingleChoiceQuestionsCollectionRepo(): QuestionsCollectionRepo<String> {
        if (singleChoiceCollectionRepo == null) {
            singleChoiceCollectionRepo = SingleChoiceQuestionsCollectionRepo()
        }
        return singleChoiceCollectionRepo as SingleChoiceQuestionsCollectionRepo
    }

    fun getSingleChoiceConditionalQuestionsCollectionRepo(): QuestionsCollectionRepo<String> {
        if (singleChoiceQuestionsCollectionRepo == null) {
            singleChoiceQuestionsCollectionRepo = SingleChoiceConditionalQuestionRepo()
        }
        return singleChoiceQuestionsCollectionRepo as SingleChoiceConditionalQuestionRepo
    }

    fun getNumberRangeQuestionsCollectionRepo(): QuestionsCollectionRepo<Int> {
        if (numberRangeQuestionRepo == null) {
            numberRangeQuestionRepo = NumberRangeQuestionRepo()
        }
        return numberRangeQuestionRepo as NumberRangeQuestionRepo
    }
}