package com.evacipated.cardcrawl.mod.hiddeninfo.patches

import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenConfig
import com.evacipated.cardcrawl.mod.hiddeninfo.extensions.iz
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption
import com.megacrit.cardcrawl.ui.campfire.RestOption
import com.megacrit.cardcrawl.ui.panels.TopPanel
import javassist.expr.ExprEditor
import javassist.expr.FieldAccess
import javassist.expr.MethodCall

object HidePlayerHP {
    @SpirePatch2(
        clz = AbstractCreature::class,
        method = "renderHealthText"
    )
    object HPBar {
        @JvmStatic
        fun Prefix(__instance: AbstractCreature): SpireReturn<Void> {
            if (HiddenConfig.playerHP && __instance is AbstractPlayer) {
                return SpireReturn.Return()
            }
            return SpireReturn.Continue()
        }
    }

    @SpirePatch2(
        clz = TopPanel::class,
        method = "renderHP"
    )
    object TopPanelHP {
        @JvmStatic
        fun Instrument(): ExprEditor =
            object : ExprEditor() {
                override fun edit(m: MethodCall) {
                    if (m.iz(FontHelper::class, "renderFontLeftTopAligned")) {
                        m.replace(
                            "if (!${HidePlayerHP::class.qualifiedName}.hide()) {" +
                                    "\$_ = \$proceed(\$\$);" +
                                    "}"
                        )
                    }
                }
            }
    }

    @SpirePatch2(
        clz = AbstractCampfireOption::class,
        method = "render"
    )
    object CampfireRestOption {
        @JvmStatic
        fun Instrument(): ExprEditor =
            object : ExprEditor() {
                override fun edit(f: FieldAccess) {
                    if (f.iz(AbstractCampfireOption::class, "description") && f.isReader) {
                        f.replace(
                            "\$_ = \$proceed(\$\$);" +
                                    "if (this instanceof ${RestOption::class.qualifiedName} && ${HidePlayerHP::class.qualifiedName}.hide()) {" +
                                    "\$_ = ${CampfireRestOption::class.qualifiedName}.filter(\$_);" +
                                    "}"
                        )
                    }
                }
            }

        private val regex = Regex(""" ?\(\d+\)""")

        @JvmStatic
        fun filter(description: String): String {
            return regex.replace(description, "")
        }
    }

    @JvmStatic
    fun hide(): Boolean =
        HiddenConfig.playerHP
}
