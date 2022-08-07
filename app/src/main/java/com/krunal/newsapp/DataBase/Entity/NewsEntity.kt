package com.krunal.newsapp.DataBase.Entity

import androidx.room.*
import com.krunal.newsapp.DataBase.TypeConverter.DateTypeConverter
import java.util.*

@Entity(tableName = "News")
@TypeConverters(DateTypeConverter::class)
data class NewsEntity(
    @PrimaryKey()
    @ColumnInfo(name = "id")
    var id: Int?,
    @ColumnInfo(name = "source_id")
    var source_id: String?,
    @ColumnInfo(name = "source_name")
    var source_name: String?,
    @ColumnInfo(name = "author")
    var author: String?,
    @ColumnInfo(name = "title")
    var title: String?,
    @ColumnInfo(name = "description")
    var description: String,
    @ColumnInfo(name = "url")
    var url: String?,
    @ColumnInfo(name = "urlToImage")
    var urlToImage: String?,

    @ColumnInfo(name = "publishedAt")
    var publishedAt: Date?,
    @ColumnInfo(name = "content")
    var content: String?
) {


}
