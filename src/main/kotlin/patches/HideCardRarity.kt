package com.evacipated.cardcrawl.mod.hiddeninfo.patches

import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenConfig
import com.evacipated.cardcrawl.mod.hiddeninfo.extensions.iz
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.evacipated.cardcrawl.modthespire.lib.SpirePatches2
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.screens.SingleCardViewPopup
import javassist.expr.ExprEditor
import javassist.expr.FieldAccess

object HideCardRarity {
    @SpirePatches2(
        SpirePatch2(
            clz = AbstractCard::class,
            method = "renderBannerImage"
        ),
        SpirePatch2(
            clz = AbstractCard::class,
            method = "renderAttackPortrait"
        ),
        SpirePatch2(
            clz = AbstractCard::class,
            method = "renderSkillPortrait"
        ),
        SpirePatch2(
            clz = AbstractCard::class,
            method = "renderPowerPortrait"
        ),
        SpirePatch2(
            clz = AbstractCard::class,
            method = "renderDynamicFrame"
        ),
        SpirePatch2(
            clz = SingleCardViewPopup::class,
            method = "renderFrame"
        ),
        SpirePatch2(
            clz = SingleCardViewPopup::class,
            method = "renderCardBanner"
        )
    )
    object Card {
        @JvmStatic
        fun Instrument(): ExprEditor =
            object : ExprEditor() {
                override fun edit(f: FieldAccess) {
                    if (f.iz(AbstractCard::class, "rarity") && f.isReader) {
                        f.replace(
                            "if (${HideCardRarity::class.qualifiedName}.hide()) {" +
                                    "\$_ = ${AbstractCard.CardRarity::class.qualifiedName}.COMMON;" +
                                    "} else {" +
                                    "\$_ = \$proceed(\$\$);" +
                                    "}"
                        )
                    }
                }
            }
    }

    @JvmStatic
    fun hide(): Boolean =
        HiddenConfig.cardRarity
}
