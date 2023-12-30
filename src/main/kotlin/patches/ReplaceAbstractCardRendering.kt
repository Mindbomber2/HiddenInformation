package com.evacipated.cardcrawl.mod.hiddeninfo.patches

import basemod.ReflectionHacks
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Align
import com.evacipated.cardcrawl.mod.hiddeninfo.editableCardTextthingy.AbstractCardTextReceiver
import com.evacipated.cardcrawl.modthespire.lib.*
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.AbstractCard.IMG_HEIGHT
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.ImageMaster


@SpirePatch2(
    clz = AbstractCard::class,
    method = "renderDescription"
)
object ReplaceAbstractCardRendering {
    /**@SpirePatch2(
        clz = AbstractCard::class,
        method = SpirePatch.CLASS
    )
    object SpireFieldHolder {
        @JvmField
        val textReceiverField: SpireField<AbstractCardTextReceiver?> = SpireField { null }
    }*/


    @JvmStatic
    fun Replace(sb: SpriteBatch, __instance: AbstractCard) {
        //val textReceiver = SpireFieldHolder.textReceiverField[__instance]!!
        val textReceiver = AbstractCardTextReceiver(__instance)
        val DESC_OFFSET_Y = if (Settings.BIG_TEXT_MODE) {
            IMG_HEIGHT * 0.24f
        } else {
            IMG_HEIGHT * 0.255f
        }

        val font = ReflectionHacks.privateMethod(AbstractCard::class.java, "getDescFont").invoke<BitmapFont>(__instance)
        val origLineHeight = font.lineHeight
        font.data.setLineHeight(font.capHeight * 1.45f / __instance.drawScale)

        textReceiver.gl.setText(
            font,
            __instance.rawDescription,
            Color.WHITE,
            textReceiver.TEXT_WIDTH * __instance.drawScale,
            Align.center,
            true
        )
        val drawX = __instance.current_x - textReceiver.TEXT_WIDTH * __instance.drawScale / 2f
        val drawY =
            __instance.current_y - IMG_HEIGHT * __instance.drawScale / 2f + DESC_OFFSET_Y * __instance.drawScale + textReceiver.gl.height / 2f
        font.draw(sb, textReceiver.gl, drawX, drawY)

        font.data.setLineHeight(origLineHeight)

        if (textReceiver.typingBarTimer >= 0f) {
            if (textReceiver.typingBarOn) {
                // render "typing" bar
                textReceiver.gl.runs.lastOrNull()?.let { lastGlyph ->
                    var xAdvance = 0f
                    for (i in 0 until lastGlyph.xAdvances.size) {
                        xAdvance += lastGlyph.xAdvances[i]
                    }
                    sb.draw(
                        ImageMaster.WHITE_SQUARE_IMG,
                        if (__instance.rawDescription.endsWith('\n')) {
                            __instance.current_x
                        } else {
                            drawX + lastGlyph.x + xAdvance
                        },
                        drawY - textReceiver.gl.height,
                        2f * __instance.drawScale,
                        font.capHeight
                    )
                } ?: run {
                    sb.draw(
                        ImageMaster.WHITE_SQUARE_IMG,
                        __instance.current_x,
                        drawY - font.capHeight,
                        2f * __instance.drawScale,
                        font.capHeight
                    )
                }
            }

            textReceiver.typingBarTimer -= Gdx.graphics.rawDeltaTime
            if (textReceiver.typingBarTimer < 0f) {
                textReceiver.typingBarTimer = 1f
                textReceiver.typingBarOn = !textReceiver.typingBarOn
            }
        }
    }
}