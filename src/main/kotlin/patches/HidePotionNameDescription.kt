package com.evacipated.cardcrawl.mod.hiddeninfo.patches

import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenConfig
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.evacipated.cardcrawl.modthespire.lib.SpirePatches2
import com.megacrit.cardcrawl.helpers.PowerTip
import com.megacrit.cardcrawl.potions.AbstractPotion

object HidePotionNameDescription {
    @SpirePatches2(
        SpirePatch2(
            clz = AbstractPotion::class,
            method = "render"
        ),
        SpirePatch2(
            clz = AbstractPotion::class,
            method = "shopRender"
        ),
        SpirePatch2(
            clz = AbstractPotion::class,
            method = "labRender"
        )
    )
    object Tooltip {
        private var tipsSave: ArrayList<PowerTip>? = null

        @JvmStatic
        fun Prefix(__instance: AbstractPotion) {
            if (HiddenConfig.potionNames || HiddenConfig.potionDescriptions) {
                tipsSave = __instance.tips
            }

            if (tipsSave != null) {
                __instance.tips.firstOrNull()?.let { tip ->
                    __instance.tips = if (HiddenConfig.potionNames && HiddenConfig.potionDescriptions) {
                        arrayListOf(PowerTip("\u200B", "\u200B"))
                    } else if (HiddenConfig.potionNames) {
                        ArrayList(__instance.tips).apply {
                            this[0] = PowerTip("\u200B", this[0].body)
                        }
                    } else if (HiddenConfig.potionDescriptions) {
                        arrayListOf(PowerTip(tip.header, "\u200B"))
                    } else {
                        __instance.tips
                    }
                }
            }
        }

        @JvmStatic
        fun Postfix(__instance: AbstractPotion) {
            if (tipsSave != null) {
                __instance.tips = tipsSave
                tipsSave = null
            }
        }
    }
}
