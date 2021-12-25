package com.example.model.database.dao

import androidx.room.*
import com.example.model.bean.PrivateFile
import com.example.model.bean.PrivateFileStatus

@Dao
interface PrivateFileDao {

    @Query("select * from private_file")
    fun getAll(): List<PrivateFile>

    @Query("select * from private_file where type = :type and status = :status")
    fun getFiles(type: Int, status: Int = PrivateFileStatus.Valid.value): List<PrivateFile>

    @Query("select * from private_file where type = 1 and status = :status")
    fun getAllImage(status: Int = PrivateFileStatus.Valid.value): List<PrivateFile>

    @Query("select * from private_file where type = 2 and status = :status")
    fun getAllVideo(status: Int = PrivateFileStatus.Valid.value): List<PrivateFile>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: PrivateFile)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<PrivateFile>)

    @Delete
    fun remove(item:PrivateFile)

    @Delete
    fun remove(list: List<PrivateFile>)

    @Query("delete from private_file")
    fun clear()
}