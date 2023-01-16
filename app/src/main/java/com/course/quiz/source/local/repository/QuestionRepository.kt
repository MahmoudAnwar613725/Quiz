package com.course.quiz.source.local.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.course.quiz.data.model.Question
import com.course.quiz.source.local.dao.QuestionsDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class QuestionRepository(private val questionsDao: QuestionsDao) {
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
    val questionMLD = MutableLiveData<Question>()
    val questionCountMLD = MutableLiveData<Int>()


    suspend fun getQuestionByNo(questNo: Int) = withContext(defaultDispatcher) {
        questionMLD.postValue(questionsDao.getQuestionByNo(questNo))
    }

    // on below line we are creating an insert method
    // for adding the note to our database.
    suspend fun insert(question: Question) {
        questionsDao.insert(question)
    }

    // on below line we are creating a delete method
    // for deleting our note from database.
    suspend fun delete(question: Question) {
        questionsDao.delete(question)
    }

    // on below line we are creating a update method for
    // updating our note from database.
    suspend fun update(question: Question) {
        questionsDao.update(question)
    }

    suspend fun getQuestionsSize() {
        withContext(defaultDispatcher) {
            questionCountMLD.postValue(questionsDao.getQuestionsSize())
        }
    }
}