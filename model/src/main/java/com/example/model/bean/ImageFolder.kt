package com.example.model.bean

data class ImageFolder(
    var id: Int,
    var dir: String,
    var firstImagePath: String,
    var dirName: String,
    var size: Int,
    var selected: Boolean,
    var type: Int //0全部 非0文件夹
) {
}