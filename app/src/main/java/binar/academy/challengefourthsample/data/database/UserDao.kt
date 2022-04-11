package binar.academy.challengefourthsample.data.database

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import binar.academy.challengefourthsample.data.User

@Dao
interface UserDao {
    @Query("SELECT * FROM User where username = :username")
    fun getAccount (
        username: String?=null
    ):List<User>
    @Insert(onConflict = REPLACE)
    fun insertUser(user: User):Long

}