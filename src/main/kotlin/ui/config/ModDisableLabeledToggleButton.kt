package com.evacipated.cardcrawl.mod.hiddeninfo.ui.config

import basemod.ModLabel
import basemod.ModLabeledToggleButton
import basemod.ModPanel
import basemod.ModToggleButton
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.evacipated.cardcrawl.mod.hiddeninfo.extensions.assetPath
import java.util.function.Consumer

class ModDisableLabeledToggleButton(
    labelText: String,
    tooltipText: String?,
    xPos: Float,
    yPos: Float,
    color: Color?,
    font: BitmapFont,
    enabled: Boolean,
    p: ModPanel,
    private val disableIf: () -> Boolean,
    labelUpdate: Consumer<ModLabel>,
    c: Consumer<ModToggleButton>
) : ModLabeledToggleButton(labelText, tooltipText, xPos, yPos, color, font, enabled, p, labelUpdate, c) {
    override fun update() {
        if (!disableIf.invoke()) {
            super.update()
        }
    }

    override fun render(sb: SpriteBatch) {
        if (disableIf.invoke()) {
            sb.end()
            val prevShader = sb.shader
            sb.shader = shader
            sb.begin()
            super.render(sb)
            sb.end()
            sb.shader = prevShader
            sb.begin()
        } else {
            super.render(sb)
        }
    }

    companion object {
        private val shader by lazy {
            ShaderProgram(
                Gdx.files.internal("shaders/grayscale/vertexShader.vs"),
                Gdx.files.internal("shaders/grayscale.frag".assetPath())
            )
        }
    }
}
