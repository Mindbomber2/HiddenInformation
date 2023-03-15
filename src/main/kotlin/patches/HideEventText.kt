package com.evacipated.cardcrawl.mod.hiddeninfo.patches

import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenConfig
import com.evacipated.cardcrawl.mod.hiddeninfo.extensions.iz
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.evacipated.cardcrawl.modthespire.lib.SpirePatches2
import com.megacrit.cardcrawl.events.GenericEventDialog
import com.megacrit.cardcrawl.events.RoomEventDialog
import com.megacrit.cardcrawl.ui.DialogWord
import javassist.expr.ExprEditor
import javassist.expr.MethodCall

@SpirePatches2(
    SpirePatch2(
        clz = RoomEventDialog::class,
        method = "render"
    ),
    SpirePatch2(
        clz = GenericEventDialog::class,
        method = "render"
    )
)
object HideEventText {
    @JvmStatic
    fun Instrument(): ExprEditor =
        object : ExprEditor() {
            override fun edit(m: MethodCall) {
                if (m.iz(DialogWord::class, "render")) {
                    m.replace(
                        "if (!${HideEventText::class.qualifiedName}.hide()) {" +
                                "\$_ = \$proceed(\$\$);" +
                                "}"
                    )
                }
            }
        }

    @JvmStatic
    fun hide(): Boolean =
        HiddenConfig.eventText
}
