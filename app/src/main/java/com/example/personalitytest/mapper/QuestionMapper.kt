package com.example.personalitytest.mapper

import com.example.personalitytest.network.*
import org.json.JSONArray
import org.json.JSONObject

class QuestionMapper {

    val questionResponseMapper = object : Mapper<JSONObject, QuestionsResponse> {
        override fun map(s: JSONObject): QuestionsResponse {
            val categories = s.getJSONArray("categories")

            val questions = s.getJSONArray("questions")
            val questionList = arrayListOf<Question>()
            for (i in 0 until questions.length()) {
                questionMapper.map(questions.getJSONObject(i))?.let {
                    questionList.add(it)
                } ?: return QuestionsResponse.Cancelled("Can not parse Question number $i")
            }

            return QuestionsResponse.Success(
                categories = categoriesMapper.map(categories),
                questionList
            )
        }
    }

    val questionMapper = object : Mapper<JSONObject, Question?> {
        override fun map(s: JSONObject): Question? {
            val questionMessage = s.getString("question")
            val category = s.getString("category")

            val questionTypeObject = s.getJSONObject("question_type")

            val questionType = questionTypeMapper.map(questionTypeObject)

            return questionType?.let {
                Question(questionMessage, categoryTypeMapper.map(category), questionType)
            }
        }
    }

    val questionTypeMapper = object : Mapper<JSONObject, QuestionType?> {
        override fun map(s: JSONObject): QuestionType? {
            val questionTypeText = s.getString("type")

            return when {
                questionTypeText.equals("single_choice") -> {
                    singleChoiceQuestionMapper.map(s)
                }
                questionTypeText.equals("single_choice_conditional") -> {
                    singleChoiceConditionalQuestionMapper.map(s)
                }
                questionTypeText.equals("number_range") -> {
                    numberRangeQuestionMapper.map(s)
                }
                else -> null
            }
        }
    }

    val categoryTypeMapper = object : Mapper<String, CategoryType> {
        override fun map(s: String): CategoryType {
            return CategoryType.valueOf(s.uppercase())
        }
    }

    val categoriesMapper = object : Mapper<JSONArray, Categories> {
        override fun map(s: JSONArray): Categories {
            val list = arrayListOf<String>()
            for (i in 0 until s.length()) {
                list.add(s.getString(i))
            }
            return Categories(list)
        }
    }

    val singleChoiceQuestionMapper = object : Mapper<JSONObject, QuestionType.SingleChoice> {
        override fun map(s: JSONObject): QuestionType.SingleChoice {
            val options = s.getJSONArray("options")
            val optionsList = arrayToStringListMapper.map(options)

            return QuestionType.SingleChoice(optionsList)
        }
    }

    val singleChoiceConditionalQuestionMapper =
        object : Mapper<JSONObject, QuestionType.SingleChoiceConditional?> {
            override fun map(s: JSONObject): QuestionType.SingleChoiceConditional? {
                val options = s.getJSONArray("options")
                return conditionMapper.map(s.getJSONObject("condition"))?.let { comdition ->
                    QuestionType.SingleChoiceConditional(
                        options = arrayToStringListMapper.map(options),
                        condition = comdition
                    )
                }
            }
        }

    val numberRangeQuestionMapper = object : Mapper<JSONObject, QuestionType.NumberRange> {
        override fun map(s: JSONObject): QuestionType.NumberRange {
            return QuestionType.NumberRange(rangeParser.map(s.getJSONObject("range")))
        }
    }

    val rangeParser = object : Mapper<JSONObject, Range> {
        override fun map(s: JSONObject): Range {
            return Range(s.getInt("from"), s.getInt("to"))
        }
    }

    val arrayToStringListMapper = object : Mapper<JSONArray, List<String>> {
        override fun map(s: JSONArray): List<String> {
            val optionsList = arrayListOf<String>()
            for (i in 0 until s.length()) {
                val option = s.getString(i)
                optionsList.add(option)
            }

            return optionsList
        }
    }

    val conditionMapper = object : Mapper<JSONObject, Condition?> {
        override fun map(s: JSONObject): Condition? {
            val exactEquals = s.getJSONObject("predicate").getJSONArray("exactEquals")
            val ifPositiveObject = s.getJSONObject("if_positive")
            return ifPositiveMapper.map(ifPositiveObject)?.let { ifPositive ->
                Condition(
                    predicate = Predicate(arrayToStringListMapper.map(exactEquals)),
                    ifPositive = ifPositive
                )
            }
        }
    }

    val ifPositiveMapper = object : Mapper<JSONObject, IfPositive?> {
        override fun map(s: JSONObject): IfPositive? {
            val questionMessage = s.getString("question")
            val category = s.getString("category")

            val questionTypeObject = s.getJSONObject("question_type")

            val questionTypeText = questionTypeObject.getString("type")

            val questionType: QuestionType? = when {
                questionTypeText.equals("single_choice") -> {
                    singleChoiceQuestionMapper.map(questionTypeObject)
                }
                questionTypeText.equals("number_range") -> {
                    numberRangeQuestionMapper.map(questionTypeObject)
                }
                else -> null
            }

            val question = questionType?.let {
                Question(questionMessage, categoryTypeMapper.map(category), questionType)
            }

            return question?.let {
                IfPositive(it)
            }
        }
    }
}
