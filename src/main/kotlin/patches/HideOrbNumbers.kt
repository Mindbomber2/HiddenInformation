package com.evacipated.cardcrawl.mod.hiddeninfo.patches

import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenConfig
import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenInfoMod
import com.evacipated.cardcrawl.mod.hiddeninfo.extensions.iz
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn
import com.megacrit.cardcrawl.helpers.TipHelper
import com.megacrit.cardcrawl.orbs.AbstractOrb
import javassist.expr.ExprEditor
import javassist.expr.MethodCall

object HideOrbNumbers {
    @SpirePatch2(
        clz = AbstractOrb::class,
        method = "renderText"
    )
    object PassiveEvokeText {
        @JvmStatic
        fun Prefix(): SpireReturn<Void> {
            if (hide()) {
                return SpireReturn.Return()
            }
            return SpireReturn.Continue()
        }
    }

    @SpirePatch2(
        clz = AbstractOrb::class,
        method = "update"
    )
    object TooltipNumber {
        @JvmStatic
        fun Instrument(): ExprEditor =
            object : ExprEditor() {
                override fun edit(m: MethodCall) {
                    if (m.iz(TipHelper::class, "renderGenericTip")) {
                        m.replace(
                            "if (${HideOrbNumbers::class.qualifiedName}.hide()) {" +
                                    "\$4 = ${HiddenInfoMod.Statics::class.qualifiedName}.replaceNumbers(\$4);" +
                                    "}" +
                                    "\$_ = \$proceed(\$\$);"
                        )
                    }
                }
            }
    }

    @JvmStatic
    fun hide(): Boolean =
        HiddenConfig.orbNumbers
}
