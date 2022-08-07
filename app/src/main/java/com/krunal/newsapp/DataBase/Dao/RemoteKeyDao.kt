package com.krunal.newsapp.DataBase.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.krunal.newsapp.DataBase.Entity.RemoteEntity

@Dao
interface RemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRemote(list: List<RemoteEntity>)

    @Query("SELECT * FROM RemoteKey WHERE id = :id")
    fun getRemoteKeys(id:String) : RemoteEntity

    @Query("DELETE FROM RemoteKey")
    fun clearAll()

}