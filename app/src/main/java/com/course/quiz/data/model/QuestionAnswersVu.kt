package com.course.quiz.data.model

import androidx.room.DatabaseView

@DatabaseView(
    "SELECT questionTable.questionNo,questionTable.questionText, ,answerTable.id, answerTable.answer, answerTable.selected  FROM questionAnswerTable " +
            "INNER JOIN answerTable ON questionTable.questionNo = questionAnswerTable.questionNo"
)
class QuestionAnswersVu(val questionNo: Int, val answer: String, val id : Int ,val selected : Boolean) {
}