package com.course.quiz.source.local.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.course.quiz.data.model.Answer
import com.course.quiz.data.model.Question
import com.course.quiz.data.model.QuestionAnswers
import com.course.quiz.source.local.dao.AnswerDao
import com.course.quiz.source.local.dao.QuestionsDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AnswerRepository(private val answerDao: AnswerDao) {
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
    var liveDataResult = MutableLiveData<Int>()




    suspend fun saveUserAnswer(answer : Answer) = withContext(defaultDispatcher){
         answerDao.insert(answer)
    }

    suspend fun calcResult() {
        withContext(defaultDispatcher){
            liveDataResult.postValue(answerDao.calcResult())
        }
    }

   suspend fun clearAnswers() {
        withContext(defaultDispatcher){
            answerDao.clearAnswers()
        }
    }

}