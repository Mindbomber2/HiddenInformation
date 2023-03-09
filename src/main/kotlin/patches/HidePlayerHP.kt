package com.evacipated.cardcrawl.mod.hiddeninfo.patches

import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenConfig
import com.evacipated.cardcrawl.mod.hiddeninfo.extensions.iz
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.ui.panels.TopPanel
import javassist.expr.ExprEditor
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
                            "if (!${TopPanelHP::class.qualifiedName}.hide()) {" +
                                    "\$_ = \$proceed(\$\$);" +
                                    "}"
                        )
                    }
                }
            }

        @JvmStatic
        fun hide(): Boolean =
            HiddenConfig.playerHP
    }
}
