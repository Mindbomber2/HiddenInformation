package com.evacipated.cardcrawl.mod.hiddeninfo.patches

import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenConfig
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn
import com.megacrit.cardcrawl.screens.SingleRelicViewPopup

@SpirePatch2(
    clz = SingleRelicViewPopup::class,
    method = "renderQuote"
)
object HideRelicFlavor {
    @JvmStatic
    fun Prefix(): SpireReturn<Void> {
        if (HiddenConfig.relicFlavor) {
            return SpireReturn.Return()
        }
        return SpireReturn.Continue()
    }
}
