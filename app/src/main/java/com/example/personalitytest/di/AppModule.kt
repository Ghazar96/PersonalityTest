package com.example.personalitytest.di

import androidx.lifecycle.SavedStateHandle
import com.example.personalitytest.QuestionsCollectionRepoFactory
import com.example.personalitytest.mapper.QuestionMapper
import com.example.personalitytest.network.QuestionsFirebaseNetworkService
import com.example.personalitytest.network.QuestionsNetworkService
import com.example.personalitytest.network.QuestionsRepo
import com.example.personalitytest.network.QuestionsRepoImpl
import com.example.personalitytest.questionPage.QuestionControllerUseCase
import com.example.personalitytest.questionPage.QuestionControllerUseCaseImpl
import com.example.personalitytest.questionPage.QuestionsViewModel
import com.google.firebase.storage.FirebaseStorage
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun appModule() = module {
    viewModel { (handle: SavedStateHandle) ->
        QuestionsViewModel(
            questionsRepo = get(),
            questionControllerUseCase = get()
        )
    }

    factory<QuestionControllerUseCase> {
        QuestionControllerUseCaseImpl(questionsCollectionRepoFactory = QuestionsCollectionRepoFactory())
    }

    factory<QuestionsRepo> {
        QuestionsRepoImpl(questionsNetworkService = get(), mapper = QuestionMapper())
    }

    factory<QuestionsNetworkService> {
        QuestionsFirebaseNetworkService(FirebaseStorage.getInstance())
    }
}
