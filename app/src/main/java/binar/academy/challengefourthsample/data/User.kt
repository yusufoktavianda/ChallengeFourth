package binar.academy.challengefourthsample.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class User(
        @PrimaryKey(autoGenerate = true) var id : Int?=0,
        @ColumnInfo(name = "username") var username:String,
        @ColumnInfo(name = "email") var email:String,
        @ColumnInfo(name = "password") var password:String
):Parcelable