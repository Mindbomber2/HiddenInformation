package com.evacipated.cardcrawl.mod.hiddeninfo.patches

import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenConfig
import com.evacipated.cardcrawl.mod.hiddeninfo.extensions.iz
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.evacipated.cardcrawl.modthespire.lib.SpirePatches2
import com.megacrit.cardcrawl.helpers.PowerTip
import com.megacrit.cardcrawl.helpers.TipHelper
import com.megacrit.cardcrawl.potions.AbstractPotion
import com.megacrit.cardcrawl.rewards.RewardItem
import com.megacrit.cardcrawl.ui.panels.PotionPopUp
import com.megacrit.cardcrawl.ui.panels.TopPanel
import javassist.expr.ExprEditor
import javassist.expr.FieldAccess
import javassist.expr.MethodCall

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

    @SpirePatch2(
        clz = TopPanel::class,
        method = "renderPotionTips"
    )
    object TopPanelTips {
        @JvmStatic
        fun Instrument(): ExprEditor =
            object : ExprEditor() {
                override fun edit(m: MethodCall) {
                    if (m.iz(TipHelper::class, "queuePowerTips")) {
                        m.replace(
                            "${Tooltip::class.qualifiedName}.Prefix(p);" +
                                    "\$3 = p.tips;" +
                                    "\$_ = \$proceed(\$\$);" +
                                    "${Tooltip::class.qualifiedName}.Postfix(p);"
                        )
                    }
                }
            }
    }

    @SpirePatch2(
        clz = PotionPopUp::class,
        method = "update"
    )
    object PotionPopUpTips {
        @JvmStatic
        fun Instrument(): ExprEditor =
            object : ExprEditor() {
                override fun edit(m: MethodCall) {
                    if (m.iz(TipHelper::class, "queuePowerTips")) {
                        m.replace(
                            "${Tooltip::class.qualifiedName}.Prefix(potion);" +
                                    "\$3 = potion.tips;" +
                                    "\$_ = \$proceed(\$\$);" +
                                    "${Tooltip::class.qualifiedName}.Postfix(potion);"
                        )
                    }
                }
            }
    }

    @SpirePatch2(
        clz = RewardItem::class,
        method = "render"
    )
    object NameReward {
        @JvmStatic
        fun Instrument(): ExprEditor =
            object : ExprEditor() {
                override fun edit(f: FieldAccess) {
                    if (f.iz(RewardItem::class, "text") && f.isReader) {
                        f.replace(
                            "if (type == ${RewardItem.RewardType::class.qualifiedName}.POTION && ${HidePotionNameDescription::class.qualifiedName}.hideName()) {" +
                                    "\$_ = \"\";" +
                                    "} else {" +
                                    "\$_ = \$proceed(\$\$);" +
                                    "}"
                        )
                    }
                }
            }
    }

    @SpirePatch2(
        clz = RewardItem::class,
        method = "update"
    )
    object DescriptionReward {
        @JvmStatic
        fun Instrument(): ExprEditor =
            object : ExprEditor() {
                override fun edit(m: MethodCall) {
                    if (m.iz(TipHelper::class, "queuePowerTips")) {
                        m.replace(
                            "${Tooltip::class.qualifiedName}.Prefix(potion);" +
                                    "\$3 = potion.tips;" +
                                    "\$_ = \$proceed(\$\$);" +
                                    "${Tooltip::class.qualifiedName}.Postfix(potion);"
                        )
                    }
                }
            }
    }

    @JvmStatic
    fun hideName(): Boolean =
        HiddenConfig.potionNames
}
