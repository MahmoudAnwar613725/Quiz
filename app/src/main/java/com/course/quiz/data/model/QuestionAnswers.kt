package com.course.quiz.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questionAnswerTable")
data class QuestionAnswers(
    val questionNo: Int,
    val answerText: String,
    var selected: Boolean,
    var isCorrectAnswer: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
