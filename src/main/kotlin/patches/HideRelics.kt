package com.evacipated.cardcrawl.mod.hiddeninfo.patches

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenConfig
import com.evacipated.cardcrawl.mod.hiddeninfo.extensions.iz
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.evacipated.cardcrawl.modthespire.lib.SpirePatches2
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn
import com.megacrit.cardcrawl.helpers.ImageMaster
import com.megacrit.cardcrawl.helpers.PowerTip
import com.megacrit.cardcrawl.relics.AbstractRelic
import com.megacrit.cardcrawl.rewards.RewardItem
import com.megacrit.cardcrawl.screens.SingleRelicViewPopup
import com.megacrit.cardcrawl.unlock.UnlockTracker
import javassist.expr.ExprEditor
import javassist.expr.FieldAccess
import javassist.expr.MethodCall

object HideRelics {
    @SpirePatches2(
        SpirePatch2(
            clz = AbstractRelic::class,
            method = "renderTip"
        ),
        SpirePatch2(
            clz = AbstractRelic::class,
            method = "renderBossTip"
        )
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
                            "if (type == ${RewardItem.RewardType::class.qualifiedName}.RELIC && ${HideRelics::class.qualifiedName}.hideName()) {" +
                                    "\$_ = \"\";" +
                                    "} else {" +
                                    "\$_ = \$proceed(\$\$);" +
                                    "}"
                        )
                    } else if (f.iz(AbstractRelic::class, "name") && f.isReader) {
                        f.replace(
                            "if (type == ${RewardItem.RewardType::class.qualifiedName}.SAPPHIRE_KEY && ${HideRelics::class.qualifiedName}.hideName()) {" +
                                    "\$_ = \"\";" +
                                    "} else {" +
                                    "\$_ = \$proceed(\$\$);" +
                                    "}"
                        )
                    }
                }

                override fun edit(m: MethodCall) {
                    if (m.iz(ArrayList::class, "add")) {
                        m.replace(
                                    "\$1 = ${NameReward::class.qualifiedName}.fixTip(relic, \$1);" +
                                    "\$_ = \$proceed(\$\$);"
                        )
                    }
                }
            }

        // Necessary because of a patch basemod makes for whatmod
        @JvmStatic
        fun fixTip(relic: AbstractRelic?, tip: Any?): Any? {
            return if (relic != null && tip is PowerTip && relic.tips.size > 0 && relic.tips[0] == tip) {
                if (HiddenConfig.relicNames && HiddenConfig.relicDescriptions) {
                    PowerTip("\u200B", "\u200B")
                } else if (HiddenConfig.relicNames) {
                    PowerTip("\u200B", tip.body)
                } else if (HiddenConfig.relicDescriptions) {
                    PowerTip(tip.header, "\u200B")
                } else {
                    tip
                }
            } else {
                tip
            }
        }
    }

    @SpirePatches2(
        SpirePatch2(
            clz = AbstractRelic::class,
            method = "renderInTopPanel"
        ),
        SpirePatch2(
            clz = AbstractRelic::class,
            method = "render",
            paramtypez = [SpriteBatch::class]
        ),
        SpirePatch2(
            clz = AbstractRelic::class,
            method = "render",
            paramtypez = [SpriteBatch::class, Boolean::class, Color::class]
        ),
        SpirePatch2(
            clz = AbstractRelic::class,
            method = "renderWithoutAmount"
        ),
        SpirePatch2(
            clz = AbstractRelic::class,
            method = "renderOutline",
            paramtypez = [Color::class, SpriteBatch::class, Boolean::class]
        ),
        SpirePatch2(
            clz = AbstractRelic::class,
            method = "renderOutline",
            paramtypez = [SpriteBatch::class, Boolean::class]
        )
    )
    object Art {
        @JvmStatic
        fun Instrument(): ExprEditor =
            object : ExprEditor() {
                override fun edit(f: FieldAccess) {
                    if (f.iz(AbstractRelic::class, "img")) {
                        f.replace(
                            "if (${HideRelics::class.qualifiedName}.hideArt()) {" +
                                    "\$_ = ${ImageMaster::class.qualifiedName}.RELIC_LOCK;" +
                                    "} else {" +
                                    "\$_ = \$proceed(\$\$);" +
                                    "}"
                        )
                    } else if (f.iz(AbstractRelic::class, "outlineImg")) {
                        f.replace(
                            "if (${HideRelics::class.qualifiedName}.hideArt()) {" +
                                    "\$_ = ${ImageMaster::class.qualifiedName}.RELIC_LOCK_OUTLINE;" +
                                    "} else {" +
                                    "\$_ = \$proceed(\$\$);" +
                                    "}"
                        )
                    }
                }
            }
    }

    @SpirePatch2(
        clz = SingleRelicViewPopup::class,
        method = "renderRelicImage"
    )
    object ArtSRV {
        @JvmStatic
        fun Instrument(): ExprEditor =
            object : ExprEditor() {
                override fun edit(m: MethodCall) {
                    if (m.iz(UnlockTracker::class, "isRelicLocked")) {
                        m.replace(
                            "if (${HideRelics::class.qualifiedName}.hideArt()) {" +
                                    "\$_ = true;" +
                                    "} else {" +
                                    "\$_ = \$proceed(\$\$);" +
                                    "}"
                        )
                    }
                }
            }
    }

    @SpirePatch2(
        clz = AbstractRelic::class,
        method = "renderCounter"
    )
    object Counter {
        @JvmStatic
        fun Prefix(): SpireReturn<Void> {
            if (HiddenConfig.relicCounters) {
                return SpireReturn.Return()
            }
            return SpireReturn.Continue()
        }
    }

    @JvmStatic
    fun hideName(): Boolean =
        HiddenConfig.relicNames

    @JvmStatic
    fun hideArt(): Boolean =
        HiddenConfig.relicArt
}
