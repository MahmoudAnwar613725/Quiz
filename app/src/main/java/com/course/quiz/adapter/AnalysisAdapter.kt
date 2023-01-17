package com.course.quiz.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.course.quiz.R
import com.course.quiz.data.model.AnalyzeQuestion
import com.course.quiz.data.model.Question
import com.course.quiz.data.model.QuestionAnswers
import com.course.quiz.data.model.QuestionAnswersVu
import com.course.quiz.databinding.AnalyzeQuesitonItemBinding
import com.course.quiz.source.local.repository.QuestionAnswersRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AnalysisAdapter(
    private val dataSet: List<Question>,
    private val questionAnswersRepository: QuestionAnswersRepository
) :
    RecyclerView.Adapter<AnalysisAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: AnalyzeQuesitonItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    // inside the onCreateViewHolder inflate the view of SingleItemBinding
    // and return new ViewHolder object containing this layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            AnalyzeQuesitonItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    // bind the items with each item
    // of the list languageList
    // which than will be
    // shown in recycler view
    // to keep it simple we are
    // not setting any image data to view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {

            with(dataSet[holder.adapterPosition]) {
                binding.tvQuestion.text = "${this.questionNo})  ${this.questionText}"
                binding.tvCorrectAnswer.text="Correct Answer"
                binding.tvUserAnswer.text  ="Your Answer"
                    var correcAnser =
                        questionAnswersRepository.getCorrectAnswerByQuestNo(dataSet[holder.adapterPosition].questionNo)
                    correcAnser.forEach() {
                        binding.tvCorrectAnswer.text  = binding.tvCorrectAnswer.text.toString() +"\n"  + it.answerText
                    }

                    var userAnswer =
                        questionAnswersRepository.getUserAnswerByQuestNo(dataSet[holder.adapterPosition ].questionNo)
                    userAnswer.forEach() {
                        binding.tvUserAnswer.text  = binding.tvUserAnswer.text.toString()+"\n" + it.answerText
                    }




            }
        }
    }

    // return the size of languageList
    override fun getItemCount(): Int {
        return dataSet.size
    }
}
