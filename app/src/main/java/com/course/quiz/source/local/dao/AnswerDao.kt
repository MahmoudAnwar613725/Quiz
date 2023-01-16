package com.course.quiz.source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.course.quiz.data.model.Answer

@Dao
interface AnswerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(answer: Answer)
    @Query("select count(questionNo) from UserAnswerTable where correctAnswer=userAnswer" )
    fun calcResult() : Int
    @Query("delete from UserAnswerTable")
    fun clearAnswers()


}