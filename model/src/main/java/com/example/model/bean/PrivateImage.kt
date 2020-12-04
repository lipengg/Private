package com.example.model.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lipeng.utils.CommonUtil
import java.util.*

@Entity(tableName = "private_image")
class PrivateImage(@ColumnInfo(name="id") @PrimaryKey(autoGenerate = true) val id : Int,
                        @ColumnInfo(name="filename") val filename : String,
                        @ColumnInfo(name="origin_filename") val originFilename : String,
                        @ColumnInfo(name="origin_path") val originPath : String,
                        var selected: Boolean
) {
    fun getFilePath(): String {
        return CommonUtil.getPrivateDirectory() + filename
    }
}