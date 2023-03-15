package com.evacipated.cardcrawl.mod.hiddeninfo.patches

import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenConfig
import com.evacipated.cardcrawl.mod.hiddeninfo.extensions.iz
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.megacrit.cardcrawl.events.GenericEventDialog
import com.megacrit.cardcrawl.helpers.FontHelper
import javassist.expr.ExprEditor
import javassist.expr.MethodCall

@SpirePatch2(
    clz = GenericEventDialog::class,
    method = "render"
)
object HideEventNames {
    @JvmStatic
    fun Instrument(): ExprEditor =
        object : ExprEditor() {
            override fun edit(m: MethodCall) {
                if (m.iz(FontHelper::class, "renderFontCentered")) {
                    m.replace(
                        "if (!${HideEventNames::class.qualifiedName}.hide()) {" +
                                "\$_ = \$proceed(\$\$);" +
                                "}"
                    )
                }
            }
        }

    @JvmStatic
    fun hide(): Boolean =
        HiddenConfig.eventNames
}
