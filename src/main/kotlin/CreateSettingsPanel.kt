package com.evacipated.cardcrawl.mod.hiddeninfo

import basemod.*
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.evacipated.cardcrawl.mod.hiddeninfo.ui.config.ModCenteredLabel
import com.evacipated.cardcrawl.mod.hiddeninfo.ui.config.ModDisableLabeledToggleButton
import com.evacipated.cardcrawl.mod.hiddeninfo.ui.config.ModRightAlignedLabeledButton
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.helpers.ImageMaster
import kotlin.math.max
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty0
import kotlin.reflect.jvm.isAccessible

fun createSettingsPanel(): ModPanel {
    return object: ModPanel() {
        override fun update() {
            super.update()
            if (!BaseMod.modSettingsUp) {
                HiddenConfig.save()
            }
        }
    }.apply {
        ModCenteredLabel(
            HiddenInfoMod.NAME,
            Settings.WIDTH.unXScale() / 2f,
            Settings.OPTION_Y.unYScale() + 321f,
            Settings.CREAM_COLOR,
            FontHelper.buttonLabelFont,
            this
        ) {}.let { this.addUIElement(it) }

        val h = rbutton("disableAll", 1560f, Settings.OPTION_Y.unYScale() - 308f, font = FontHelper.tipHeaderFont) {
            HiddenConfig.disableAll()
        }.height()
        rbutton("enableAll", 1560f, Settings.OPTION_Y.unYScale() - 308f + h, font = FontHelper.tipHeaderFont) {
            HiddenConfig.enableAll()
        }

        column {
            label("cardsHeader")
            indent {
                checkbox(HiddenConfig::cardTitles)
                indent(32f) {
                    checkbox(HiddenConfig::showCardUpgrades, disableIf = not(HiddenConfig::cardTitles))
                }
                checkbox(HiddenConfig::cardDescriptions)
                checkbox(HiddenConfig::cardArt)
                indent(32f) {
                    checkbox(HiddenConfig::cardBetaArt, disableIf = HiddenConfig::cardArt)
                }
                checkbox(HiddenConfig::cardRarity)
                checkbox(HiddenConfig::cardCosts)
            }
            vspace()
            label("relicsHeader")
            indent {
                checkbox(HiddenConfig::relicNames)
                checkbox(HiddenConfig::relicDescriptions)
                checkbox(HiddenConfig::relicCounters)
                checkbox(HiddenConfig::relicFlavor)
                checkbox(HiddenConfig::relicArt)
            }
        }

        column {
            label("playerHeader")
            indent {
                checkbox(HiddenConfig::playerHP)
                checkbox(HiddenConfig::playerBlock)
                checkbox(HiddenConfig::playerPowerAmount)
                checkbox(HiddenConfig::playerPowerNames)
                checkbox(HiddenConfig::playerPowerDescriptions)
                checkbox(HiddenConfig::playerEnergy)
                checkbox(HiddenConfig::orbNumbers)
            }
            vspace()
            label("goldHeader")
            indent {
                checkbox(HiddenConfig::playerGold)
                checkbox(HiddenConfig::rewardGold)
                checkbox(HiddenConfig::shopPrices)
            }
            vspace()
            label("potionsHeader")
            indent {
                checkbox(HiddenConfig::potionNames)
                checkbox(HiddenConfig::potionDescriptions)
                checkbox(HiddenConfig::potionArt)
            }
        }

        column {
            label("enemiesHeader")
            indent {
                checkbox(HiddenConfig::enemy)
                checkbox(HiddenConfig::enemyHP)
                checkbox(HiddenConfig::enemyBlock)
                checkbox(HiddenConfig::enemyIntentDamage)
                indent(32f) {
                    checkbox(HiddenConfig::enemyIntentDamageImg, disableIf = not(HiddenConfig::enemyIntentDamage))
                }
                checkbox(HiddenConfig::damageNumbers)
                checkbox(HiddenConfig::enemyPowerAmount)
                checkbox(HiddenConfig::enemyPowerNames)
                checkbox(HiddenConfig::enemyPowerDescriptions)
            }
        }

        column {
            label("mapHeader")
            indent {
                checkbox(HiddenConfig::mapNodeType)
                checkbox(HiddenConfig::bossIcon)
            }
            vspace()
            label("eventsHeader")
            indent {
                checkbox(HiddenConfig::eventNames)
                checkbox(HiddenConfig::eventText)
                checkbox(HiddenConfig::eventOptions)
                indent(32f) {
                    checkbox(HiddenConfig::eventOptionsEffect, disableIf = HiddenConfig::eventOptions)
                }
                checkbox(HiddenConfig::eventArt)
            }
        }
    }
}

private var x = 360f
private var y = Settings.OPTION_Y.unYScale() + 242f
private var width = 0f
private var indent = 0f

private inline fun column(rPad: Float = 20f, size: Float = -1f, block: () -> Unit) {
    val ySave = y
    block.invoke()
    x += if (size > 0) { size } else { width } + rPad
    width = 0f
    y = ySave
}

private inline fun indent(size: Float = 16f, block: () -> Unit) {
    indent += size
    x += size
    block.invoke()
    x -= size
    indent -= size
}

private fun vspace(size: Float = 16f) {
    y -= size
}

private fun ModPanel.label(key: String) {
    val font = FontHelper.charDescFont
    val text = HiddenConfig._strings[key] ?: key
    ModLabel(text, x, y, Settings.CREAM_COLOR, font, this) {
    }.let {
        this.addUIElement(it)
        y -= font.lineHeight / Settings.scale
        val w = FontHelper.getWidth(it.font, it.text, 1f)
        width = max(width, w / Settings.scale + indent)
    }
}

private fun ModPanel.checkbox(kprop: KMutableProperty0<Boolean>, vararg nothings: MyNothing, disableIf: () -> Boolean = {false}) {
    val text = HiddenConfig._strings[kprop.name] ?: kprop.name
    val tooltip = HiddenConfig._strings["${kprop.name}_tooltip"]
    ModDisableLabeledToggleButton(text, tooltip, x, y, Settings.CREAM_COLOR, FontHelper.tipBodyFont, kprop.get(), this, disableIf, {}) {
        kprop.set(it.enabled)
    }.let {
        this.addUIElement(it)
        kprop.isAccessible = true
        (kprop.getDelegate() as? HiddenConfig.Companion.Setting)?.setModToggleButton(it.toggle)
        y -= ImageMaster.OPTION_TOGGLE.height
        val hb = ReflectionHacks.getPrivate<Hitbox>(it.toggle, ModToggleButton::class.java, "hb")
        width = max(width, hb.width / Settings.scale + indent)
    }
}

private fun ModPanel.rbutton(key: String, x: Float, y: Float, textColor: Color = Color.WHITE, font: BitmapFont = FontHelper.buttonLabelFont, pressed: (ModLabeledButton) -> Unit): ModRightAlignedLabeledButton {
    val text = HiddenConfig._strings[key] ?: key
    return ModRightAlignedLabeledButton(text, x, y, textColor, Color.GREEN, font, this, pressed).also {
        this.addUIElement(it)
    }
}

private fun not(kprop: KProperty0<Boolean>): () -> Boolean =
    { !kprop.get() }

private fun Number.unXScale(): Float =
    this.toFloat() / Settings.xScale

private fun Number.unYScale(): Float =
    this.toFloat() / Settings.yScale

private class MyNothing private constructor() {}
