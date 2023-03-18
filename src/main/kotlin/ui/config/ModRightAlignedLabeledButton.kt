package com.evacipated.cardcrawl.mod.hiddeninfo.ui.config

import basemod.ModLabeledButton
import basemod.ModPanel
import basemod.ReflectionHacks
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.megacrit.cardcrawl.core.Settings
import java.util.function.Consumer

class ModRightAlignedLabeledButton(
    label: String,
    xPos: Float,
    yPos: Float,
    textColor: Color,
    textColorHover: Color,
    font: BitmapFont,
    p: ModPanel,
    c: Consumer<ModLabeledButton>
) : ModLabeledButton(label, xPos, yPos, textColor, textColorHover, font, p, c) {
    init {
        x -= ReflectionHacks.getPrivate<Float>(this, ModLabeledButton::class.java, "w") / Settings.scale
    }

    fun height(): Float =
        ReflectionHacks.getPrivate<Float>(this, ModLabeledButton::class.java, "h") / Settings.scale
}
