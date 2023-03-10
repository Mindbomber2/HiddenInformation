package com.evacipated.cardcrawl.mod.hiddeninfo.patches

import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenConfig
import com.evacipated.cardcrawl.mod.hiddeninfo.extensions.iz
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.evacipated.cardcrawl.modthespire.lib.SpirePatches2
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.screens.SingleCardViewPopup
import javassist.expr.ExprEditor
import javassist.expr.MethodCall

@SpirePatches2(
    SpirePatch2(
        clz = AbstractCard::class,
        method = "renderTitle"
    ),
    SpirePatch2(
        clz = SingleCardViewPopup::class,
        method = "renderTitle"
    ),
)
object HideCardTitles {
    @JvmStatic
    fun Instrument(): ExprEditor =
        object : ExprEditor() {
            override fun edit(m: MethodCall) {
                if (m.iz(FontHelper::class, "renderRotatedText") || m.iz(FontHelper::class, "renderFontCentered")) {
                    m.replace(
                        "if (!${HideCardTitles::class.qualifiedName}.hide()) {" +
                                "\$_ = \$proceed(\$\$);" +
                                "}"
                    )
                }
            }
        }

    @JvmStatic
    fun hide(): Boolean =
        HiddenConfig.cardTitles
}
