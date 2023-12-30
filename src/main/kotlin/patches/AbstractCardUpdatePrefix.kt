package com.evacipated.cardcrawl.mod.hiddeninfo.patches

import basemod.ReflectionHacks
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Align
import com.evacipated.cardcrawl.mod.hiddeninfo.editableCardTextthingy.AbstractCardTextReceiver
import com.evacipated.cardcrawl.modthespire.lib.SpireField
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.ImageMaster
import com.megacrit.cardcrawl.helpers.input.InputHelper
import com.evacipated.cardcrawl.mod.stslib.patches.HitboxRightClick


@SpirePatch2(
    clz = AbstractCard::class,
    method = "update"
)
object AbstractCardUpdatePrefix {


    @JvmStatic
    @SpirePrefixPatch
    fun pleaseWork(__instance: AbstractCard) {
        /*val textReceiver = ReplaceAbstractCardRendering.SpireFieldHolder.textReceiverField.get(__instance)!!*/
        val textReceiver = AbstractCardTextReceiver(__instance)

        if (HitboxRightClick.rightClicked[__instance.hb]) {
            textReceiver.onRightClick()
        } else if ((InputHelper.justClickedLeft || InputHelper.justClickedRight) && !__instance.hb.hovered) {
            textReceiver.onKeyDown(Input.Keys.ESCAPE)
        }
    }
}