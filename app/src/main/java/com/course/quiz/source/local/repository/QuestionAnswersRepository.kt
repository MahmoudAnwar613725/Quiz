package com.course.quiz.source.local.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.course.quiz.data.model.AnalyzeQuestion
import com.course.quiz.data.model.Question
import com.course.quiz.data.model.QuestionAnswers
import com.course.quiz.data.model.QuestionAnswersVu
import com.course.quiz.source.local.dao.QuestionAnswersDao
import com.course.quiz.source.local.dao.QuestionsAnswersVuDao
import com.course.quiz.source.local.dao.QuestionsDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
 import kotlin.collections.ArrayList

class QuestionAnswersRepository(private val questionAnswersDao: QuestionAnswersDao) {
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
    var liveDataQuestionAnswers = MutableLiveData<List<QuestionAnswers>>()
    var liveDataCorrectAnswer = MutableLiveData<List<AnalyzeQuestion>>()
    var liveDataUserAnswer = MutableLiveData<List<AnalyzeQuestion>>()

    suspend fun getAnswerByQuestId(questno: Int) = withContext(defaultDispatcher) {
        liveDataQuestionAnswers.postValue(questionAnswersDao.getAnswerByQuestion(questno))
    }

    suspend fun resetAnsweres(): Boolean {
        withContext(defaultDispatcher) {
            questionAnswersDao.resetAnswers()

        }
        return true
    }

    suspend fun updateCurrentQuestion(currentQuestionAnswers: ArrayList<QuestionAnswers>) {
        withContext(defaultDispatcher) {
            try {
                questionAnswersDao.update(currentQuestionAnswers)
            } catch (e: Exception) {
            }
        }
    }


    fun getAllAnswer() : List<Question>  {

        return      questionAnswersDao.getAllAnswers()

    }

    fun getCorrectAnswerByQuestNo(questionNo: Int): List<AnalyzeQuestion> {

        return questionAnswersDao.getCorrectQuestionAnswers(questionNo)

    }

    fun getUserAnswerByQuestNo(questionNo: Int): List<AnalyzeQuestion> {
        return questionAnswersDao.getUserCorrectAnswers(questionNo)

    }
}