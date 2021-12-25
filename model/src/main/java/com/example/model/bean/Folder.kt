package com.example.model.bean

data class Folder(
        var id: Int,
        var dir: String,
        var firstFilePath: String,
        var dirName: String,
        var size: Int,
        var selected: Boolean,
        var type: Int //0全部 非0文件夹
) {
}