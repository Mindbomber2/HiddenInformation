package com.evacipated.cardcrawl.mod.hiddeninfo.extensions

import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenInfoMod

fun String.makeID(): String =
    HiddenInfoMod.makeID(this)

fun String.assetPath(): String =
    HiddenInfoMod.assetPath(this)
