package com.example.album.extensions

import android.app.Activity
import androidx.fragment.app.Fragment

import com.lipeng.utils.extensions.printFunctionInfo

// 这里offsetIndex默认值为1是因为扩展对其内部的带默认值的函数进行了一次封装
// 外部调用带默认值的扩展函数首先调用的是扩展的method@default, 再由该函数调用扩展内部的真正的method
// 比如调用Activity.printFunctionInfo, 首先调用的是Activity.printFunctionInfo@default,
// 再由Activity.printFunctionInfo@default调用Activity.printFunctionInfo
fun Activity.printFunctionInfo(extra: String = "", offsetIndex: Int = 1) {
    //printFunctionInfo("Android Debug Activity", extra, offsetIndex+1)
    printFunctionInfo("", extra, offsetIndex+1)
}

// 这里offsetIndex默认值为1是因为扩展对其内部的带默认值的函数进行了一次封装
// 外部调用带默认值的扩展函数首先调用的是扩展的method@default, 再由该函数调用扩展内部的真正的method
// 比如调用Fragment.printFunctionInfo, 首先调用的是Fragment.printFunctionInfo@default,
// 再由Fragment.printFunctionInfo@default调用Fragment.printFunctionInfo
fun Fragment.printFunctionInfo(extra: String = "", offsetIndex: Int = 1) {
    //printFunctionInfo("Android Debug Fragment", extra, offsetIndex+1)
    printFunctionInfo("", extra, offsetIndex+1)
}