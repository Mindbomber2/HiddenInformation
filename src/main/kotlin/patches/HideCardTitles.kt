package com.evacipated.cardcrawl.mod.hiddeninfo.patches

import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenConfig
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.evacipated.cardcrawl.modthespire.lib.SpirePatches2
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.screens.SingleCardViewPopup

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
    fun Prefix(): SpireReturn<Void> {
        if (HiddenConfig.cardTitles) {
            return SpireReturn.Return()
        }
        return SpireReturn.Continue()
    }
}
