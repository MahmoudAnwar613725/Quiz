package com.course.quiz.source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.course.quiz.data.model.QuestionAnswers
import com.course.quiz.data.model.QuestionAnswersVu


interface QuestionsAnswersVuDao {
   // @Query("Select * from QuestionAnswersVu where questionNo")
    fun getAllAnswers(): LiveData<List<QuestionAnswersVu>>

 //   @Query("Select * from QuestionAnswersVu where questionNo = :questNo")
    fun getAnswerByQuestion(questNo: Int): List<QuestionAnswersVu>
}