package com.evacipated.cardcrawl.mod.hiddeninfo.patches

import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenConfig
import com.evacipated.cardcrawl.mod.hiddeninfo.extensions.iz
import com.evacipated.cardcrawl.modthespire.lib.SpireField
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.evacipated.cardcrawl.modthespire.lib.SpirePatches2
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.vfx.combat.HealEffect
import com.megacrit.cardcrawl.vfx.combat.HealNumberEffect
import javassist.expr.ExprEditor
import javassist.expr.MethodCall
import javassist.expr.NewExpr

object HideHealEffectNumber {
    @SpirePatch2(
        clz = HealNumberEffect::class,
        method = SpirePatch.CLASS
    )
    object Field {
        @JvmField
        val source: SpireField<AbstractCreature?> = SpireField { null }
    }

    @SpirePatches2(
        SpirePatch2(
            clz = AbstractCreature::class,
            method = "heal",
            paramtypez = [Int::class, Boolean::class]
        ),
        SpirePatch2(
            clz = AbstractMonster::class,
            method = "heal",
            paramtypez = [Int::class]
        )
    )
    object SetSource {
        @JvmStatic
        fun Instrument(): ExprEditor =
            object : ExprEditor() {
                override fun edit(e: NewExpr) {
                    if (e.iz(HealEffect::class)) {
                        e.replace(
                            "\$_ = \$proceed(\$\$);" +
                                    "${SetSource::class.qualifiedName}.setSource(this);"
                        )
                    }
                }
            }

        @JvmStatic
        fun setSource(instance: AbstractCreature) {
            AbstractDungeon.effectsQueue
                .asReversed()
                .firstOrNull { it is HealNumberEffect }
                ?.let { Field.source[it] = instance }
        }
    }

    @SpirePatch2(
        clz = HealNumberEffect::class,
        method = "render"
    )
    object HideNumber {
        @JvmStatic
        fun Instrument(): ExprEditor =
            object : ExprEditor() {
                override fun edit(m: MethodCall) {
                    if (m.iz(FontHelper::class, "renderFontCentered")) {
                        m.replace(
                            "if (${HideHealEffectNumber::class.qualifiedName}.hide(this)) {" +
                                    "\$3 = \"?\";" +
                                    "}" +
                                    "\$_ = \$proceed(\$\$);"
                        )
                    }
                }
            }
    }

    @JvmStatic
    fun hide(vfx: HealNumberEffect): Boolean {
        val instance = Field.source[vfx]
        return (instance is AbstractMonster && HiddenConfig.enemyHP)
                || (instance is AbstractPlayer && HiddenConfig.playerHP)
    }
}
