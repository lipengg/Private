package com.example.model.bean

import androidx.room.ColumnInfo

class BaseImage(
    val filename : String,
    val directory : String,
    var selected: Boolean
) {
}