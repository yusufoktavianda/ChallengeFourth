package binar.academy.challengefourthsample.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import binar.academy.challengefourthsample.data.Notes
import binar.academy.challengefourthsample.data.User

@Database(entities = [Notes::class,User::class], version = 1)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao
    abstract fun userDao():UserDao

    companion object{
        private var INSTANCE: NotesDatabase? = null

        fun getInstance(context: Context): NotesDatabase? {
            if(INSTANCE == null){
                synchronized(NotesDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        NotesDatabase::class.java,"Appdata.db").build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance(){
            INSTANCE = null
        }
    }
}