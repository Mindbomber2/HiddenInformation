package com.evacipated.cardcrawl.mod.hiddeninfo.patches

import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenConfig
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.evacipated.cardcrawl.modthespire.lib.SpirePatches2
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn
import com.megacrit.cardcrawl.helpers.PowerTip
import com.megacrit.cardcrawl.relics.AbstractRelic
import com.megacrit.cardcrawl.screens.SingleRelicViewPopup

object HideRelicDescriptions {
    @SpirePatch2(
        clz = AbstractRelic::class,
        method = "renderTip"
    )
    object Tooltip {
        private var tipsSave: ArrayList<PowerTip>? = null

        @JvmStatic
        fun Prefix(__instance: AbstractRelic) {
            if (HiddenConfig.relicDescriptions) {
                __instance.tips.firstOrNull()?.let { tip ->
                    tipsSave = __instance.tips
                    __instance.tips = arrayListOf(
                        PowerTip(tip.header, "\u200B")
                    )
                }
            }
        }

        @JvmStatic
        fun Postfix(__instance: AbstractRelic) {
            if (tipsSave != null) {
                __instance.tips = tipsSave
                tipsSave = null
            }
        }
    }

    @SpirePatches2(
        SpirePatch2(
            clz = SingleRelicViewPopup::class,
            method = "renderDescription"
        ),
        SpirePatch2(
            clz = SingleRelicViewPopup::class,
            method = "renderTips"
        )
    )
    object SRV {
        @JvmStatic
        fun Prefix(): SpireReturn<Void> {
            if (HiddenConfig.relicDescriptions) {
                return SpireReturn.Return()
            }
            return SpireReturn.Continue()
        }
    }
}
