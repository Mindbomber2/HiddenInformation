package com.evacipated.cardcrawl.mod.hiddeninfo.patches

import com.badlogic.gdx.graphics.Color
import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenConfig
import com.evacipated.cardcrawl.modthespire.lib.*
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.screens.SingleCardViewPopup
import javassist.CtBehavior

object HideCardCosts {
    @SpirePatch2(
        clz = AbstractCard::class,
        method = "renderEnergy"
    )
    object Card {
        @JvmStatic
        @SpireInsertPatch(
            locator = Locator::class
        )
        fun Insert(): SpireReturn<Void> {
            if (HiddenConfig.cardCosts) {
                return SpireReturn.Return()
            }
            return SpireReturn.Continue()
        }

        class Locator : SpireInsertLocator() {
            override fun Locate(ctBehavior: CtBehavior): IntArray {
                val matcher = Matcher.FieldAccessMatcher(Color::class.java, "WHITE")
                return LineFinder.findInOrder(ctBehavior, matcher)
            }
        }
    }

    @SpirePatch2(
        clz = SingleCardViewPopup::class,
        method = "renderCost"
    )
    object SCV {
        @JvmStatic
        @SpireInsertPatch(
            locator = Locator::class
        )
        fun Insert(): SpireReturn<Void> {
            if (HiddenConfig.cardCosts) {
                return SpireReturn.Return()
            }
            return SpireReturn.Continue()
        }

        class Locator : SpireInsertLocator() {
            override fun Locate(ctBehavior: CtBehavior): IntArray {
                val matcher = Matcher.FieldAccessMatcher(AbstractCard::class.java, "isCostModified")
                return LineFinder.findInOrder(ctBehavior, matcher)
            }
        }
    }
}
