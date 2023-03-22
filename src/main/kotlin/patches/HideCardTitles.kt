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
                        "if (${card(m)}.upgraded && ${HideCardTitles::class.qualifiedName}.hideButKeepUpgrade()) {" +
                                "\$3 = ${HideCardTitles::class.qualifiedName}.extractUpgrade(${card(m)});" +
                                "\$_ = \$proceed(\$\$);" +
                                "} else if (!${HideCardTitles::class.qualifiedName}.hide()) {" +
                                "\$_ = \$proceed(\$\$);" +
                                "}"
                    )
                }
            }

            private fun card(m: MethodCall): String =
                if (m.enclosingClass.name == AbstractCard::class.java.name) {
                    "this"
                } else {
                    "card"
                }
        }

    private val regex = Regex(""".*(\+\d*)""")

    @JvmStatic
    fun extractUpgrade(card: AbstractCard): String {
        return if (card.upgraded) {
            val result = regex.matchEntire(card.name)
            if (result != null) {
                result.groupValues[1]
            } else if (card.timesUpgraded == 1) {
                "+"
            } else {
                "+${card.timesUpgraded}"
            }
        } else {
            ""
        }
    }

    @JvmStatic
    fun hide(): Boolean =
        HiddenConfig.cardTitles

    @JvmStatic
    fun hideButKeepUpgrade(): Boolean =
        HiddenConfig.cardTitles && HiddenConfig.showCardUpgrades
}
