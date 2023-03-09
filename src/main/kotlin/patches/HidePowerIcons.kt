package com.evacipated.cardcrawl.mod.hiddeninfo.patches

import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenConfig
import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenInfoMod
import com.evacipated.cardcrawl.mod.hiddeninfo.extensions.iz
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.helpers.PowerTip
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.AbstractPower
import javassist.expr.ExprEditor
import javassist.expr.MethodCall
import javassist.expr.NewExpr

object HidePowerIcons {
    @SpirePatch2(
        clz = AbstractCreature::class,
        method = "renderPowerIcons"
    )
    object IconAmount {
        @JvmStatic
        fun Instrument(): ExprEditor =
            object : ExprEditor() {
                override fun edit(m: MethodCall) {
                    if (m.iz(AbstractPower::class, "renderAmount")) {
                        m.replace(
                            "if (!${HidePowerIcons::class.qualifiedName}.hideAmount(this)) {" +
                                    "\$_ = \$proceed(\$\$);" +
                                    "}"
                        )
                    }
                }
            }
    }

    @SpirePatch2(
        clz = AbstractMonster::class,
        method = "renderTip"
    )
    object EnemyTooltipAmount {
        @JvmStatic
        fun Instrument(): ExprEditor =
            object : ExprEditor() {
                override fun edit(e: NewExpr) {
                    if (e.iz(PowerTip::class)) {
                        e.replace(
                            "if (${HidePowerIcons::class.qualifiedName}.hideAmount(this)) {" +
                                    "\$2 = ${HiddenInfoMod.Statics::class.qualifiedName}.replaceNumbers(\$2);" +
                                    "}" +
                                    "\$_ = \$proceed(\$\$);"
                        )
                    }
                }
            }
    }

    @SpirePatch2(
        clz = AbstractPlayer::class,
        method = "renderPowerTips"
    )
    object PlayerTooltipAmount {
        @JvmStatic
        fun Instrument(): ExprEditor =
            object : ExprEditor() {
                override fun edit(e: NewExpr) {
                    if (e.iz(PowerTip::class)) {
                        e.replace(
                            "if (${HidePowerIcons::class.qualifiedName}.hideAmount(this)) {" +
                                    "\$2 = ${HiddenInfoMod.Statics::class.qualifiedName}.replaceNumbers(\$2);" +
                                    "}" +
                                    "\$_ = \$proceed(\$\$);"
                        )
                    }
                }
            }
    }

    @JvmStatic
    fun hideAmount(instance: AbstractCreature): Boolean =
        (instance is AbstractMonster && HiddenConfig.enemyPowerAmount)
                || (instance is AbstractPlayer && HiddenConfig.playerPowerAmount)
}
