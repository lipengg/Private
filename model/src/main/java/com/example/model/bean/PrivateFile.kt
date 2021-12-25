package com.example.model.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lipeng.utils.CommonUtil
import java.util.*


enum class PrivateFileType(val value: Int){
    IMAGE(1), VIDEO(2), TEXT(3)
//
//    private val innerValues = values()
//
//    fun getValue(value: Int) = innerValues.firstOrNull { it.value == value }
}

enum class PrivateFileStatus(val value: Int){
    Invalid(0), Valid(1)
}

@Entity(tableName = "private_file")
class PrivateFile(@ColumnInfo(name="id") @PrimaryKey(autoGenerate = true) val id : Int,
                   @ColumnInfo(name="name") val name : String,
                  @ColumnInfo(name="type") val type : Int,
                  @ColumnInfo(name="status") val status : Int,
                   @ColumnInfo(name="origin_name") val originName : String,
                   @ColumnInfo(name="origin_path") val originPath : String,
                   var selected: Boolean
) {
    fun getFilePath(): String {
        return CommonUtil.getPrivateDirectory() + name
    }

    fun getFileType(): PrivateFileType {
        return PrivateFileType.valueOf(type.toString())
    }

    fun getFileStatus(): PrivateFileStatus {
        return PrivateFileStatus.valueOf(status.toString())
    }
}