package com.evacipated.cardcrawl.mod.hiddeninfo.patches

import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenConfig
import com.evacipated.cardcrawl.mod.hiddeninfo.extensions.iz
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.evacipated.cardcrawl.modthespire.lib.SpirePatches2
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.vfx.SumDamageEffect
import com.megacrit.cardcrawl.vfx.combat.BlockedNumberEffect
import com.megacrit.cardcrawl.vfx.combat.DamageNumberEffect
import javassist.expr.ExprEditor
import javassist.expr.MethodCall

@SpirePatches2(
    SpirePatch2(
        clz = DamageNumberEffect::class,
        method = "render"
    ),
    SpirePatch2(
        clz = SumDamageEffect::class,
        method = "render"
    ),
    SpirePatch2(
        clz = BlockedNumberEffect::class,
        method = "render"
    )
)
object HideFlyingDamageNumbers {
    @JvmStatic
    fun Instrument(): ExprEditor =
        object : ExprEditor() {
            override fun edit(m: MethodCall) {
                if (m.iz(FontHelper::class, "renderFontCentered")) {
                    m.replace(
                        "if (${HideFlyingDamageNumbers::class.qualifiedName}.hide()) {" +
                                "\$3 = \"?\";" +
                                "}" +
                                "\$_ = \$proceed(\$\$);"
                    )
                }
            }
        }

    @JvmStatic
    fun hide(): Boolean =
        HiddenConfig.damageNumbers
}
