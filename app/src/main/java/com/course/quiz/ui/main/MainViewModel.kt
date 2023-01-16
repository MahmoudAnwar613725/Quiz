package com.course.quiz.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.course.quiz.data.model.Answer
import com.course.quiz.data.model.Question
import com.course.quiz.data.model.QuestionAnswers
import com.course.quiz.data.model.QuestionAnswersVu
import com.course.quiz.source.local.QuizDatabase
import com.course.quiz.source.local.repository.AnswerRepository
import com.course.quiz.source.local.repository.QuestionAnswersRepository
import com.course.quiz.source.local.repository.QuestionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val questionAnswers: LiveData<List<QuestionAnswers>>
    val question: MutableLiveData<Question>
    val questionSize: MutableLiveData<Int>
    private val repositoryQuestion: QuestionRepository
    private val repositoryQuestionAnswers: QuestionAnswersRepository
    private val repositoryAnswer: AnswerRepository
    var currentQuestionAnswers = ArrayList<QuestionAnswers>()
    lateinit var currentQuestion: Question

    var currentQuestionIndex: Int = 0

    var resetStatus = MutableLiveData<Boolean>()
    var liveDataResult: MutableLiveData<Int>

    init {
        val daoQuestion = QuizDatabase.getDatabase(application).getQuestionsDao()
        val daoQuestAnswer = QuizDatabase.getDatabase(application).getQuestionAnswerDao()
        val daoAnswer = QuizDatabase.getDatabase(application).getAnswersDao()
        repositoryQuestion = QuestionRepository(daoQuestion)
        repositoryQuestionAnswers = QuestionAnswersRepository(daoQuestAnswer)
        repositoryAnswer = AnswerRepository(daoAnswer)
        questionAnswers = repositoryQuestionAnswers.liveDataQuestionAnswers
        question = repositoryQuestion.questionMLD
        questionSize = repositoryQuestion.questionCountMLD
        liveDataResult = repositoryAnswer.liveDataResult

    }

    fun loadNextQuestion() {
        currentQuestionIndex++
        getQuestionByIndex(currentQuestionIndex)

    }

    fun loadPreviousQuestion() {
        currentQuestionIndex--
        getQuestionByIndex(currentQuestionIndex)

    }

    fun getAnswerByQuestion(questionNo: Int) {
        // Create a new coroutine on the UI thread
        viewModelScope.launch {
            repositoryQuestionAnswers.getAnswerByQuestId(questionNo)
        }
    }

    fun getQuestionByIndex(index: Int) {
        viewModelScope.launch {
            repositoryQuestion.getQuestionByNo(index)
        }
    }

    fun resetExam() {
        currentQuestionIndex = 0
        resetAnswers()
    }

    private fun resetAnswers() {
        viewModelScope.launch {
            resetStatus.postValue(repositoryQuestionAnswers.resetAnsweres())
            repositoryAnswer.clearAnswers()

        }
    }


    fun updateUserSingleChoice(userAnswerIndex: Int) {
        resetPreviousAnswers()
        var currentQuestion = currentQuestion
        var userAnsewr = "-1"
        viewModelScope.launch {
            for ((index, _) in currentQuestionAnswers.withIndex()) {
                if (userAnswerIndex == index) {
                    currentQuestionAnswers.get(userAnswerIndex).selected = true
                    userAnsewr = index.toString()
                }
            }
            repositoryQuestionAnswers.updateCurrentQuestion(currentQuestionAnswers)
            var anser =
                Answer(currentQuestion.questionNo, currentQuestion.correctAnswer, userAnsewr)
            repositoryAnswer.saveUserAnswer(anser)
        }
    }

    private fun resetPreviousAnswers() {
        currentQuestionAnswers.forEach() {
            it.selected = false
        }
        viewModelScope.launch {
            repositoryQuestionAnswers.updateCurrentQuestion(currentQuestionAnswers)
        }
    }

    fun updateUserMultipleChoice(selectedAnswers: IntArray) {
        resetPreviousAnswers()
        var currentQuestion = currentQuestion
        var userAnsewr =
            selectedAnswers.contentToString().replace("]", "").replace("[", "").replace(",", "")
                .replace(" ", "");
        viewModelScope.launch {
            selectedAnswers.forEach {
                if (it != 0) {
                    for ((index, _) in currentQuestionAnswers.withIndex()) {
                        if (it-1 == index)
                            currentQuestionAnswers.get(it-1).selected = true
                    }
                }
            }
            var anser =
                Answer(currentQuestion.questionNo, currentQuestion.correctAnswer, userAnsewr)
            repositoryAnswer.saveUserAnswer(anser)
            repositoryQuestionAnswers.updateCurrentQuestion(currentQuestionAnswers)

        }
    }

    fun getQuestionsCount() {
        viewModelScope.launch {
            repositoryQuestion.getQuestionsSize()
        }
    }

    fun submitQuiz() {
        viewModelScope.launch {
            repositoryAnswer.calcResult()
        }
    }


    //implement insert , update and delete questions methods for admin user

}