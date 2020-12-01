package com.example.model.database.dao

import androidx.room.*
import com.example.model.bean.PrivateImage

@Dao
interface PrivateImageDao {

    @Query("select * from private_image")
    fun getAll(): List<PrivateImage>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: PrivateImage)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<PrivateImage>)

    @Delete
    fun remove(item:PrivateImage)

    @Delete
    fun remove(list: List<PrivateImage>)

    @Query("delete from private_image")
    fun clear()
}