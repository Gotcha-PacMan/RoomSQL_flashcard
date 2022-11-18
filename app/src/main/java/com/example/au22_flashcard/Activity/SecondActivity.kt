package com.example.au22_flashcard.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.UserDictionary.Words.addWord
import android.widget.Button
import android.widget.EditText
import androidx.room.Room
import com.example.au22_flashcard.AppDatabase
import com.example.au22_flashcard.R
import com.example.au22_flashcard.Word.Word
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SecondActivity : AppCompatActivity(), CoroutineScope {

    lateinit var sweWords: EditText
    lateinit var engWords: EditText

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private lateinit var db: AppDatabase
    private lateinit var job: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)


        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "word-database")
            .fallbackToDestructiveMigration()
            .build()
        db = AppDatabase.getInstance(this)

        job = Job()

        sweWords = findViewById(R.id.swe_tv)
        engWords = findViewById(R.id.eng_tv)

        val addWords = findViewById<Button>(R.id.addWord_button)

        addWords.setOnClickListener {
            val newWord = Word(0, engWords.text.toString(), sweWords.text.toString())
            launch {
                addWord(newWord)
                finish()
            }
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun addWord(word: Word) {
        launch(Dispatchers.IO) {
            db.wordDao.insert(word)
        }

    }

}
