package com.lipeng.utils.extensions

import android.util.Log


// 堆栈中位置0存储的是dalvik.system.VMStack.getThreadStackTrace(Native Method)
// 堆栈中位置1存储的是java.lang.Thread.getStackTrace
// 堆栈中位置2存储的是当前函数信息, 比如在Any.getMethodName中调用Thread.currentThread ().stackTrace,
//     获取到的堆栈信息中, 位置2存储的就是getMethodName的相关信息
// 堆栈中位置3存储的是调用函数信息, 比如你在Any.printFunctionInfo中调用getMethodName, 存储的就是Any.printFunctionInfo的信息
const val CALL_METHOD_STACK_TRACE_INDEX = 3

// offsetIndex是堆栈位置的偏移索引值, 如果你不是要获取调用Any.getMethodName的函数的信息, 而是把Any.getMethodName再次封装
// 打印更上层的函数的信息, 这里需要添加对应的上层函数的在堆栈中的索引偏移值
fun Any.getMethodName(offsetIndex: Int = 0):String {
    var index: Int = CALL_METHOD_STACK_TRACE_INDEX + offsetIndex
    var stacktrace = Thread.currentThread ().stackTrace;
    if (stacktrace.size > index) {
        return stacktrace[index].methodName
    }
    return "unknownMethod"
}

// 调试跟踪代码用, 打印当前类和函数名称信息
// 这里offsetIndex默认值为1是因为扩展对其内部的带默认值的函数进行了一次封装
// 外部调用带默认值的扩展函数首先调用的是扩展的method@default, 再由该函数调用扩展内部的真正的method
// 比如调用Any.printFunctionInfo, 首先调用的是printFunctionInfo@default
// 再由printFunctionInfo@default调用printFunctionInfo
fun Any.printFunctionInfo(prefix: String = "", extra: String = "", offsetIndex: Int = 1) {
    var log = ""
    if (prefix.isNotEmpty()) {
        log += "$prefix "
    }
    log += (javaClass.simpleName + " " + getMethodName(offsetIndex+1))
    if (extra.isNotEmpty()) {
        log +=" $extra"
    }
    Log.i("Function", log)
}