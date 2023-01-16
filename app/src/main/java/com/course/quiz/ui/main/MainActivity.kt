package com.course.quiz.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.course.quiz.R
import com.course.quiz.data.model.Question
import com.course.quiz.data.model.QuestionAnswers
import com.course.quiz.data.model.QuestionType
import com.course.quiz.databinding.ActivityMainBinding
import com.course.quiz.ui.analyze.AnalyzeResultActivity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    //  var currentQuestionIndex = 1
    lateinit var viewModal: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.toolbar.setTitle("Quiz no 2")
        viewModal = ViewModelProvider(this).get(MainViewModel::class.java)

        observeQuestion()
        observerResetQuiz()
        viewModal.getQuestionsCount()
        viewModal.loadNextQuestion()
        initProgress()
        binding.btnNextEnd.setOnClickListener {
            saveCurrentQuestionAnswer()
            if (viewModal.questionSize.value != viewModal.currentQuestionIndex) {
                viewModal.loadNextQuestion()
                if (viewModal.questionSize.value == viewModal.currentQuestionIndex) //the next question is the last one
                    binding.btnNextEnd.text = "End"
            } else {
                submitQuizAndFinish()
            }


        }

        binding.btnPrevious.setOnClickListener {
            if (viewModal.currentQuestionIndex != 1) {
                if (viewModal.questionSize.value != (viewModal.currentQuestionIndex + 1))
                    binding.btnNextEnd.text = "Next"

                viewModal.loadPreviousQuestion()
            }
        }


    }

    private fun observerResetQuiz() {
        viewModal.resetStatus.observe(this) {
            viewModal.loadNextQuestion()
            binding.btnNextEnd.text = "Next"
            binding.btnNextEnd.visibility = View.VISIBLE
            binding.btnPrevious.visibility = View.VISIBLE
        }
    }


    private fun submitQuizAndFinish() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Submit Quiz")
        builder.setMessage("Are you sure you want to submit your quiz")

        builder.setPositiveButton(android.R.string.ok) { dialog, which ->
            finishQuiz()
        }

        builder.setNegativeButton(android.R.string.cancel) { dialog, which ->

            dialog.cancel()
        }


        builder.show()
    }

    private fun initProgress() {
        viewModal.questionSize.observe(this) {
            binding.progress.max = it

        }
    }

    private fun updateProgress() {
        binding.progress.progress = viewModal.currentQuestionIndex
    }

    private fun saveCurrentQuestionAnswer() {
        try {

            if (viewModal.currentQuestion.questionType == QuestionType.SINGLE) {
                val dlayout = binding.dynamicContent.findViewById<RadioGroup>(R.id.rdgrpAnswers)
                val selectedAnswer = dlayout.checkedRadioButtonId
                val view: View = dlayout.findViewById<RadioGroup>(selectedAnswer)
                val index = dlayout.indexOfChild(view)
                viewModal.updateUserSingleChoice(index)


            } else {
                var res = ""
                var selectedAnswers = IntArray(4)
                if (binding.dynamicContent.findViewById<CheckBox>(R.id.chkbxAnswer1).isChecked) selectedAnswers[0] =
                    1 else selectedAnswers[0] = 0
                if (binding.dynamicContent.findViewById<CheckBox>(R.id.chkbxAnswer2).isChecked) selectedAnswers[1] =
                    2 else selectedAnswers[1] = 0
                if (binding.dynamicContent.findViewById<CheckBox>(R.id.chkbxAnswer3).isChecked) selectedAnswers[2] =
                    3 else selectedAnswers[2] = 0
                if (binding.dynamicContent.findViewById<CheckBox>(R.id.chkbxAnswer4).isChecked) selectedAnswers[3] =
                    4 else selectedAnswers[3] = 0
                viewModal.updateUserMultipleChoice(selectedAnswers)

            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }


    private fun observeQuestion() {
        try {
            viewModal.question.observe(this) {
                if (it != null) {
                    viewModal.currentQuestion = it
                    updateProgress()
                    var questionLayout: Int = when (it.questionType) {
                        QuestionType.SINGLE -> {
                            R.layout.single_question
                            //set question's answers
                        }
                        QuestionType.MULTIPLE -> {
                            R.layout.multiple_question

                        }
                    }
                    // get ahold of the instance of your layout
                    var dynamicContent = findViewById<LinearLayout>(R.id.dynamic_content)
// assuming your Wizard content is in content_wizard.xml
                    dynamicContent.removeAllViews()
                    var wizardView = layoutInflater.inflate(questionLayout, dynamicContent, false)
// add the inflated View to the layout

                    dynamicContent.addView(wizardView);


                    wizardView.findViewById<TextView>(R.id.txtQuestion).text =
                        "  ${(viewModal.currentQuestionIndex)} )  ${viewModal.currentQuestion.questionText}"


                    //set question's answers
                    setQuestionAnsewers(it, wizardView)


                } else {
                    Toast.makeText(this, "Error loading question", Toast.LENGTH_SHORT).show()
                }

            }

        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }

    }

    private fun setQuestionAnsewers(question: Question, wizardView: View) {

        viewModal.getAnswerByQuestion(viewModal.currentQuestion.questionNo)
        viewModal.questionAnswers.observe(this) {
            if (it == null) {
                Toast.makeText(this, "No answer is exist", Toast.LENGTH_SHORT).show()
            } else {
                viewModal.currentQuestionAnswers.clear()
                viewModal.currentQuestionAnswers.addAll(it) //save current answers to update user's choice
                when (question.questionType) {
                    QuestionType.SINGLE -> {
                        wizardView.findViewById<RadioButton>(R.id.rdbtnAnswer1).text =
                            it[0].answerText
                        wizardView.findViewById<RadioButton>(R.id.rdbtnAnswer1).isChecked =
                            it[0].selected

                        wizardView.findViewById<RadioButton>(R.id.rdbtnAnswer2).text =
                            it[1].answerText
                        wizardView.findViewById<RadioButton>(R.id.rdbtnAnswer2).isChecked =
                            it[1].selected


                        wizardView.findViewById<RadioButton>(R.id.rdbtnAnswer3).text =
                            it[2].answerText
                        wizardView.findViewById<RadioButton>(R.id.rdbtnAnswer3).isChecked =
                            it[2].selected

                        wizardView.findViewById<RadioButton>(R.id.rdbtnAnswer4).text =
                            it[3].answerText
                        wizardView.findViewById<RadioButton>(R.id.rdbtnAnswer4).isChecked =
                            it[3].selected


                    }
                    QuestionType.MULTIPLE -> {

                        wizardView.findViewById<CheckBox>(R.id.chkbxAnswer1).text =
                            it[0].answerText

                        wizardView.findViewById<CheckBox>(R.id.chkbxAnswer1).isChecked =
                            it[0].selected

                        wizardView.findViewById<CheckBox>(R.id.chkbxAnswer2).text =
                            it[1].answerText

                        wizardView.findViewById<CheckBox>(R.id.chkbxAnswer2).isChecked =
                            it[1].selected

                        wizardView.findViewById<CheckBox>(R.id.chkbxAnswer3).text =
                            it[2].answerText

                        wizardView.findViewById<CheckBox>(R.id.chkbxAnswer3).isChecked =
                            it[2].selected

                        wizardView.findViewById<CheckBox>(R.id.chkbxAnswer4).text =
                            it[3].answerText

                        wizardView.findViewById<CheckBox>(R.id.chkbxAnswer4).isChecked =

                            it[3].selected


                    }
                }
            }
        }

    }


    private fun finishQuiz() {

        viewModal.submitQuiz()
        viewModal.liveDataResult.observe(this) {
            showResults(it)
        }
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
            viewModal.resetExam()


        }

        wizardView.findViewById<TextView>(R.id.btnAnalyze).setOnClickListener {


            val intent = Intent(this, AnalyzeResultActivity::class.java)
            startActivity(intent)
        }

    }


}


