package com.example.personalitytest.questionPage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalitytest.UploadEventResponse
import com.example.personalitytest.network.Question
import com.example.personalitytest.network.QuestionType
import com.example.personalitytest.network.QuestionsRepo
import com.example.personalitytest.network.QuestionsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuestionsViewModel(
    val questionsRepo: QuestionsRepo,
    val questionControllerUseCase: QuestionControllerUseCase
) : ViewModel() {
    private var _showErrorLiveData: MutableLiveData<String> = MutableLiveData()
    var showErrorLiveData: LiveData<String> = _showErrorLiveData

    private var _loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var loadingLiveData: LiveData<Boolean> = _loadingLiveData

    private var _updateListViewLiveData: MutableLiveData<QuestionsResponse.Success> =
        MutableLiveData()
    var updateListViewLiveData: LiveData<QuestionsResponse.Success> = _updateListViewLiveData

    private var _questionsReadyLiveData: MutableLiveData<QuestionsResponse.Success> =
        MutableLiveData()
    var questionsReadyLiveData: LiveData<QuestionsResponse.Success> = _questionsReadyLiveData

    fun downloadQuestions() {
        viewModelScope.launch(Dispatchers.IO) {
            _loadingLiveData.postValue(true)
            val requestState = questionsRepo.getQuestions()
            if (requestState is QuestionsResponse.Success) {
                _questionsReadyLiveData.postValue(requestState)
            } else if (requestState is QuestionsResponse.Cancelled) {
                _showErrorLiveData.postValue(requestState.error)
            }
            _loadingLiveData.postValue(false)
        }
    }

    private fun findQuestionIndex(question: Question): Int {
        val questions = _questionsReadyLiveData.value?.questions

        questions?.let {
            it.forEachIndexed { index, q ->
                if (q.question == question.question) {
                    return index
                }
            }
        }
        return 0
    }

    fun updateSingleChoiceConditionalQuestions(question: Question, selectedItem: String) {
        if (questionControllerUseCase.checkSingleChoiceConditional(question, selectedItem)) {
            (question.questionType as? QuestionType.SingleChoiceConditional)?.let {
                val index = findQuestionIndex(question)
                val questions = _questionsReadyLiveData.value?.questions
                (questions as? ArrayList)?.add(index + 1, it.condition.ifPositive.question)
                _updateListViewLiveData.postValue(_questionsReadyLiveData.value)
            }
        }

        questionControllerUseCase.updateSingleChoiceConditionalQuestions(question, selectedItem)
    }

    fun updateSingleChoiceQuestions(question: Question, selectedItem: String) {
        questionControllerUseCase.updateSingleChoiceQuestions(question, selectedItem)

    }

    fun updateNumberRangeQuestions(question: Question, value: Int) {
        questionControllerUseCase.updateNumberRangeQuestions(question, value)
    }

    fun uploadResults() {
        viewModelScope.launch {
            _loadingLiveData.postValue(true)
            val json = questionControllerUseCase.generateJson()

            val response = questionsRepo.uploadQuestions(json)
            if (response is UploadEventResponse.Cancelled) {
                _showErrorLiveData.postValue(response.error)
            }
            _loadingLiveData.postValue(false)
        }
    }
}
