package com.evacipated.cardcrawl.mod.hiddeninfo.patches

import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenConfig
import com.evacipated.cardcrawl.mod.hiddeninfo.extensions.iz
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.ui.panels.TopPanel
import javassist.expr.ExprEditor
import javassist.expr.MethodCall

@SpirePatch2(
    clz = TopPanel::class,
    method = "renderGold"
)
object HidePlayerGold {
    @JvmStatic
    fun Instrument(): ExprEditor =
        object : ExprEditor() {
            override fun edit(m: MethodCall) {
                if (m.iz(FontHelper::class, "renderFontLeftTopAligned")) {
                    m.replace(
                        "if (!${HidePlayerGold::class.qualifiedName}.hide()) {" +
                                "\$_ = \$proceed(\$\$);" +
                                "}"
                    )
                }
            }
        }

    @JvmStatic
    fun hide(): Boolean =
        HiddenConfig.playerGold
}
