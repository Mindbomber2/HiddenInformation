package com.evacipated.cardcrawl.mod.hiddeninfo.extensions

import javassist.expr.MethodCall
import kotlin.reflect.KClass

fun MethodCall.iz(clz: KClass<*>, name: String): Boolean =
    this.className == clz.java.name && this.methodName == name
