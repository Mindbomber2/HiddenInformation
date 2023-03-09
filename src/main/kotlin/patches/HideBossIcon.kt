package com.evacipated.cardcrawl.mod.hiddeninfo.patches

import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenConfig
import com.evacipated.cardcrawl.mod.hiddeninfo.extensions.assetPath
import com.evacipated.cardcrawl.modthespire.lib.*
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.ImageMaster
import com.megacrit.cardcrawl.map.DungeonMap
import javassist.CtBehavior

@SpirePatch2(
    clz = AbstractDungeon::class,
    method = "setBoss"
)
object HideBossIcon {
    @JvmStatic
    @SpireInsertPatch(
        locator = Locator::class
    )
    fun Insert(): SpireReturn<Void> {
        if (HiddenConfig.bossIcon) {
            DungeonMap.boss = ImageMaster.loadImage("images/hiddenBossIcon.png".assetPath())
            DungeonMap.bossOutline = ImageMaster.loadImage("images/hiddenBossIconOutline.png".assetPath())
            return SpireReturn.Return()
        }
        return SpireReturn.Continue()
    }

    class Locator : SpireInsertLocator() {
        override fun Locate(ctBehavior: CtBehavior): IntArray {
            val matcher = Matcher.MethodCallMatcher(String::class.java, "equals")
            return LineFinder.findInOrder(ctBehavior, matcher)
        }
    }
}
