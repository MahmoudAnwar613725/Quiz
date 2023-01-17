package com.course.quiz.source.local.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.course.quiz.data.model.*

@Dao
interface QuestionAnswersDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(questionAnswers: QuestionAnswers)

    @Insert
    fun insert(questionAnswers: List<QuestionAnswers>)

    @Query("select distinct  *    from   questionTable  ")
    fun getAllAnswers(): List<Question>

    @Query("Select * from questionAnswerTable where questionNo = :questNo")
    fun getAnswerByQuestion(questNo: Int): List<QuestionAnswers>

    @Query("update questionAnswerTable set selected = 0")
    fun resetAnswers()

    @Query("select  questionTable.questionNo,questionTable.questionText,answerText   from questionAnswerTable join questionTable on questionTable.questionNo = questionAnswerTable.questionNo  where isCorrectAnswer=1 and questionTable.questionNo  = :questionNo")
    fun getCorrectQuestionAnswers(questionNo:Int) : List<AnalyzeQuestion>

    @Query("select  questionTable.questionNo,questionTable.questionText,answerText   from questionAnswerTable join questionTable on questionTable.questionNo = questionAnswerTable.questionNo  where selected=1 and questionTable.questionNo  = :questionNo")
    fun getUserCorrectAnswers(questionNo:Int) : List<AnalyzeQuestion>
    @Update
    fun update(it: List<QuestionAnswers>)
}