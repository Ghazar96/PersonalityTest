package com.example.personalitytest.questionPage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.personalitytest.*
import com.example.personalitytest.network.Question
import com.example.personalitytest.network.QuestionType
import com.example.personalitytest.network.QuestionsResponse
import com.google.android.material.slider.Slider

class QuestionsAdapter(
    var questions: QuestionsResponse.Success,
    val context: Context,
    val onSingleChoiceSelected: (CheckedData) -> Unit,
    val onRangeSliderChanged: (SliderData) -> Unit,
    val onSingleChoiceConditionalSelected: (CheckedData) -> Unit
) :
    RecyclerView.Adapter<QuestionsAdapter.BaseViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return when (questions.questions[position].questionType) {
            is QuestionType.NumberRange -> NUMBER_RANGE_TYPE
            is QuestionType.SingleChoice -> SINGLE_CHOICE_TYPE
            is QuestionType.SingleChoiceConditional -> SINGLE_CHOICE_CONDITIONAL_TYPE
        }
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    companion object {
        const val NUMBER_RANGE_TYPE = 0
        const val SINGLE_CHOICE_TYPE = 1
        const val SINGLE_CHOICE_CONDITIONAL_TYPE = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            NUMBER_RANGE_TYPE -> {
                val view =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.number_range_item, parent, false)
                NumberRangeViewHolder(view)
            }
            SINGLE_CHOICE_TYPE -> {
                val view =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.single_choice_item, parent, false)
                SingleChoiceViewHolder(view)
            }
            else -> {
                val view =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.single_choice_conditional_item, parent, false)
                SingleChoiceConditionalViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(questions.questions[position])
    }

    override fun getItemCount(): Int {
        return questions.questions.count()
    }

    inner class NumberRangeViewHolder(view: View) : BaseViewHolder(view) {
        override fun onBind(data: Question) {
            (data.questionType as? QuestionType.NumberRange)?.let { numberRangeQuestion ->
                val questionText = itemView.findViewById<TextView>(R.id.questionText)
                val slider = itemView.findViewById<Slider>(R.id.rangeSlider)
                val fromText = itemView.findViewById<TextView>(R.id.fromText)
                val toText = itemView.findViewById<TextView>(R.id.toText)
                val resultText = itemView.findViewById<TextView>(R.id.resultText)

                questionText.text = data.question
                fromText.text = numberRangeQuestion.range.from.toString()
                toText.text = numberRangeQuestion.range.to.toString()
                slider.valueFrom = numberRangeQuestion.range.from.toFloat()
                slider.valueTo = numberRangeQuestion.range.to.toFloat()
                val currentValue =
                    (numberRangeQuestion.range.from + numberRangeQuestion.range.to) / 2f
                slider.value = currentValue
                resultText.text = currentValue.toString()

                val sliderData = SliderData(data, currentValue.toInt())
                slider.addOnChangeListener { _, value, fromUser ->
                    if (fromUser) {
                        resultText.text = value.toString()
                    }

                    sliderData.sliderValue = value.toInt()
                    onRangeSliderChanged.invoke(sliderData)
                }
            }
        }
    }

    inner class SingleChoiceViewHolder(view: View) : BaseViewHolder(view) {
        override fun onBind(data: Question) {
            val text = itemView.findViewById<TextView>(R.id.questionText)
            val optionsListView = itemView.findViewById<RecyclerView>(R.id.optionsListView)

            text.text = data.question

            val items = (data.questionType as? QuestionType.SingleChoice)?.options

            items?.let {
                val adapter = SingleChoiceRowAdapter(it) { item ->
                    onSingleChoiceSelected.invoke(CheckedData(data, item))
                }
                val llm = LinearLayoutManager(context)
                llm.orientation = LinearLayoutManager.VERTICAL
                optionsListView?.layoutManager = llm
                optionsListView.adapter = adapter
            }
        }
    }

    inner class SingleChoiceConditionalViewHolder(view: View) : BaseViewHolder(view) {
        override fun onBind(data: Question) {
            (data.questionType as? QuestionType.SingleChoiceConditional)?.let { singleChoiceConditionalData ->
                val questionText = itemView.findViewById<TextView>(R.id.questionText)
                val spinner = itemView.findViewById<Spinner>(R.id.spinner)

                questionText.text = data.question
                val adapter: ArrayAdapter<String> =
                    ArrayAdapter<String>(
                        context,
                        android.R.layout.simple_spinner_item,
                        singleChoiceConditionalData.options
                    )

                spinner.adapter = adapter
                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        val selectedItem = singleChoiceConditionalData.options[position]
                        onSingleChoiceConditionalSelected.invoke(
                            CheckedData(
                                data,
                                selectedItem
                            )
                        )
                    }
                }
            }
        }
    }

    abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun onBind(data: Question)
    }
}

data class CheckedData(val question: Question, val selectedItem: String)
data class SliderData(val question: Question, var sliderValue: Int)
