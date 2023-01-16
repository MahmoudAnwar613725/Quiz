package com.course.quiz.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.course.quiz.data.model.*
import com.course.quiz.source.local.dao.AnswerDao
import com.course.quiz.source.local.dao.QuestionAnswersDao
import com.course.quiz.source.local.dao.QuestionsAnswersVuDao
import com.course.quiz.source.local.dao.QuestionsDao
import com.course.quiz.util.ioThread

@Database(
    entities = [Question::class, Answer::class, QuestionAnswers::class],
    //views = [QuestionAnswersVu::class],
    version = 1,
    exportSchema = false
)
abstract class QuizDatabase : RoomDatabase() {
    abstract fun getQuestionsDao(): QuestionsDao
    abstract fun getQuestionAnswerDao(): QuestionAnswersDao
    abstract fun getAnswersDao(): AnswerDao
    //  abstract fun getQuestionAnswersVuDao(): QuestionsAnswersVuDao

    companion object {
        @Volatile
        private var Instance: QuizDatabase? = null

        fun getDatabase(context: Context): QuizDatabase {
            return Instance ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QuizDatabase::class.java,
                    "quiz_db"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // insert the data on the IO Thread
                            ioThread {
                                getDatabase(context).getQuestionsDao().insert(PREPOPULATE_QUESTIONS)
                                getDatabase(context).getQuestionAnswerDao()
                                    .insert(PREPOPULATE_QUESTIONS_ANSWERS)

                            }
                        }
                    }).allowMainThreadQueries().build()
                Instance = instance
                // return instance
                instance
            }

        }

        val PREPOPULATE_QUESTIONS = listOf(
            Question(
                1,
                " Which of following is used to handle null exceptions in Kotlin?",
                QuestionType.SINGLE,
                "2"
            ),
            Question(
                2,
                " Which file extension is used to save Kotlin files.",
                QuestionType.SINGLE,
                "2"
            ),
            Question(3, "All classes in Kotlin classes are by default?", QuestionType.SINGLE, "0"),
            Question(
                4,
                " Which of the followings constructors are available in Kotlin?",
                QuestionType.MULTIPLE,
                "1200"
            ),
            Question(
                5,
                " Which of the following extension methods are used in Kotlin?",
                QuestionType.MULTIPLE,
                "1234"
            ),
            Question(
                6,
                "Kotlin is a statically-typed programming language which runs on the?",
                QuestionType.SINGLE,
                "1"
            ),
            Question(7, "How can you declare a variable in Kotlin?", QuestionType.SINGLE, "3"),
            Question(
                8,
                "How do you insert COMMENTS in Kotlin code?",
                QuestionType.MULTIPLE,
                "1030"
            ),
            Question(
                9,
                "On which of the following, developers can test the application, during developing the android applications?",
                QuestionType.SINGLE,
                "3"
            ),
            Question(
                10,
                "What is the equivalent of Java static methods in Kotlin?",
                QuestionType.MULTIPLE,
                "1234"
            ),
            Question(
                11,
                "A(n)........... represents an object or concept, and its properties, to store in a database.",
                QuestionType.SINGLE,
                "0"
            ),
            Question(
                12,
                "Which tag is used to define themes?",
                QuestionType.SINGLE,
                "1"
            ),
            Question(
                13,
                "Which of the following are reasons for using Material Design components? Select all that apply.",
                QuestionType.MULTIPLE,
                "1230"
            ),
            Question(
                14,
                "Which of the following accessibility tools are available on Android devices?",
                QuestionType.MULTIPLE,
                "1200"
            ),
            Question(
                15,
                "Assume you have a TextView with a textSize attribute of 16sp. This TextView also has a style applied to it which sets textSize to 14sp. In addition," +
                        " there is also a theme in the app that sets textSize to 12sp. What is the actual textSize of the TextView that will be displayed on screen?",
                QuestionType.SINGLE,
                "3"
            ),
        )

        private val PREPOPULATE_QUESTIONS_ANSWERS = listOf(
            QuestionAnswers(
                1,
                "Range",
                false, false
            ),
            QuestionAnswers(
                1,
                "Sealed Class",
                false, false
            ),
            QuestionAnswers(
                1,
                "Elvis Operator",
                false, true
            ),
            QuestionAnswers(
                1,
                "Lambda function",
                false, false
            ),
            QuestionAnswers(
                2,
                "java", false, false

            ),
            QuestionAnswers(
                2,
                "kot", false, false
            ),
            QuestionAnswers(
                2,
                ".kt or .kts", false, true

            ),
            QuestionAnswers(
                2,
                ".android", false, false
            ),
            QuestionAnswers(
                3,
                "public", false, true
            ),
            QuestionAnswers(
                3,
                "final", false, true
            ),
            QuestionAnswers(
                3,
                "sealed",
                false, false
            ),
            QuestionAnswers(
                3,
                "abstract", false, false
            ),
            QuestionAnswers(
                4,
                "Primary constructor", false, true
            ),
            QuestionAnswers(
                4,
                "Secondary constructor", false, true
            ),
            QuestionAnswers(
                4,
                "abstract constructor", false, false
            ),
            QuestionAnswers(
                4,
                "None of the above", false, false
            ),
            QuestionAnswers(
                5,
                "Read texts ()", false, true
            ),
            QuestionAnswers(
                5,
                "Buffer reader ()", false, true
            ),
            QuestionAnswers(
                5,
                "Read each line ()", false, true
            ),
            QuestionAnswers(
                5,
                "Headlines ()", false, true
            ),
            QuestionAnswers(6, "JCM", false, true),
            QuestionAnswers(6, "JVM", false, false),
            QuestionAnswers(6, "JPM", false, false),
            QuestionAnswers(6, "JDM", false, false),
            QuestionAnswers(7, "value my_var: Char", false, false),
            QuestionAnswers(7, "value Char : my_var", false, false),
            QuestionAnswers(7, "my_var: Char", false, false),
            QuestionAnswers(7, "value my_var: Char", false, true),
            QuestionAnswers(8, "/* This is a comment", false, true),
            QuestionAnswers(8, "# This is a comment", false, false),
            QuestionAnswers(8, "// This is a comment", false, true),
            QuestionAnswers(8, "-- This is a comment", false, false),
            QuestionAnswers(9, "Third-party emulators", false, false),
            QuestionAnswers(9, "Physical android phone", false, false),
            QuestionAnswers(9, "Emulator included in Android SDK", false, false),
            QuestionAnswers(9, "All of the above", false, true),
            QuestionAnswers(10, "companion object", false, true),
            QuestionAnswers(10, "package-level function", false, true),
            QuestionAnswers(10, "object", false, true),
            QuestionAnswers(10, "all of the above", false, true),
            QuestionAnswers(11, "Entity", false, true),
            QuestionAnswers(11, "Object", false, false),
            QuestionAnswers(11, "Class", false, false),
            QuestionAnswers(11, "Row", false, false),
            QuestionAnswers(12, "<style>", false, false),
            QuestionAnswers(12, "<theme>", false, true),
            QuestionAnswers(12, "<meta-tag>", false, false),
            QuestionAnswers(12, "<styling>", false, false),
            QuestionAnswers(
                13,
                "They are designed to be beautiful, functional, and work together.",
                false,true
            ),
            QuestionAnswers(13, "They help you create an app that uses consistent styling", false,true),
            QuestionAnswers(13, "They help you make your app more accessible for all users", false,true),
            QuestionAnswers(
                13,
                "Android Studio will give you a warning if you are using a poor color scheme.",
                false,false
            ),
            QuestionAnswers(14, "TalkBack", false,true),
            QuestionAnswers(14, "Accessibility Scanner", false,true),
            QuestionAnswers(
                14,
                "In Android Studio, Refactor > Add RTL support where possible",
                false,false
            ),
            QuestionAnswers(14, "Lint", false,false),
            QuestionAnswers(15, "12sp)", false,false),
            QuestionAnswers(15, "14sp)", false,false),
            QuestionAnswers(15, "11sp)", false,false),
            QuestionAnswers(15, "16sp)", false,true),
        )


    }
}
