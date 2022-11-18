package com.example.au22_flashcard.Word

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.au22_flashcard.Word.Word

@Dao
interface WordDao {

    @Insert
    fun insert(word: Word)

    // delete
    @Delete
    fun delete(word: Word)

    // getAllwords
    @Query("SELECT * FROM word_table")
    fun getAllWords() : MutableList<Word>
}