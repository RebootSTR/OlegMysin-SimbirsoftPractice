package o.mysin.simbirsoftappjava.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class Category(
    @PrimaryKey
    val id: Int,
    val title: String,
    @ColumnInfo("icon_url") val iconUrl: String
)
