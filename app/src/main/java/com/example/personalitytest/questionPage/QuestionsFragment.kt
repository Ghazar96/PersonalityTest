package com.example.personalitytest.questionPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.personalitytest.R
import com.google.firebase.storage.ktx.storageMetadata
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class QuestionsFragment : Fragment() {

    private val questionsViewModel: QuestionsViewModel by stateViewModel()

    private var questionList: RecyclerView? = null
    private var startButton: View? = null
    private var listView: View? = null
    private var uploadButton: View? = null
    private var loadingBar: ProgressBar? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.questions_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        loadingBar = view.findViewById(R.id.loading)
        uploadButton = view.findViewById(R.id.uploadButton)
        listView = view.findViewById(R.id.listView)
        startButton = view.findViewById(R.id.startButton)
        questionList = view.findViewById(R.id.questionList)

        startButton?.setOnClickListener {
            startButton?.visibility = View.GONE

            questionsViewModel.downloadQuestions()
        }

        uploadButton?.setOnClickListener {
            questionsViewModel.uploadResults()
        }
    }

    private fun initObservers() {
        questionsViewModel.loadingLiveData.observe(viewLifecycleOwner, { show ->
            if (show) {
                loadingBar?.visibility = View.VISIBLE
            } else {
                loadingBar?.visibility = View.GONE
            }
        })

        questionsViewModel.updateListViewLiveData.observe(viewLifecycleOwner, { questionsResponse ->
            (questionList?.adapter as? QuestionsAdapter)?.let {
                it.questions = questionsResponse
            }
        })

        questionsViewModel.questionsReadyLiveData.observe(viewLifecycleOwner, { data ->
            listView?.visibility = View.VISIBLE
            val llm = LinearLayoutManager(requireContext())
            llm.orientation = LinearLayoutManager.VERTICAL
            questionList?.layoutManager = llm
            val adapter = QuestionsAdapter(
                data, requireContext(),
                { checkedData ->
                    questionsViewModel.updateSingleChoiceQuestions(
                        checkedData.question,
                        checkedData.selectedItem
                    )
                },
                { sliderData ->
                    questionsViewModel.updateNumberRangeQuestions(
                        sliderData.question,
                        sliderData.sliderValue
                    )
                },
                { checkedData ->
                    questionsViewModel.updateSingleChoiceConditionalQuestions(
                        checkedData.question,
                        checkedData.selectedItem
                    )
                }
            )
            questionList?.adapter = adapter
        })

        questionsViewModel.showErrorLiveData.observe(
            viewLifecycleOwner,
            {
                startButton?.visibility = View.VISIBLE
            }
        )
    }
}
