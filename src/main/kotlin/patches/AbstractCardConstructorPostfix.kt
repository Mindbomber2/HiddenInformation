package com.evacipated.cardcrawl.mod.hiddeninfo.patches

import basemod.ReflectionHacks
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Align
import com.evacipated.cardcrawl.mod.hiddeninfo.editableCardTextthingy.AbstractCardTextReceiver
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.ImageMaster
import com.megacrit.cardcrawl.helpers.input.InputHelper
import com.evacipated.cardcrawl.mod.stslib.patches.HitboxRightClick
import com.evacipated.cardcrawl.modthespire.lib.*
import com.megacrit.cardcrawl.cards.DamageInfo
import kotlin.reflect.KClass


@SpirePatch2(
    clz = AbstractCard::class,
    method = SpirePatch.CONSTRUCTOR,
    paramtypez = [String::class, String::class, String::class, Int::class, String::class, AbstractCard.CardType::class, AbstractCard.CardColor::class, AbstractCard.CardRarity::class, AbstractCard.CardTarget::class, DamageInfo.DamageType::class]
)
object AbstractCardConstructorPostfix {

    @JvmStatic
    @SpirePostfixPatch
    fun pleaseWork(
        __instance: AbstractCard,
    ) {
        /**ReplaceAbstractCardRendering.SpireFieldHolder.textReceiverField.set(
            __instance,
            AbstractCardTextReceiver(__instance)
        )*/
    }
}