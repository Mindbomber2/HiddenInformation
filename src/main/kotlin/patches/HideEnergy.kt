package com.evacipated.cardcrawl.mod.hiddeninfo.patches

import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenConfig
import com.evacipated.cardcrawl.mod.hiddeninfo.extensions.iz
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.ui.panels.EnergyPanel
import javassist.expr.ExprEditor
import javassist.expr.MethodCall

@SpirePatch2(
    clz = EnergyPanel::class,
    method = "render"
)
object HideEnergy {
    @JvmStatic
    fun Instrument(): ExprEditor =
        object : ExprEditor() {
            override fun edit(m: MethodCall) {
                if (m.iz(FontHelper::class, "renderFontCentered")) {
                    m.replace(
                        "if (${HideEnergy::class.qualifiedName}.hide()) {" +
                                "\$3 = \"?\";" +
                                "}" +
                                "\$_ = \$proceed(\$\$);"
                    )
                }
            }
        }

    @JvmStatic
    fun hide(): Boolean =
        HiddenConfig.playerEnergy
}
