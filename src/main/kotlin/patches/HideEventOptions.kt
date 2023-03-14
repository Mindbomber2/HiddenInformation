package com.evacipated.cardcrawl.mod.hiddeninfo.patches

import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenConfig
import com.evacipated.cardcrawl.mod.hiddeninfo.extensions.iz
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.ui.buttons.LargeDialogOptionButton
import javassist.expr.ExprEditor
import javassist.expr.MethodCall

object HideEventOptions {
    @SpirePatch2(
        clz = LargeDialogOptionButton::class,
        method = "render"
    )
    object Text {
        @JvmStatic
        fun Instrument(): ExprEditor =
            object : ExprEditor() {
                override fun edit(m: MethodCall) {
                    if (m.iz(FontHelper::class, "renderSmartText")) {
                        m.replace(
                            "if (${HideEventOptions::class.qualifiedName}.hide()) {" +
                                    "\$3 = ${Text::class.qualifiedName}.truncateText(\$3);" +
                                    "}" +
                                    "\$_ = \$proceed(\$\$);"
                        )
                    }
                }
            }

        private val regex = Regex("^(\\[.+]).*$")
        @JvmStatic
        fun truncateText(msg: String): String {
            return if (HiddenConfig.eventOptions) {
                ""
            } else if (HiddenConfig.eventOptionsEffect) {
                regex.matchEntire(msg)?.let {
                    it.groupValues[1]
                } ?: ""
            } else {
                msg
            }
        }
    }

    @JvmStatic
    fun hide(): Boolean =
        HiddenConfig.eventOptionsEffect || HiddenConfig.eventOptions
}
