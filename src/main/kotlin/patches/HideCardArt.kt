package com.evacipated.cardcrawl.mod.hiddeninfo.patches

import basemod.ReflectionHacks
import com.evacipated.cardcrawl.mod.hiddeninfo.HiddenConfig
import com.evacipated.cardcrawl.mod.hiddeninfo.extensions.iz
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.evacipated.cardcrawl.modthespire.lib.SpirePatches2
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.ImageMaster
import com.megacrit.cardcrawl.screens.SingleCardViewPopup
import javassist.expr.ExprEditor
import javassist.expr.FieldAccess

object HideCardArt {
    @SpirePatches2(
        SpirePatch2(
            clz = AbstractCard::class,
            method = "renderPortrait"
        ),
        SpirePatch2(
            clz = AbstractCard::class,
            method = "renderJokePortrait"
        )
    )
    object Card {
        @JvmStatic
        fun Prefix(__instance: AbstractCard) {
            start(__instance, false)
        }

        @JvmStatic
        fun Postfix(__instance: AbstractCard) {
            end(__instance)
        }
    }

    @SpirePatch2(
        clz = SingleCardViewPopup::class,
        method = "renderPortrait"
    )
    object SCV {
        @JvmStatic
        fun Prefix(___card: AbstractCard) {
            start(___card, true)
        }

        @JvmStatic
        fun Postfix(___card: AbstractCard) {
            end(___card)
        }
    }

    private var isLockedSave = false

    private fun start(card: AbstractCard, scv: Boolean) {
        if (HiddenConfig.cardArt) {
            isLockedSave = card.isLocked
            card.isLocked = true

            if (!scv) {
                val lockedTex = when (card.type) {
                    AbstractCard.CardType.ATTACK -> ImageMaster.CARD_LOCKED_ATTACK
                    AbstractCard.CardType.POWER -> ImageMaster.CARD_LOCKED_POWER
                    else -> ImageMaster.CARD_LOCKED_SKILL
                }
                ReflectionHacks.setPrivate(card, AbstractCard::class.java, "portraitImg", lockedTex)
            }
        }
    }

    private fun end(card: AbstractCard) {
        if (HiddenConfig.cardArt) {
            card.isLocked = isLockedSave
        }
    }

    @SpirePatches2(
        SpirePatch2(
            clz = AbstractCard::class,
            method = "renderImage"
        ),
        SpirePatch2(
            clz = SingleCardViewPopup::class,
            method = "loadPortraitImg"
        )
    )
    object BetaArt {
        @JvmStatic
        fun Instrument(): ExprEditor =
            object : ExprEditor() {
                override fun edit(f: FieldAccess) {
                    if (f.iz(Settings::class, "PLAYTESTER_ART_MODE")) {
                        f.replace(
                            "\$_ = \$proceed(\$\$) || ${BetaArt::class.qualifiedName}.enabled();"
                        )
                    }
                }
            }

        @JvmStatic
        fun enabled(): Boolean =
            HiddenConfig.cardBetaArt
    }
}
