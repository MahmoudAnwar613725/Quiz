package com.course.quiz.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questionTable")
data class Question(@PrimaryKey(autoGenerate = true) var questionNo : Int=0, val questionText: String, val questionType :QuestionType, val correctAnswer : String){}
