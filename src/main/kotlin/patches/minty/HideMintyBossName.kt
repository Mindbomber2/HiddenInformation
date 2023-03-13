package com.evacipated.cardcrawl.mod.hiddeninfo.patches.minty

import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenConfig
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn

@SpirePatch2(
    cls = "mintySpire.patches.map.MapBossNameDisplay",
    method = "getBossName",
    requiredModId = "mintyspire"
)
object HideMintyBossName {
    @JvmStatic
    fun Prefix(): SpireReturn<String> =
        if (HiddenConfig.bossIcon) {
            SpireReturn.Return("???")
        } else {
            SpireReturn.Continue()
        }
}
