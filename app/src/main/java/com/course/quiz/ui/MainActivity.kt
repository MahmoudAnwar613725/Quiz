package com.course.quiz.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.course.quiz.R
import com.course.quiz.data.model.Answer
import com.course.quiz.data.model.Question
import com.course.quiz.data.model.QuestionAnswers
import com.course.quiz.data.model.QuestionType
import com.course.quiz.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var currentQuestionIndex = 0
    private lateinit var currentQuestion: Question

    val questions = listOf(
        Question(1, " Which of following is used to handle null exceptions in Kotlin?", QuestionType.SINGLE, "2"),
        Question(2, " Which file extension is used to save Kotlin files.", QuestionType.SINGLE, "2"),
        Question(3, "All classes in Kotlin classes are by default?", QuestionType.SINGLE, "0"),
        Question(4, " Which of the followings constructors are available in Kotlin?", QuestionType.MULTIPLE, "1100"),
        Question(5, " Which of the following extension methods are used in Kotlin?", QuestionType.MULTIPLE, "1111"),
        Question(6, "Kotlin is a statically-typed programming language which runs on the?", QuestionType.SINGLE, "1"),
        Question(7, "How can you declare a variable in Kotlin?", QuestionType.SINGLE, "3"),
        Question(8, "How do you insert COMMENTS in Kotlin code?", QuestionType.MULTIPLE, "1010"),
    )

    private val questionAnswers = listOf(
        QuestionAnswers(
            1,
            listOf(
                Answer("Range", false),
                Answer("Sealed Class", false),
                Answer("Elvis Operator", false),
                Answer("Lambda function", false)
            )
        ),
        QuestionAnswers(
            2,
            listOf(
                Answer(".java", false),
                Answer(".kot", false),
                Answer(".kt or .kts", false),
                Answer(".android", false)
            )
        ),
        QuestionAnswers(
            3,
            listOf(
                Answer("public", false),
                Answer("final", false),
                Answer("sealed", false),
                Answer("abstract", false)
            )
        ),
        QuestionAnswers(
            4,
            listOf(
                Answer("Primary constructor", false),
                Answer("Secondary constructor", false),
                Answer("abstract constructor", false),
                Answer("None of the above", false)
            )
        ),
        QuestionAnswers(
            5,
            listOf(
                Answer("Read texts ()", false),
                Answer("Buffer reader ()", false),
                Answer("Read each line ()", false),
                Answer("Headlines ()", false)
            )
        ),
        QuestionAnswers(
            6,
            listOf(
                Answer("JCM", false),
                Answer("JVM", false),
                Answer("JPM", false),
                Answer("JDM", false)
            )
        ),
        QuestionAnswers(
            7,
            listOf(
                Answer("value my_var: Char", false),
                Answer("value Char : my_var", false),
                Answer("my_var: Char", false),
                Answer("value my_var: Char", false)
            )
        ),
        QuestionAnswers(
            8,
            listOf(
                Answer("/* This is a comment ", false),
                Answer("# This is a comment", false),
                Answer("// This is a comment ", false),
                Answer("-- This is a comment", false)
            )
        ),
    )

    private val answers: HashMap<Int, String> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.toolbar.setTitle("Quiz no 1")
        loadQuestionNo(0)
        initProgress()
        binding.btnNextEnd.setOnClickListener {

            if (currentQuestionIndex == 6)
                binding.btnNextEnd.text = "End"

            saveCurrentQuestionAnswer()
            if (currentQuestionIndex == 7)//reach last question
            {
                currentQuestionIndex++
                updateProgress()
                submitQuizAndFinish()
            } else {
                loadQuestionNo(++currentQuestionIndex)
            }

        }

        binding.btnPrevious.setOnClickListener {
            if (currentQuestionIndex == 0)
                return@setOnClickListener
            if (currentQuestionIndex == 7)
                binding.btnNextEnd.text = "Next"
            loadQuestionNo(--currentQuestionIndex)
        }


    }

    private fun submitQuizAndFinish() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Submit Quiz")
        builder.setMessage("Are you sure you want to submit your quiz")

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            finishQuiz()
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->

            dialog.cancel()
        }


        builder.show()
    }

    private fun initProgress() {

        binding.progress.max = questions.size
    }

    private fun updateProgress() {
        binding.progress.progress = currentQuestionIndex
    }

    private fun saveCurrentQuestionAnswer() {
        try {
            var userAnswer = ""

            if (currentQuestion.questionType == QuestionType.SINGLE) {
                val dlayout = binding.dynamicContent.findViewById<RadioGroup>(R.id.rdgrpAnswers)
                val selectedAnswer = dlayout.checkedRadioButtonId
                val view: View = dlayout.findViewById<RadioGroup>(selectedAnswer)
                val index = dlayout.indexOfChild(view)
                userAnswer = index.toString();

            } else {
                userAnswer += if (binding.dynamicContent.findViewById<CheckBox>(R.id.chkbxAnswer1).isChecked) "1" else "0"
                userAnswer += if (binding.dynamicContent.findViewById<CheckBox>(R.id.chkbxAnswer2).isChecked) "1" else "0"
                userAnswer += if (binding.dynamicContent.findViewById<CheckBox>(R.id.chkbxAnswer3).isChecked) "1" else "0"
                userAnswer += if (binding.dynamicContent.findViewById<CheckBox>(R.id.chkbxAnswer4).isChecked) "1" else "0"

            }
            saveUiPosition(userAnswer)
            answers.put(currentQuestionIndex, userAnswer)
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveUiPosition(userAnswer: String) {
        if (currentQuestion.questionType == QuestionType.SINGLE) {

            questionAnswers.find { it.questionNo == currentQuestion.questionNo }?.apply {
                this.answer.forEach { it.selected = false } //reset previous answer
                this.answer[userAnswer.toInt()].selected = true
            }
        } else {

            questionAnswers.find { it.questionNo == currentQuestion.questionNo }?.apply {
                this.answer.forEach { it.selected = false } //reset previous answer

                for ((i, v) in userAnswer.withIndex()) {
                    this.answer[i].selected = v == '1'
                }

            }
        }
    }

    private fun loadQuestionNo(index: Int) {
        try {
            var question = questions.find {
                it.questionNo == index + 1
            }
            if (question != null) {
                currentQuestion = question
            } else {
                Toast.makeText(this, "Error loading question", Toast.LENGTH_SHORT).show()
                return
            }
            updateProgress()
            var questionLayout: Int
            when (question.questionType) {
                QuestionType.SINGLE -> {
                    questionLayout = R.layout.single_question
                    //set question's answers
                }
                QuestionType.MULTIPLE -> {
                    questionLayout = R.layout.multiple_question

                }
            }
            // get ahold of the instance of your layout
            var dynamicContent = findViewById<LinearLayout>(R.id.dynamic_content)
// assuming your Wizard content is in content_wizard.xml
            dynamicContent.removeAllViews()
            var wizardView = layoutInflater.inflate(questionLayout, dynamicContent, false)
// add the inflated View to the layout

            dynamicContent.addView(wizardView);


            wizardView.findViewById<TextView>(R.id.txtQuestion).text ="  ${(currentQuestionIndex+1)} )  ${currentQuestion.questionText}"
            //set question's answers
            val questionAnswer = getAnswers(currentQuestion.questionNo)
            if (questionAnswer == null) {
                Toast.makeText(this, "No answer is exist", Toast.LENGTH_SHORT).show()
                return
            }
            when (question.questionType) {
                QuestionType.SINGLE -> {

                    wizardView.findViewById<RadioButton>(R.id.rdbtnAnswer1).text =
                        questionAnswer.answer[0].answer
                    wizardView.findViewById<RadioButton>(R.id.rdbtnAnswer1).isChecked =
                        questionAnswer.answer[0].selected

                    wizardView.findViewById<RadioButton>(R.id.rdbtnAnswer2).text =
                        questionAnswer.answer[1].answer
                    wizardView.findViewById<RadioButton>(R.id.rdbtnAnswer2).isChecked =
                        questionAnswer.answer[1].selected

                    wizardView.findViewById<RadioButton>(R.id.rdbtnAnswer3).text =
                        questionAnswer.answer[2].answer
                    wizardView.findViewById<RadioButton>(R.id.rdbtnAnswer3).isChecked =
                        questionAnswer.answer[2].selected

                    wizardView.findViewById<RadioButton>(R.id.rdbtnAnswer4).text =
                        questionAnswer.answer[3].answer
                    wizardView.findViewById<RadioButton>(R.id.rdbtnAnswer4).isChecked =
                        questionAnswer.answer[3].selected


                }
                QuestionType.MULTIPLE -> {
                    wizardView.findViewById<CheckBox>(R.id.chkbxAnswer1).text =
                        questionAnswer.answer[0].answer

                    wizardView.findViewById<CheckBox>(R.id.chkbxAnswer1).isChecked =
                        questionAnswer.answer[0].selected

                    wizardView.findViewById<CheckBox>(R.id.chkbxAnswer2).text =
                        questionAnswer.answer[1].answer
                    wizardView.findViewById<CheckBox>(R.id.chkbxAnswer2).isChecked =
                        questionAnswer.answer[1].selected


                    wizardView.findViewById<CheckBox>(R.id.chkbxAnswer3).text =
                        questionAnswer.answer[2].answer
                    wizardView.findViewById<CheckBox>(R.id.chkbxAnswer3).isChecked =
                        questionAnswer.answer[2].selected

                    wizardView.findViewById<CheckBox>(R.id.chkbxAnswer4).text =
                        questionAnswer.answer[3].answer
                    wizardView.findViewById<CheckBox>(R.id.chkbxAnswer4).isChecked =
                        questionAnswer.answer[3].selected
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }

    }


    private fun getAnswers(questionNo: Int): QuestionAnswers? {
        var questionAnswers = questionAnswers.find {
            it.questionNo == questionNo
        }

        return questionAnswers
    }

    private fun finishQuiz() {
        var finalGrade = 0
        questions.forEach {
            if (answers[it.questionNo - 1] == it.correctAnswer) {
                finalGrade += 50
            }
        }

        showResults(finalGrade)
        Toast.makeText(this, "your grade is $finalGrade", Toast.LENGTH_SHORT).show()
    }

    private fun showResults(finalGrade: Int) {
        // get ahold of the instance of your layout
        var dynamicContent = findViewById<LinearLayout>(R.id.dynamic_content)
// assuming your Wizard content is in content_wizard.xml
        dynamicContent.removeAllViews()
        var wizardView = layoutInflater.inflate(R.layout.final_grade, dynamicContent, false)
// add the inflated View to the layout

        val simpleDate = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = simpleDate.format(Date())

        wizardView.findViewById<TextView>(R.id.tvScore).text = "your score is : ${finalGrade}"
        wizardView.findViewById<TextView>(R.id.tvStatus).text =
            "you have submitted the quiz on $currentDate and your result is"

        dynamicContent.addView(wizardView);

        binding.btnNextEnd.visibility = View.GONE
        binding.btnPrevious.visibility = View.GONE

        wizardView.findViewById<TextView>(R.id.btnReset).setOnClickListener {
            currentQuestionIndex = 0
            answers.clear()
            resetQuationsAnswer()
            updateProgress()
            binding.btnNextEnd.text = "Next"
            binding.btnNextEnd.visibility = View.VISIBLE
            binding.btnPrevious.visibility = View.VISIBLE
            loadQuestionNo(0)

        }

    }

    private fun resetQuationsAnswer() {
        questionAnswers.forEach {
            it.answer.forEach {
                it.selected = false
            }
        }
    }
}


