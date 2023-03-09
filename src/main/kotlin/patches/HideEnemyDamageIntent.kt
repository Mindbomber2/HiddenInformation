package com.evacipated.cardcrawl.mod.hiddeninfo.patches

import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenConfig
import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenInfoMod
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn
import com.megacrit.cardcrawl.helpers.PowerTip
import com.megacrit.cardcrawl.monsters.AbstractMonster

object HideEnemyDamageIntent {
    @SpirePatch2(
        clz = AbstractMonster::class,
        method = "renderDamageRange"
    )
    object Intent {
        @JvmStatic
        fun Prefix(): SpireReturn<Void> {
            if (HiddenConfig.enemyIntentDamage) {
                return SpireReturn.Return()
            }
            return SpireReturn.Continue()
        }
    }

    @SpirePatch2(
        clz = AbstractMonster::class,
        method = "updateIntentTip"
    )
    object Tooltip {
        @JvmStatic
        fun Postfix(__instance: AbstractMonster, ___intentTip: PowerTip) {
            if (HiddenConfig.enemyIntentDamage && __instance.intent.name.contains("ATTACK")) {
                ___intentTip.body = ___intentTip.body.replace(HiddenInfoMod.numberRegex, "?")
            }
        }
    }
}
