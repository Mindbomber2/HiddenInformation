package com.evacipated.cardcrawl.mod.hiddeninfo.patches

import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenConfig
import com.evacipated.cardcrawl.modthespire.lib.*
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.monsters.AbstractMonster
import javassist.CtBehavior

@SpirePatch2(
    clz = AbstractCreature::class,
    method = "renderBlockIconAndValue"
)
object HideBlock {
    @JvmStatic
    @SpireInsertPatch(
        locator = Locator::class
    )
    fun Insert(__instance: AbstractCreature): SpireReturn<Void> {
        if (HiddenConfig.enemyBlock && __instance is AbstractMonster) {
            return SpireReturn.Return()
        }
        return SpireReturn.Continue()
    }

    class Locator : SpireInsertLocator() {
        override fun Locate(ctBehavior: CtBehavior): IntArray {
            val matcher = Matcher.MethodCallMatcher(FontHelper::class.java, "renderFontCentered")
            return LineFinder.findInOrder(ctBehavior, matcher)
        }
    }
}
