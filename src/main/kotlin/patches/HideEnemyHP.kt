package com.evacipated.cardcrawl.mod.hiddeninfo.patches

import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenConfig
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.monsters.AbstractMonster

@SpirePatch2(
    clz = AbstractCreature::class,
    method = "renderHealthText"
)
object HideEnemyHP {
    @JvmStatic
    fun Prefix(__instance: AbstractCreature): SpireReturn<Void> {
        if (HiddenConfig.enemyHP && __instance is AbstractMonster) {
            return SpireReturn.Return()
        }
        return SpireReturn.Continue()
    }
}
