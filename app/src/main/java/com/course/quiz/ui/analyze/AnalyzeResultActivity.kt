package com.course.quiz.ui.analyze

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.course.quiz.R
import com.course.quiz.adapter.AnalysisAdapter
import com.course.quiz.data.model.AnalyzeQuestion
import com.course.quiz.data.model.QuestionAnswers
import com.course.quiz.source.local.QuizDatabase
import com.course.quiz.source.local.repository.QuestionAnswersRepository

class AnalyzeResultActivity : AppCompatActivity() {
    private lateinit var   repositoryQuestionAnswers: QuestionAnswersRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analyze_result)
        // getting the recyclerview by its id
        val recyclerview = findViewById<RecyclerView>(R.id.rvResultAnalysis)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        // ArrayList of class ItemsViewModel

        // This loop will create 20 Views containing
        // the image with the count of view
        val daoQuestAnswer = QuizDatabase.getDatabase(application).getQuestionAnswerDao()
        repositoryQuestionAnswers   = QuestionAnswersRepository(daoQuestAnswer)
        var data   = repositoryQuestionAnswers.getAllAnswer()

        // This will pass the ArrayList to our Adapter
        val adapter = AnalysisAdapter(data,repositoryQuestionAnswers)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter

    }
}