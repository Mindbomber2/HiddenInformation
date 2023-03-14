package com.evacipated.cardcrawl.mod.hiddeninfo.patches

import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenConfig
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.evacipated.cardcrawl.modthespire.lib.SpirePatches2
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn
import com.megacrit.cardcrawl.helpers.PowerTip
import com.megacrit.cardcrawl.relics.AbstractRelic
import com.megacrit.cardcrawl.screens.SingleRelicViewPopup

object HideRelics {
    @SpirePatch2(
        clz = AbstractRelic::class,
        method = "renderTip"
    )
    object Tooltip {
        private var tipsSave: ArrayList<PowerTip>? = null

        @JvmStatic
        fun Prefix(__instance: AbstractRelic) {
            if (HiddenConfig.relicNames || HiddenConfig.relicDescriptions) {
                tipsSave = __instance.tips
            }

            if (tipsSave != null) {
                __instance.tips.firstOrNull()?.let { tip ->
                    __instance.tips = if (HiddenConfig.relicNames && HiddenConfig.relicDescriptions) {
                        arrayListOf(PowerTip("\u200B", "\u200B"))
                    } else if (HiddenConfig.relicNames) {
                        ArrayList(__instance.tips).apply {
                            this[0] = PowerTip("\u200B", this[0].body)
                        }
                    } else if (HiddenConfig.relicDescriptions) {
                        arrayListOf(PowerTip(tip.header, "\u200B"))
                    } else {
                        __instance.tips
                    }
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

    @SpirePatch2(
        clz = SingleRelicViewPopup::class,
        method = "renderName"
    )
    object NameSRV {
        @JvmStatic
        fun Prefix(): SpireReturn<Void> {
            if (HiddenConfig.relicNames) {
                return SpireReturn.Return()
            }
            return SpireReturn.Continue()
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
    object DescriptionSRV {
        @JvmStatic
        fun Prefix(): SpireReturn<Void> {
            if (HiddenConfig.relicDescriptions) {
                return SpireReturn.Return()
            }
            return SpireReturn.Continue()
        }
    }

    @SpirePatch2(
        clz = SingleRelicViewPopup::class,
        method = "renderQuote"
    )
    object FlavorText {
        @JvmStatic
        fun Prefix(): SpireReturn<Void> {
            if (HiddenConfig.relicFlavor) {
                return SpireReturn.Return()
            }
            return SpireReturn.Continue()
        }
    }
}
