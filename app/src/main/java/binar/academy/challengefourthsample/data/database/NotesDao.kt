package binar.academy.challengefourthsample.data.database

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import binar.academy.challengefourthsample.data.Notes

@Dao
interface NotesDao {
    @Query("SELECT * FROM Notes")
    fun getAllNotes(): List<Notes>

    @Insert(onConflict = REPLACE)
    fun insertNotes(notes: Notes):Long

    @Update
    fun updateNotes(notes: Notes):Int

    @Delete
    fun deleteNotes(notes: Notes):Int
}