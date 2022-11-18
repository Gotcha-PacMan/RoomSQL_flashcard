package com.example.au22_flashcard.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import androidx.room.Room
import com.example.au22_flashcard.AppDatabase
import com.example.au22_flashcard.R
import com.example.au22_flashcard.Word.Word
import com.example.au22_flashcard.Word.WordList
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope{

    // Send to add words activity!
    lateinit var addWords : Button

    lateinit var wordView : TextView
    var currentWord : Word? = null
    val wordList = WordList()

    lateinit var db : AppDatabase
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private lateinit var job: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "WordList!"
        )
            .fallbackToDestructiveMigration()
            .build()
        db = AppDatabase.getInstance(this)

        job = Job()

        wordView = findViewById(R.id.wordTextView)
        showNewWord()

        wordView.setOnClickListener {
            revealTranslation()
        }

        addWords = findViewById(R.id.add_button)
        addWords.setOnClickListener(){
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    override fun onResume() {
        super.onResume()
        wordList.clearList()
        wordList.initializeWords()
        launch {
            val newWordList = getAllWords()
            val list = newWordList.await()
            addNewWords(list)
        }
    }

    fun getAllWords(): Deferred<List<Word>> =
        async (Dispatchers.IO){
            db.wordDao.getAllWords()
        }

    fun addNewWords(list:List<Word>) {
        for (word in list) {
            wordList.addWord(word)
        }
    }

        fun revealTranslation() {

        wordView.text = currentWord?.english

    }


    fun showNewWord() {

        currentWord = wordList.getNewWord()
        wordView.text = currentWord?.swedish

    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (event?.action == MotionEvent.ACTION_UP) {
            showNewWord()
        }

        return true
    }
}


//Vad ska göras:

//1. skapa en ny aktivitet där ett nytt ord får skrivas in
//2. spara det nya ordet i databasen.

//3. I main activity läs in alla ord från databasen

// (anväd coroutiner när ni läser och skriver till databasen se tidigare exempel)

