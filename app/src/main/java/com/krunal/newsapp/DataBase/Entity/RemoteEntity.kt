package com.krunal.newsapp.DataBase.Entity

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RemoteKey")
data class RemoteEntity(
    @PrimaryKey()
    @ColumnInfo(name = "id")
    var id :String,
    @ColumnInfo(name = "prevKey")
    @Nullable
    val prevKey:Int?,
    @ColumnInfo(name = "nextKey")
    @Nullable
    val nextKey:Int?)
