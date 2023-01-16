package com.course.quiz.source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.course.quiz.data.model.Question

@Dao
interface QuestionsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(question: Question)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(question: List<Question>)

    @Delete
    suspend fun delete(question: Question)


    @Query("Select * from questionTable")
    fun getAllQuestions(): LiveData<List<Question>>

    // below method is use to update the note.
    @Update
      fun update(question: Question)
    @Query("Select * from questionTable where questionNo = :questNo")
       fun getQuestionByNo(questNo: Int): Question

    @Query("Select count(questionNo) from questionTable")
    fun getQuestionsSize(): Int
}